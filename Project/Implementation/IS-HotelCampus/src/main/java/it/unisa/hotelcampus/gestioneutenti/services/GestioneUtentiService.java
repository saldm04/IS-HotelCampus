package it.unisa.hotelcampus.gestioneutenti.services;

import it.unisa.hotelcampus.model.entity.Utente;

import java.sql.Date;
import java.util.Collection;

public interface GestioneUtentiService {
    Utente autentica(String email, String password);
    Utente creaUtente(String nome, String cognome, Date dataDiNascita, String nazionalita, String email, String password);
    boolean elimina(Utente account);
    Utente getUtente(String email);
    Collection<Utente> getUtenti();
    Collection<Utente> getClienti();
    void setRuolo(Utente account, String ruolo);
}
