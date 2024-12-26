package it.unisa.hotelcampus.gestioneutenti.controller;

import it.unisa.hotelcampus.gestioneutenti.services.GestioneUtentiService;
import it.unisa.hotelcampus.model.entity.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/utenti")
public class RuoloAndEliminazioneController {
    private GestioneUtentiService gestioneUtentiService;
    private static final Map<String, Set<Utente.Ruolo>> ACL_MAP = new HashMap<>();

    @Autowired
    public RuoloAndEliminazioneController(GestioneUtentiService gestioneUtentiService) {
        this.gestioneUtentiService = gestioneUtentiService;
    }

    static {
        ACL_MAP.put("elimina", Set.of(Utente.Ruolo.DIRETTORE));
        ACL_MAP.put("setRuolo", Set.of(Utente.Ruolo.DIRETTORE));
        ACL_MAP.put("getUtenti", Set.of(Utente.Ruolo.DIRETTORE));
        ACL_MAP.put("getUtente", Set.of(Utente.Ruolo.DIRETTORE));
        ACL_MAP.put("getClienti", Set.of(Utente.Ruolo.GESTOREPRENOTAZIONI));
    }

    //elimina

    //setRuolo

    //getUtenti

    //getUtente

    //getClienti
}
