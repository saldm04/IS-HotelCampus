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

    @Autowired
    public RuoloAndEliminazioneController(GestioneUtentiService gestioneUtentiService) {
        this.gestioneUtentiService = gestioneUtentiService;
    }

    //elimina

    //setRuolo

    //getUtenti

    //getUtente

    //getClienti
}
