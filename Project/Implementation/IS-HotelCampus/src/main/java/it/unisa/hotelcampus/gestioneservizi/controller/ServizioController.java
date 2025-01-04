package it.unisa.hotelcampus.gestioneservizi.controller;

import it.unisa.hotelcampus.gestioneservizi.service.GestioneServiziService;
import it.unisa.hotelcampus.model.entity.Servizio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Controller per la gestione delle operazioni relative ai servizi offerti dall'hotel.
 * Gestisce le richieste HTTP per visualizzare i servizi e fornire dati in formato JSON.
 *
 * @version 1.0
 */
@Controller
@RequestMapping("/servizi")
public class ServizioController {

    /**
     * Servizio per la gestione dei servizi offerti dall'hotel.
     */
    private GestioneServiziService gestioneServiziService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param gestioneServiziService il servizio per la gestione dei servizi
     */
    @Autowired
    public ServizioController(final GestioneServiziService gestioneServiziService) {
        this.gestioneServiziService = gestioneServiziService;
    }

    /**
     * Gestisce la richiesta GET per visualizzare la lista dei servizi.
     * Recupera tutti i servizi disponibili e li aggiunge al modello per la visualizzazione nella vista "servizi".
     *
     * @param model il modello per aggiungere attributi alla vista
     * @return il nome della vista "servizi" che mostra la lista dei servizi
     */
    @GetMapping
    public String getServizi(Model model) {
        model.addAttribute("servizi", gestioneServiziService.getServizi());
        return "servizi";
    }

    /**
     * Gestisce la richiesta POST per ottenere la lista dei servizi in formato JSON.
     * Recupera tutti i servizi disponibili e li restituisce come collezione JSON.
     *
     * @return una collezione di oggetti {@link Servizio} in formato JSON
     */
    @PostMapping
    @ResponseBody
    public Collection<Servizio> getServiziJson() {
        return gestioneServiziService.getServizi();
    }
}
