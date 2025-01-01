package it.unisa.hotelcampus.gestioneprenotazioni.service;

import it.unisa.hotelcampus.model.entity.Camera;
import it.unisa.hotelcampus.model.entity.ClienteDettagli;
import it.unisa.hotelcampus.model.entity.Prenotazione;
import it.unisa.hotelcampus.model.entity.ServizioPrenotato;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

public interface GestionePrenotazioniService {
    Collection<Prenotazione> getPrenotazioni();
    Prenotazione creaPrenotazione(Date dataCheckIn, Date dataCheckOut, int numeroOspiti, Camera camera, Set<ServizioPrenotato> servizi, ClienteDettagli cliente);
    boolean eliminaPrenotazione(Prenotazione prenotazione);
    Collection<Prenotazione> cercaPrenotazioni(String email, Date checkIn, Date checkOut);

}
