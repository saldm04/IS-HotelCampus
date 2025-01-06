package it.unisa.hotelcampus.gestioneprenotazioni.service;


import it.unisa.hotelcampus.gestionecamere.service.GestioneCamereServiceImpl;
import it.unisa.hotelcampus.model.dao.ClienteDettagliRepository;
import it.unisa.hotelcampus.model.dao.PrenotazioneRepository;
import it.unisa.hotelcampus.model.entity.Camera;
import it.unisa.hotelcampus.model.entity.ClienteDettagli;
import it.unisa.hotelcampus.model.entity.Prenotazione;
import it.unisa.hotelcampus.model.entity.ServizioPrenotato;
import it.unisa.hotelcampus.utils.acl.ControllaACL;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementazione del servizio {@link GestionePrenotazioniService}.
 * Fornisce metodi per creare, eliminare, recuperare e cercare prenotazioni.
 * Utilizza i repository {@link PrenotazioneRepository} e {@link ClienteDettagliRepository} per l'accesso ai dati.
 *
 * @version 1.0
 */
@Service
public class GestionePrenotazioniServImp implements GestionePrenotazioniService {


  private PrenotazioneRepository prenotazioneRepository;
  private GestioneCamereServiceImpl gestioneCamereService;
  private ClienteDettagliRepository clienteDettagliRepository;


  /**
   * Costruttore della classe GestionePrenotazioniServiceImpl.
   *
   * @param gestioneCamereService      il servizio di gestione delle camere
   * @param prenotazioneRepository     il repository per l'entità {@link Prenotazione}
   * @param clienteDettagliRepository  il repository per l'entità {@link ClienteDettagli}
   */
  public GestionePrenotazioniServImp(final GestioneCamereServiceImpl gestioneCamereService,
                                     final PrenotazioneRepository prenotazioneRepository,
                                     final ClienteDettagliRepository clienteDettagliRepository
                                  ) {

    this.gestioneCamereService = gestioneCamereService;
    this.prenotazioneRepository = prenotazioneRepository;
    this.clienteDettagliRepository = clienteDettagliRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @ControllaACL
  public Collection<Prenotazione> getPrenotazioni() {
    return List.of();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @ControllaACL
  public Prenotazione creaPrenotazione(Date dataCheckIn, Date dataCheckOut,
                                       int numeroOspiti, Camera camera,
                                       Set<ServizioPrenotato> servizi, ClienteDettagli cliente
  ) {
    if (dataCheckIn == null || dataCheckOut == null) {
      throw new IllegalArgumentException("Le date di check-in e check-out non possono essere nulle");
    }

    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);

    if (dataCheckIn.before(today.getTime())) {
      throw new IllegalArgumentException("La data di check-in non può essere precedente alla data odierna");
    }

    if (dataCheckOut.before(today.getTime())) {
      throw new IllegalArgumentException("La data di check-out non può essere precedente alla data odierna");
    }
    if (dataCheckOut.before(dataCheckIn)) {
      throw new IllegalArgumentException("La data di check-out non può essere precedente alla data di check-in");
    }
    if(dataCheckOut.equals(dataCheckIn)) {
      throw new IllegalArgumentException("La data di check-out non può essere uguale alla data di check-in");
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

      for (ServizioPrenotato servizio : servizi) {
        servizio.setPrenotazione(prenotazione);
      }

      prenotazioneRepository.save(prenotazione);

      cliente.creaPrenotazione(prenotazione);

      clienteDettagliRepository.save(cliente);

      return prenotazione;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @ControllaACL
  public boolean eliminaPrenotazione(Prenotazione prenotazione) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @ControllaACL
  public Collection<Prenotazione> cercaPrenotazioni(
          final String email, final Date checkIn, final Date checkOut
  ) {
    return List.of();
  }
}
