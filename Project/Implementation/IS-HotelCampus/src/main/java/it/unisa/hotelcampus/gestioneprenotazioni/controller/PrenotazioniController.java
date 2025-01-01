package it.unisa.hotelcampus.gestioneprenotazioni.controller;

import it.unisa.hotelcampus.gestioneprenotazioni.service.GestionePrenotazioniService;
import it.unisa.hotelcampus.gestioneservizi.service.GestioneServiziService;
import it.unisa.hotelcampus.model.dao.CameraRepository;
import it.unisa.hotelcampus.model.dao.ServizioPrenotatoRepository;
import it.unisa.hotelcampus.model.dao.ServizioRepository;
import it.unisa.hotelcampus.model.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.sql.Date;
import java.util.*;

@Controller
@RequestMapping("/prenotazioni")
public class PrenotazioniController {
    private final GestionePrenotazioniService gestionePrenotazioniService;
    private final GestioneServiziService gestioneServiziService;
    private final CameraRepository cameraRepository;
    private final ServizioRepository servizioRepository;
    private final ServizioPrenotatoRepository servizioPrenotatoRepository;

    @Autowired
    public PrenotazioniController(GestionePrenotazioniService gestionePrenotazioniService, GestioneServiziService gestioneServiziService, CameraRepository cameraRepository, ServizioRepository servizioRepository, ServizioPrenotatoRepository servizioPrenotatoRepository) {
        this.gestionePrenotazioniService = gestionePrenotazioniService;
        this.gestioneServiziService = gestioneServiziService;
        this.cameraRepository = cameraRepository;
        this.servizioRepository = servizioRepository;
        this.servizioPrenotatoRepository = servizioPrenotatoRepository;
    }

    @PostMapping("/selezionaServizi")
    public String selezionaServizi(
            Model model,
            @RequestParam("numeroCamera") String numeroCamera,
            @RequestParam("importoCamera") String importoCamera,
            @RequestParam("numeroOspiti") String numeroOspiti,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut
    ) {
        model.addAttribute("servizi", gestioneServiziService.getServizi());
        model.addAttribute("importoCamera", importoCamera);
        model.addAttribute("numeroCamera", numeroCamera);
        model.addAttribute("numeroOspiti", numeroOspiti);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        return "selezionaServizi";
    }

    @PostMapping("/riepilogo")
    public String riepilogo(
            Model model,
            @RequestParam("numeroCamera") Long numeroCamera,
            @RequestParam("importoCamera") Double importoCamera,
            @RequestParam("numeroOspiti") Integer numeroOspiti,
            @RequestParam Map<String, String> quantita, // Mappa delle quantità con chiavi come "quantita[servizioId]"
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut
    ) {
        // Recupera la Camera dal database
        Camera camera = cameraRepository.findById(numeroCamera).orElse(null);

        // Processa le quantità dei servizi
        Map<Servizio, Integer> serviziSelezionati = new HashMap<>();
        double totaleServizi = 0.0;

        for (Map.Entry<String, String> entry : quantita.entrySet()) {
            // Estrarre l'ID del servizio dalla chiave, che è nel formato "quantita[servizioId]"
            String key = entry.getKey();
            if (key.startsWith("quantita[") && key.endsWith("]")) {
                String idStr = key.substring(9, key.length() - 1); // Estrae 'servizioId'
                try {
                    Long servizioId = Long.parseLong(idStr);
                    int quantitaServizio = Integer.parseInt(entry.getValue());

                    if (quantitaServizio > 0) {
                        Optional<Servizio> servizioOpt = servizioRepository.findById(servizioId);
                        if (servizioOpt.isPresent()) {
                            Servizio servizio = servizioOpt.get();
                            serviziSelezionati.put(servizio, quantitaServizio);
                            totaleServizi += servizio.getCosto() * quantitaServizio;
                        }
                    }
                } catch (NumberFormatException e) {
                    return "redirect:/error";
                }
            }
        }

        double totaleCamera = importoCamera;
        double totale = totaleCamera + totaleServizi;

        // Aggiungi i dati al modello
        model.addAttribute("camera", camera);
        model.addAttribute("serviziSelezionati", serviziSelezionati);
        model.addAttribute("totaleCamera", totaleCamera);
        model.addAttribute("totaleServizi", totaleServizi);
        model.addAttribute("totale", totale);
        model.addAttribute("numeroOspiti", numeroOspiti);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);

        return "riepilogo";
    }

    @PostMapping("/confermaPrenotazione")
    public String confermaPrenotazione(
            @RequestParam("numeroCamera") Long numeroCamera,
            @RequestParam("numeroOspiti") Integer numeroOspiti,
            @RequestParam Map<String, String> quantita, // Mappa delle quantità con chiavi come "quantita[servizioId]"
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut,
            @SessionAttribute("utente") Utente utente,
            Model model
    ){
        Camera camera = cameraRepository.findById(numeroCamera).orElse(null);
        Date dataCheckIn = Date.valueOf(checkIn);
        Date dataCheckOut = Date.valueOf(checkOut);

        Set<ServizioPrenotato> serviziPrenotati = new HashSet<>();

        // Processa le quantità dei servizi
        for (Map.Entry<String, String> entry : quantita.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("quantita[")) {
                String idStr = key.substring(9, key.length() - 1);
                try {
                    Long servizioId = Long.parseLong(idStr);
                    int quantitaServizio = Integer.parseInt(entry.getValue());

                    if (quantitaServizio > 0) {
                        Optional<Servizio> servizioOpt = servizioRepository.findById(servizioId);
                        if (servizioOpt.isPresent()) {
                            Servizio servizio = servizioOpt.get();

                            ServizioPrenotato servizioPrenotato = new ServizioPrenotato();
                            servizioPrenotato.setServizio(servizio);
                            servizioPrenotato.setNumeroServizi(quantitaServizio);
                            servizioPrenotato.setCostoUnitario(servizio.getCosto());

                            serviziPrenotati.add(servizioPrenotato);
                        }
                    }
                } catch (NumberFormatException e) {
                    return "redirect:/error";
                }
            }
        }

        try {
            Prenotazione prenotazione = gestionePrenotazioniService.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, serviziPrenotati, utente.getClienteDettagli());
            model.addAttribute("prenotazione", prenotazione);
        }catch (IllegalArgumentException e){
            model.addAttribute("errore", e.getMessage());
            return "redirect:/error";
        }

        return "confermaPrenotazione";
    }
}
