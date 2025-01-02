package it.unisa.hotelcampus.gestioneutenti.services;

import it.unisa.hotelcampus.model.dao.ClienteDettagliRepository;
import it.unisa.hotelcampus.model.dao.UtenteRepository;
import it.unisa.hotelcampus.model.entity.ClienteDettagli;
import it.unisa.hotelcampus.model.entity.Utente;
import it.unisa.hotelcampus.utils.acl.ControllaACL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.*;

@Service
public class GestioneUtentiServiceImpl implements GestioneUtentiService {
    private UtenteRepository utenteRepository;
    private ClienteDettagliRepository clienteDettagliRepository;

    @Autowired
    public GestioneUtentiServiceImpl(UtenteRepository utenteRepository, ClienteDettagliRepository clienteDettagliRepository) {
        this.utenteRepository = utenteRepository;
        this.clienteDettagliRepository = clienteDettagliRepository;
    }

    @Override
    public Utente autentica(String email, String password) {
        if(email==null || email.trim().isEmpty() || password==null || password.trim().isEmpty()){
            throw new IllegalArgumentException("I campi email e password non possono essere vuoti!");
        }
        Utente utente = this.getUtente(email);
        if (utente==null) {
            throw new IllegalArgumentException("L'email inserita non è associata ad alcun account!");
        }
        if(!utente.getPassword().equals(toHash(password))){
            throw new IllegalArgumentException("Credenziali errate!");
        }else {
            return utente;
        }
    }

    @Override
    public Utente creaUtente(String nome, String cognome, Date dataDiNascita, String nazionalita, String email, String password) {
        if(nome==null ||  cognome==null || dataDiNascita==null || nazionalita==null  || email==null ||  password==null){
            throw new IllegalArgumentException("I campi non possono essere vuoti!");
        }
        nome = nome.trim();
        cognome = cognome.trim();
        nazionalita = nazionalita.trim();
        email = email.trim();
        password = password.trim();

        if(nome.isEmpty() || cognome.isEmpty() || nazionalita.isEmpty() || email.isEmpty() || password.length()<8 || dataDiNascita.after(new Date(System.currentTimeMillis()))){
            throw new IllegalArgumentException("I campi non possono essere vuoti!");
        }

        if(this.getUtente(email)!=null){
            throw new IllegalArgumentException("Esiste già un account associato a questa email!");
        }
        Utente utente = new Utente(email, toHash(password), nome, cognome, dataDiNascita, nazionalita);
        ClienteDettagli clienteDettagli = new ClienteDettagli(utente);
        utente.setClienteDettagli(clienteDettagli);
        utenteRepository.save(utente);
        return utente;
    }

    @Override
    @ControllaACL
    public boolean elimina(Utente account) {
        if(account==null || this.getUtente(account.getEmail())!=account){
            throw new IllegalArgumentException("Account non trovato!");
        }
        utenteRepository.delete(account);
        return true;
    }

    @Override
    public Utente getUtente(String email) {
        if(email==null || email.trim().isEmpty()){
            throw new IllegalArgumentException("Il campo email non può essere vuoto!");
        }
        Utente utente = utenteRepository.findById(email).orElse(null);
        return utente;
    }

    @Override
    @ControllaACL
    public Collection<Utente> getUtenti() {
        return utenteRepository.findAll();
    }

    @Override
    @ControllaACL
    public Collection<Utente> getClienti() {
        return utenteRepository.findAllByRuolo(Utente.Ruolo.CLIENTE);
    }

    @Override
    @ControllaACL
    public void setRuolo(Utente account, String ruolo) {
        if(account==null || ruolo==null || ruolo.trim().isEmpty()){
            throw new IllegalArgumentException("I campi non possono essere vuoti!");
        }
        Utente.Ruolo ruoloUtente = Utente.Ruolo.valueOf(ruolo);
        if(ruoloUtente==null){
            throw new IllegalArgumentException("Ruolo non valido!");
        }
        if(ruoloUtente==account.getRuolo()){
            return;
        }
        if(ruoloUtente!=Utente.Ruolo.CLIENTE) {
            account.setClienteDettagli(null);
        }else{
            if(clienteDettagliRepository.findById(account.getEmail()).isEmpty()){
                account.setClienteDettagli(new ClienteDettagli(account));
            }else {
                account.setClienteDettagli(clienteDettagliRepository.findById(account.getEmail()).get());
            }
        }

        account.setRuolo(ruoloUtente);
        utenteRepository.save(account);
    }

    private static String toHash(String password) {
        String hashString = null;

        try {

            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            hashString = "";

            for (int i = 0; i < hash.length; i++) {
                hashString += Integer.toHexString((hash[i] & 0xFF) | 0x100).substring(1, 3);
            }

        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return hashString;
    }
}
