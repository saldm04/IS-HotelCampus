package it.unisa.hotelcampus.gestionecamere.service;

import it.unisa.hotelcampus.model.entity.Camera;

import java.util.Collection;
import java.util.Date;

public interface GestioneCamereService {

    Collection<Camera> getCamere();
    Camera creaCamera(int numero, Camera.TipoCamera tipo, int numeroMaxOspiti, int quadratura, int costo, String immagine);
    boolean rimuoviCamera(Camera camera);
    Collection<Camera> getCamereDisponibili(Date checkIn, Date checkOut, int numeroOspiti);
    boolean verificaDisponibilita(Camera camera, Date checkIn, Date checkOut);

}
