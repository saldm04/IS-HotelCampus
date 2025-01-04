package it.unisa.hotelcampus.gestionecamere.service;

import it.unisa.hotelcampus.model.dao.CameraRepository;
import it.unisa.hotelcampus.model.dao.PrenotazioneRepository;
import it.unisa.hotelcampus.model.entity.Camera;
import it.unisa.hotelcampus.utils.acl.ControllaACL;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

/**
 * Implementazione del servizio di gestione delle camere.
 * Fornisce metodi per inserire, rimuovere e verificare la disponibilità delle camere.
 */
@Service
public class GestioneCamereServiceImpl implements GestioneCamereService {

    private final CameraRepository cameraRepository;
    private final PrenotazioneRepository prenotazioneRepository;

    /**
     * Costruttore della classe GestioneCamereServiceImpl.
     *
     * @param cameraRepository il repository per la gestione delle camere
     * @param prenotazioneRepository il repository per la gestione delle prenotazioni
     */
    public GestioneCamereServiceImpl(CameraRepository cameraRepository, PrenotazioneRepository prenotazioneRepository) {
        this.cameraRepository = cameraRepository;
        this.prenotazioneRepository = prenotazioneRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Camera> getCamere() {
        return cameraRepository.findCameraByisDeletedIsFalse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ControllaACL
    public Camera creaCamera(int numero, Camera.TipoCamera tipo, int numeroMaxOspiti, int quadratura, int costo, String immagine) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ControllaACL
    public boolean rimuoviCamera(Camera camera) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Camera> getCamereDisponibili(Date checkIn, Date checkOut, int numeroOspiti) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Le date di check-in e check-out non possono essere nulle");
        }

        if (checkIn.after(checkOut)) {
            throw new IllegalArgumentException("La data di check-in non può essere successiva alla data di check-out");
        }

        if (numeroOspiti <= 0) {
            throw new IllegalArgumentException("Il numero di ospiti deve essere maggiore di 0");
        }

        return cameraRepository.findCamereDisponibili(checkIn, checkOut, numeroOspiti);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ControllaACL
    public boolean verificaDisponibilita(Camera camera, Date checkIn, Date checkOut) {
        return !prenotazioneRepository.existsPrenotazione(
                camera.getId(),
                checkIn,
                checkOut
        );
    }
}
