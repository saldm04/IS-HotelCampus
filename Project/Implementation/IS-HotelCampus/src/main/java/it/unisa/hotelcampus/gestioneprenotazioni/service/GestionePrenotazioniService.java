package it.unisa.hotelcampus.gestioneprenotazioni.service;

import it.unisa.hotelcampus.model.entity.Camera;
import it.unisa.hotelcampus.model.entity.ClienteDettagli;
import it.unisa.hotelcampus.model.entity.Prenotazione;
import it.unisa.hotelcampus.model.entity.ServizioPrenotato;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * L'interfaccia GestionePrenotazioniService fornisce metodi per la
 * gestione delle prenotazioni nel sistema di prenotazione.
 * Include funzionalità per creare, eliminare e cercare prenotazioni.
 */
public interface GestionePrenotazioniService {

    /**
     * Restituisce tutte le prenotazioni effettuate e
     * registrate nel sistema.
     *
     * @return una collezione di oggetti Prenotazione
     */
    Collection<Prenotazione> getPrenotazioni();

    /**
     * Crea una nuova prenotazione con i dettagli specificati.
     *
     * @param dataCheckIn      la data di check-in desiderata
     * @param dataCheckOut     la data di check-out desiderata
     * @param numeroOspiti     il numero di ospiti
     * @param camera           la camera da prenotare
     * @param servizi          l'insieme dei servizi prenotati
     * @param cliente          l'istanza del cliente che effettua la prenotazione
     * @return l'oggetto Prenotazione appena creata
     * @throws IllegalArgumentException se uno dei parametri è invalido
     */
    Prenotazione creaPrenotazione(Date dataCheckIn, Date dataCheckOut,
                                  int numeroOspiti, Camera camera,
                                  Set<ServizioPrenotato> servizi, ClienteDettagli cliente
    );



    /**
     * Permette di eliminare una prenotazione che non è ancora stata consumata.
     *
     * @param prenotazione la prenotazione da eliminare
     * @return true se la prenotazione è stata eliminata con successo, false altrimenti
     */
    boolean eliminaPrenotazione(Prenotazione prenotazione);



    /**
     * Restituisce le prenotazioni presenti nel sistema che rispettano i parametri
     * specificati.
     *
     * @param email    l'email del cliente che ha effettuato la prenotazione
     * @param checkIn  la data di check-in desiderata
     * @param checkOut la data di check-out desiderata
     *
     * @return una collezione di oggetti Prenotazione che rispettano i parametri specificati
     *
     * @throws IllegalArgumentException se uno dei parametri è invalido
     */
    Collection<Prenotazione> cercaPrenotazioni(String email, Date checkIn, Date checkOut);

}
