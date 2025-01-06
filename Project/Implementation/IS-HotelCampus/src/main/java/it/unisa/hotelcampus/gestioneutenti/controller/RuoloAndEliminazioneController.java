package it.unisa.hotelcampus.gestioneutenti.controller;

import it.unisa.hotelcampus.gestioneutenti.service.GestioneUtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
