package it.unisa.hotelcampus.gestionecamere.controller;

import it.unisa.hotelcampus.gestionecamere.service.GestioneCamereService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;

import it.unisa.hotelcampus.model.entity.Camera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Il controller CameraController gestisce le richieste relative
 * alle camere di un sistema di prenotazione.
 * Consente di visualizzare tutte le camere disponibili e
 * filtrare le camere per disponibilità in base a criteri
 * specifici.
 */
@Controller
@RequestMapping("/camere")
public class CameraController {

    private final GestioneCamereService gestioneCamereService;

    /**
     * Costruisce un'istanza di CameraController con il servizio
     * GestioneCamereService iniettato.
     *
     * @param gestioneCamereService il servizio di gestione delle camere
     */
    @Autowired
    public CameraController(GestioneCamereService gestioneCamereService) {
        this.gestioneCamereService = gestioneCamereService;
    }

    /**
     * Gestisce la richiesta GET per visualizzare tutte le camere
     * registrate nel sistema.
     *
     * @param model il modello da utilizzare per aggiungere attributi
     * @return il nome della vista "prenotaOra"
     */
    @GetMapping("/prenota")
    public String mostraCamere(Model model) {
        Collection<Camera> camere = gestioneCamereService.getCamere();
        int maxOspiti = camere.stream()
                .mapToInt(Camera::getNumeroMaxOspiti)
                .max()
                .orElse(1);
        model.addAttribute("camere", camere);
        model.addAttribute("maxOspiti", maxOspiti);
        return "prenotaOra";
    }

    /**
     * Gestisce la richiesta POST per filtrare le camere disponibili
     * per la prenotazione, in base a check-in, check-out e numero di ospiti.
     *
     * @param checkInDate      la data di check-in
     * @param checkOutDate     la data di check-out
     * @param numeroOspiti il numero di ospiti
     * @param model        il modello da utilizzare per aggiungere attributi
     * @return il nome della vista "prenotaOra"
     */
    @PostMapping("/disponibiliPerPrenotazione")
    public String mostraCamereDisponibili(
            @RequestParam(value = "checkindate", required = false)
            String checkInDate,
            @RequestParam(value = "checkoutdate", required = false)
            String checkOutDate,
            @RequestParam(value = "numOspiti", required = false)
            Integer numeroOspiti,
            Model model) {

        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate checkOut = LocalDate.parse(checkOutDate);

        try {


            model.addAttribute("camere", gestioneCamereService
                    .getCamereDisponibili(checkIn, checkOut, numeroOspiti));
            model.addAttribute("checkIn",
                    checkIn != null ? checkIn : "");
            model.addAttribute("checkOut",
                    checkOut != null ? checkOut : "");
            model.addAttribute("numeroOspiti", numeroOspiti);
            model.addAttribute("ricercaEffettuata", true);

            Collection<Camera> camere = gestioneCamereService.getCamere();
            int maxOspiti = camere.stream()
                    .mapToInt(Camera::getNumeroMaxOspiti)
                    .max()
                    .orElse(1);
            model.addAttribute("maxOspiti", maxOspiti);

        } catch (IllegalArgumentException e) {
            model.addAttribute("generalError", e.getMessage());
            mostraCamere(model);
        }

        return "prenotaOra";
    }
}
