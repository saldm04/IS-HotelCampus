package it.unisa.hotelcampus.gestioneprenotazioni.service;


import it.unisa.hotelcampus.gestionecamere.service.GestioneCamereServiceImpl;
import it.unisa.hotelcampus.model.dao.CameraRepository;
import it.unisa.hotelcampus.model.dao.ClienteDettagliRepository;
import it.unisa.hotelcampus.model.dao.PrenotazioneRepository;
import it.unisa.hotelcampus.model.entity.Camera;
import it.unisa.hotelcampus.model.entity.ClienteDettagli;
import it.unisa.hotelcampus.model.entity.Prenotazione;
import it.unisa.hotelcampus.model.entity.ServizioPrenotato;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class GestionePrenotazioniServImp implements GestionePrenotazioniService {


  private PrenotazioneRepository prenotazioneRepository;
  private GestioneCamereServiceImpl gestioneCamereService;
  private ClienteDettagliRepository clienteDettagliRepository;
  private CameraRepository cameraRepository;

  public GestionePrenotazioniServImp(final GestioneCamereServiceImpl gestioneCamereService,
                                     final PrenotazioneRepository prenotazioneRepository,
                                     final ClienteDettagliRepository clienteDettagliRepository,
                                     final CameraRepository cameraRepository
                                  ) {

    this.gestioneCamereService = gestioneCamereService;
    this.prenotazioneRepository = prenotazioneRepository;
    this.clienteDettagliRepository = clienteDettagliRepository;
    this.cameraRepository = cameraRepository;
  }

  @Override
  public Collection<Prenotazione> getPrenotazioni() {
    return List.of();
  }

  @Override
  public Prenotazione creaPrenotazione(Date dataCheckIn, Date dataCheckOut, int numeroOspiti, Camera camera, Set<ServizioPrenotato> servizi, ClienteDettagli cliente) {
    if (dataCheckIn == null || dataCheckIn.before(new Date())) {
      throw new IllegalArgumentException("La data di check-in non può essere nulla o precedente alla data attuale");
    }
    if (dataCheckOut == null || dataCheckOut.before(new Date())) {
      throw new IllegalArgumentException("La data di check-out non può essere nulla o precedente alla data attuale");
    }
    if (dataCheckOut.before(dataCheckIn)) {
      throw new IllegalArgumentException("La data di check-out non può essere precedente alla data di check-in");
    }
    if (numeroOspiti <= 0) {
      throw new IllegalArgumentException("Il numero di ospiti deve essere maggiore di 0");
    }
    if (camera == null) {
      throw new IllegalArgumentException("La camera non è selezionata");
    }
    if (servizi == null) {
      throw new IllegalArgumentException("I servizi non possono essere nulli");
    }
    if (cliente == null) {
      throw new IllegalArgumentException("Il cliente deve essere autenticato");
    }
    if (numeroOspiti > camera.getNumeroMaxOspiti()) {
      throw new IllegalArgumentException(
              "Il numero di ospiti non può essere maggiore "
                      + "del numero massimo di ospiti della camera"
      );
    }
    synchronized (this) {
      if (!gestioneCamereService.verificaDisponibilita(camera, dataCheckIn, dataCheckOut)) {
        throw new IllegalArgumentException("La camera non è più disponibile per le date selezionate"
        );
      }
      for (ServizioPrenotato servizio : servizi) {
        if (servizio.getNumeroServizi() > numeroOspiti) {
          throw new IllegalArgumentException(
                  "Il numero di servizi non può essere maggiore del numero di ospiti"
          );
        }
      }

      Prenotazione prenotazione = new Prenotazione(
              new Date(), dataCheckIn, dataCheckOut,
              numeroOspiti, camera, servizi, cliente
      );

      prenotazioneRepository.save(prenotazione);

      cliente.creaPrenotazione(prenotazione);

      clienteDettagliRepository.save(cliente);

      camera.addPrenotazione(prenotazione);

      cameraRepository.save(camera);

      return prenotazione;
    }
  }

  @Override
  public boolean eliminaPrenotazione(Prenotazione prenotazione) {
    return false;
  }

  @Override
  public Collection<Prenotazione> cercaPrenotazioni(
          final String email, final Date checkIn, final Date checkOut
  ) {
    return List.of();
  }
}
