package it.unisa.hotelcampus.gestioneservizi.service;

import it.unisa.hotelcampus.model.entity.Servizio;

import java.util.Collection;

public interface GestioneServiziService {
    public Collection<Servizio> getServizi();
    public Servizio creaServizio(String nome, String descrizione, int costo, String immagine);
    public boolean rimuoviServizio(Servizio servizio);
}
