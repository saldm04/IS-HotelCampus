package it.unisa.hotelcampus.gestionecamere.service;

import it.unisa.hotelcampus.model.dao.CameraRepository;
import it.unisa.hotelcampus.model.entity.Camera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class GestioneCamereServiceImpl implements GestioneCamereService {

    @Autowired
    private CameraRepository cameraRepository;

    @Override
    public Collection<Camera> getCamere() {
        return cameraRepository.findCameraByisDeletedIsFalse();
    }

    @Override
    public Camera creaCamera(int numero, Camera.TipoCamera tipo, int numeroMaxOspiti, int quadratura, int costo, String immagine) {
        return null;
    }

    @Override
    public boolean rimuoviCamera(Camera camera) {
        return false;
    }

    @Override
    public Collection<Camera> getCamereDisponibili(Date checkIn, Date checkOut, int numeroOspiti) {
        return List.of();
    }

    @Override
    public boolean verificaDisponibilita(Camera camera, Date checkIn, Date checkOut) {
        return false;
    }
}
