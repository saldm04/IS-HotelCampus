package it.unisa.hotelcampus.gestioneutenti.service;

import it.unisa.hotelcampus.model.dao.ClienteDettagliRepository;
import it.unisa.hotelcampus.model.dao.UtenteRepository;
import it.unisa.hotelcampus.model.entity.ClienteDettagli;
import it.unisa.hotelcampus.model.entity.Utente;
import it.unisa.hotelcampus.utils.acl.ControllaACL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

/**
 * Implementazione del servizio {@link GestioneUtentiService}.
 * Gestisce l'autenticazione, la creazione, l'eliminazione e la gestione dei ruoli degli utenti nel sistema HotelCampus.
 * Utilizza i repository {@link UtenteRepository} e {@link ClienteDettagliRepository} per l'accesso ai dati.
 *
 * @version 1.0
 */
@Service
public class GestioneUtentiServiceImpl implements GestioneUtentiService {
    private UtenteRepository utenteRepository;
    private ClienteDettagliRepository clienteDettagliRepository;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param utenteRepository         il repository per l'entità {@link Utente}
     * @param clienteDettagliRepository il repository per l'entità {@link ClienteDettagli}
     */
    @Autowired
    public GestioneUtentiServiceImpl(UtenteRepository utenteRepository, ClienteDettagliRepository clienteDettagliRepository) {
        this.utenteRepository = utenteRepository;
        this.clienteDettagliRepository = clienteDettagliRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Utente autentica(String email, String password) {
        if(email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()){
            throw new IllegalArgumentException("I campi email e password non possono essere vuoti!");
        }
        Utente utente = this.getUtente(email);
        if (utente == null) {
            throw new IllegalArgumentException("L'email inserita non è associata ad alcun account!");
        }
        if(!utente.getPassword().equals(toHash(password))){
            throw new IllegalArgumentException("Credenziali errate!");
        } else {
            return utente;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Utente creaUtente(String nome, String cognome, LocalDate dataDiNascita, String nazionalita, String email, String password) {
        if(nome == null ||  cognome == null || dataDiNascita == null || nazionalita == null  || email == null ||  password == null){
            throw new IllegalArgumentException("I campi non possono essere vuoti!");
        }
        nome = nome.trim();
        cognome = cognome.trim();
        nazionalita = nazionalita.trim();
        email = email.trim();
        password = password.trim();

        if(nome.isEmpty() || cognome.isEmpty() || nazionalita.isEmpty() || email.isEmpty() || password.length() < 8 || dataDiNascita.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("I campi non possono essere vuoti!");
        }

        if(this.getUtente(email) != null){
            throw new IllegalArgumentException("Esiste già un account associato a questa email!");
        }
        Utente utente = new Utente(email, toHash(password), nome, cognome, dataDiNascita, nazionalita);
        ClienteDettagli clienteDettagli = new ClienteDettagli(utente);
        utente.setClienteDettagli(clienteDettagli);
        utenteRepository.save(utente);
        return utente;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ControllaACL
    public boolean elimina(Utente account) {
        if(account == null || this.getUtente(account.getEmail()) != account){
            throw new IllegalArgumentException("Account non trovato!");
        }
        utenteRepository.delete(account);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Utente getUtente(String email) {
        if(email == null || email.trim().isEmpty()){
            throw new IllegalArgumentException("Il campo email non può essere vuoto!");
        }
        Utente utente = utenteRepository.findById(email).orElse(null);
        return utente;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ControllaACL
    public Collection<Utente> getUtenti() {
        return utenteRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ControllaACL
    public Collection<Utente> getClienti() {
        return utenteRepository.findAllByRuolo(Utente.Ruolo.CLIENTE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ControllaACL
    public void setRuolo(Utente account, String ruolo) {
        if(account == null || ruolo == null || ruolo.trim().isEmpty()){
            throw new IllegalArgumentException("I campi non possono essere vuoti!");
        }
        Utente.Ruolo ruoloUtente;
        try {
            ruoloUtente = Utente.Ruolo.valueOf(ruolo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ruolo non valido!");
        }
        if(ruoloUtente == account.getRuolo()){
            return;
        }
        if(ruoloUtente != Utente.Ruolo.CLIENTE) {
            account.setClienteDettagli(null);
        } else {
            if(clienteDettagliRepository.findById(account.getEmail()).isEmpty()){
                account.setClienteDettagli(new ClienteDettagli(account));
            } else {
                account.setClienteDettagli(clienteDettagliRepository.findById(account.getEmail()).get());
            }
        }

        account.setRuolo(ruoloUtente);
        utenteRepository.save(account);
    }

    /**
     * Genera l'hash della password utilizzando l'algoritmo SHA-512.
     *
     * @param password la password in chiaro da hashare
     * @return la stringa hashata della password, oppure {@code null} se si verifica un errore
     */
    private static String toHash(String password) {
        String hashString = null;

        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            hashString = sb.toString();

        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return hashString;
    }
}
