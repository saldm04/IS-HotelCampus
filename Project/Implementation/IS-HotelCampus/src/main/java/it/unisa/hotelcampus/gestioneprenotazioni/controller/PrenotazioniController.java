package it.unisa.hotelcampus.gestioneprenotazioni.controller;

import it.unisa.hotelcampus.gestioneprenotazioni.service.GestionePrenotazioniService;
import it.unisa.hotelcampus.gestioneservizi.service.GestioneServiziService;
import it.unisa.hotelcampus.model.dao.CameraRepository;
import it.unisa.hotelcampus.model.dao.ServizioPrenotatoRepository;
import it.unisa.hotelcampus.model.dao.ServizioRepository;
import it.unisa.hotelcampus.model.entity.*;
import it.unisa.hotelcampus.utils.acl.ControllaACL;
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
    private final CameraRepository cameraRepository;
    private final ServizioRepository servizioRepository;

    @Autowired
    public PrenotazioniController(GestionePrenotazioniService gestionePrenotazioniService, CameraRepository cameraRepository, ServizioRepository servizioRepository) {
        this.gestionePrenotazioniService = gestionePrenotazioniService;
        this.cameraRepository = cameraRepository;
        this.servizioRepository = servizioRepository;
    }

    @PostMapping("/riepilogo")
    public String riepilogo(
            Model model,
            @RequestParam("cameraId") Long id,
            @RequestParam("numeroOspiti") Integer numeroOspiti,
            @RequestParam Map<String, String> quantita, // Mappa delle quantità con chiavi come "quantita[servizioId]"
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut
    ) {
        // Recupera la Camera dal database
        Camera camera = cameraRepository.findById(id).orElse(null);

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
                    return "error";
                }
            }
        }

        double totaleCamera = camera.getCosto();
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
                    return "error";
                }
            }
        }

        try {
            Prenotazione prenotazione = gestionePrenotazioniService.creaPrenotazione(dataCheckIn, dataCheckOut, numeroOspiti, camera, serviziPrenotati, utente.getClienteDettagli());
            model.addAttribute("prenotazione", prenotazione);
        }catch (Exception e){
            model.addAttribute("generalError", e.getMessage());
            return "error";
        }
        System.out.println("\n\n\n sono qui2 \n\n\n");
        return "confermaPrenotazione";
    }
}
