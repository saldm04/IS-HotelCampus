package it.unisa.hotelcampus.gestioneutenti.service;

import it.unisa.hotelcampus.model.entity.Utente;

import java.sql.Date;
import java.util.Collection;

/**
 * Servizio per la gestione degli utenti nel sistema HotelCampus.
 * Fornisce metodi per autenticare, creare, eliminare e gestire i ruoli degli utenti.
 *
 * @version 1.0
 */
public interface GestioneUtentiService {

    /**
     * Autentica un utente utilizzando l'email e la password.
     *
     * @param email    l'email dell'utente
     * @param password la password dell'utente
     * @return l'oggetto {@link Utente} autenticato
     * @throws IllegalArgumentException se uno dei parametri è invalido
     */
    Utente autentica(String email, String password);

    /**
     * Crea un nuovo utente nel sistema.
     *
     * @param nome          il nome dell'utente
     * @param cognome       il cognome dell'utente
     * @param dataDiNascita la data di nascita dell'utente
     * @param nazionalita   la nazionalità dell'utente
     * @param email         l'email dell'utente
     * @param password      la password dell'utente
     * @return l'oggetto {@link Utente} creato
     * @throws IllegalArgumentException se uno dei parametri è invalido
     */
    Utente creaUtente(String nome, String cognome, Date dataDiNascita, String nazionalita, String email, String password);

    /**
     * Elimina un account utente dal sistema.
     *
     * @param account l'account {@link Utente} da eliminare
     * @return {@code true} se l'eliminazione ha avuto successo
     * @throws IllegalArgumentException se uno dei parametri è invalido
     */
    boolean elimina(Utente account);

    /**
     * Recupera un utente specifico dal sistema tramite la sua email.
     *
     * @param email l'email dell'utente da recuperare
     * @return l'oggetto {@link Utente} se trovato, {@code null} altrimenti
     * @throws IllegalArgumentException se uno dei parametri è invalido
     */
    Utente getUtente(String email);

    /**
     * Recupera tutti gli utenti presenti nel sistema.
     *
     * @return una collezione di tutti gli utenti
     */
    Collection<Utente> getUtenti();

    /**
     * Recupera tutti gli utenti con ruolo di cliente.
     *
     * @return una collezione di utenti con ruolo {@link Utente.Ruolo#CLIENTE}
     */
    Collection<Utente> getClienti();

    /**
     * Imposta il ruolo di un utente specificato.
     *
     * @param account l'account {@link Utente} a cui assegnare il ruolo
     * @param ruolo   il nuovo ruolo da assegnare all'utente
     * @throws IllegalArgumentException se uno dei parametri è invalido
     */
    void setRuolo(Utente account, String ruolo);
}
