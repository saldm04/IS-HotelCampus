package it.unisa.hotelcampus.gestioneservizi.service;

import it.unisa.hotelcampus.model.dao.ServizioRepository;
import it.unisa.hotelcampus.model.entity.Servizio;
import it.unisa.hotelcampus.utils.acl.ControllaACL;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Implementazione del servizio {@link GestioneServiziService}.
 * Gestisce le operazioni relative ai servizi offerti dall'hotel, come il recupero dei servizi disponibili.
 * Utilizza il repository {@link ServizioRepository} per l'accesso ai dati.
 *
 * @version 1.0
 */
@Service
public class GestioneServiziServiceImpl implements GestioneServiziService{

    private final ServizioRepository servizioRepository;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param servizioRepository il repository per l'entità {@link Servizio}
     */
    public GestioneServiziServiceImpl(final ServizioRepository servizioRepository) {
        this.servizioRepository = servizioRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Servizio> getServizi() {
        return servizioRepository.findAllByisDeletedIsFalse();
    }

    @Override
    @ControllaACL
    public Servizio creaServizio(String nome, String descrizione, int costo, String immagine) {
        return null;
    }

    @Override
    @ControllaACL
    public boolean rimuoviServizio(Servizio servizio) {
        return false;
    }
}
