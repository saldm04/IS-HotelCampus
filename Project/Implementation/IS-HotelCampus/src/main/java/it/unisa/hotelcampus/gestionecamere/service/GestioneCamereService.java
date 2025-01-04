package it.unisa.hotelcampus.gestionecamere.service;


import it.unisa.hotelcampus.model.entity.Camera;

import java.util.Collection;
import java.util.Date;

/**
 * L'interfaccia GestioneCamereService fornisce metodi per la
 * gestione delle camere nel sistema di prenotazione.
 * Include funzionalità per inserire, rimuovere e verificare
 * la disponibilità delle camere.
 */
public interface GestioneCamereService {

    /**
     * Restituisce tutte le camere registrate nel sistema
     * e non eliminate logicamente.
     *
     * @return una collezione di oggetti Camera non eliminate.
     */
    Collection<Camera> getCamere();

    /**
     * Crea una nuova camera con i parametri specificati.
     * Se il numero di camera fornito corrisponde a una camera
     * già attiva, questa viene eliminata logicamente e la nuova
     * camera viene creata come attiva.
     *
     * @param numero          il numero della camera
     * @param tipo            il tipo di camera
     * @param numeroMaxOspiti il numero massimo di ospiti consentiti
     * @param quadratura      la metratura della camera in metri quadrati
     * @param costo           il costo della camera
     * @param immagine        il percorso o identificativo dell'immagine
     *                        della camera
     * @return l'oggetto Camera appena creata
     * @throws IllegalArgumentException se uno dei parametri è invalido
     */
    Camera creaCamera(
            int numero, Camera.TipoCamera tipo, int numeroMaxOspiti,
            int quadratura, int costo, String immagine
    );

    /**
     * Cancella logicamente una camera attiva nel sistema.
     *
     * @param camera la camera da rimuovere logicamente
     * @return true se la rimozione è avvenuta con successo, false altrimenti
     * @throws IllegalArgumentException se la camera è null o non
     * esiste nel sistema
     */
    boolean rimuoviCamera(Camera camera);

    /**
     * Restituisce tutte le camere registrate e non eliminate logicamente
     * nel sistema, che sono disponibili nel periodo specificato e
     * che possono ospitare il numero di ospiti indicato.
     *
     * @param checkIn      la data di check-in desiderata
     * @param checkOut     la data di check-out desiderata
     * @param numeroOspiti il numero di ospiti che occuperanno la camera
     * @return una collezione di camere disponibili che soddisfano i
     * criteri specificati
     * @throws IllegalArgumentException se uno dei parametri è invalido
     */
    Collection<Camera> getCamereDisponibili(
            Date checkIn, Date checkOut, int numeroOspiti
    );

    /**
     * Verifica la disponibilità di una camera nel periodo specificato.
     *
     * @param camera   la camera da verificare
     * @param checkIn  la data di check-in desiderata
     * @param checkOut la data di check-out desiderata
     * @return true se la camera è disponibile nel periodo specificato,
     * false altrimenti
     * @throws IllegalArgumentException se uno dei parametri è invalido
     */
    boolean verificaDisponibilita(Camera camera, Date checkIn, Date checkOut);
}
