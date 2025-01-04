package it.unisa.hotelcampus.gestioneservizi.service;

import it.unisa.hotelcampus.model.entity.Servizio;

import java.util.Collection;

/**
 * Servizio per la gestione dei servizi offerti dall'hotel.
 * Fornisce metodi per recuperare e gestire i servizi disponibili.
 *
 * @version 1.0
 */
public interface GestioneServiziService {

    /**
     * Recupera tutti i servizi che non sono stati eliminati.
     *
     * @return una collezione di servizi non eliminati
     */
    public Collection<Servizio> getServizi();
    public Servizio creaServizio(String nome, String descrizione, int costo, String immagine);
    public boolean rimuoviServizio(Servizio servizio);
}
