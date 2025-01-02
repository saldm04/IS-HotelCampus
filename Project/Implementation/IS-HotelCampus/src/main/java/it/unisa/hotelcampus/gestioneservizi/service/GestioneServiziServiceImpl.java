package it.unisa.hotelcampus.gestioneservizi.service;

import it.unisa.hotelcampus.model.dao.ServizioRepository;
import it.unisa.hotelcampus.model.entity.Servizio;
import it.unisa.hotelcampus.utils.acl.ControllaACL;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class GestioneServiziServiceImpl implements GestioneServiziService{

    ServizioRepository servizioRepository;

    public GestioneServiziServiceImpl(final ServizioRepository  servizioRepository) {
        this.servizioRepository = servizioRepository;
    }

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
