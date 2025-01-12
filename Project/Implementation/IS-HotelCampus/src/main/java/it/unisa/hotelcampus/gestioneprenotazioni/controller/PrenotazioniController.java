package it.unisa.hotelcampus.gestioneprenotazioni.controller;

import it.unisa.hotelcampus.gestioneprenotazioni.service.GestionePrenotazioniService;
import it.unisa.hotelcampus.model.dao.CameraRepository;
import it.unisa.hotelcampus.model.dao.ServizioRepository;
import it.unisa.hotelcampus.model.entity.*;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDate;
import java.util.*;

/**
 * Il controller PrenotazioniController gestisce le operazioni relative
 * alle prenotazioni nel sistema di prenotazione.
 * Permette di visualizzare il riepilogo di una prenotazione e di
 * confermare la prenotazione stessa.
 */
@Controller
@RequestMapping("/prenotazioni")
public class PrenotazioniController {
    private final GestionePrenotazioniService gestionePrenotazioniService;
    private final CameraRepository cameraRepository;
    private final ServizioRepository servizioRepository;

    /**
     * Costruisce un'istanza di PrenotazioniController con i servizi
     * necessari iniettati.
     *
     * @param gestionePrenotazioniService il servizio di gestione delle prenotazioni
     * @param cameraRepository            il repository per la gestione delle camere
     * @param servizioRepository          il repository per la gestione dei servizi
     */
    @Autowired
    public PrenotazioniController(GestionePrenotazioniService gestionePrenotazioniService, CameraRepository cameraRepository, ServizioRepository servizioRepository) {
        this.gestionePrenotazioniService = gestionePrenotazioniService;
        this.cameraRepository = cameraRepository;
        this.servizioRepository = servizioRepository;
    }

    /**
     * Gestisce la richiesta POST per visualizzare il riepilogo di una prenotazione.
     * Recupera i dettagli della camera, i servizi selezionati e calcola i costi totali.
     *
     * @param model         il modello da utilizzare per aggiungere attributi
     * @param id            l'ID della camera selezionata
     * @param numeroOspiti  il numero di ospiti
     * @param quantita      mappa delle quantità dei servizi selezionati, con chiavi nel formato "quantita[servizioId]"
     * @param checkIn       la data di check-in in formato stringa "yyyy-MM-dd"
     * @param checkOut      la data di check-out in formato stringa "yyyy-MM-dd"
     * @return il nome della vista "riepilogo" con i dettagli della prenotazione, o "error" in caso di errore
     */
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
        int totaleServizi = 0;

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

        int totaleCamera = camera.getCosto();
        int totale = totaleCamera + totaleServizi;

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

    /**
     * Gestisce la richiesta POST per confermare una prenotazione.
     * Crea una nuova prenotazione nel sistema con i dettagli forniti.
     *
     * @param numeroCamera   l'ID della camera da prenotare
     * @param numeroOspiti   il numero di ospiti
     * @param quantita        mappa delle quantità dei servizi selezionati, con chiavi nel formato "quantita[servizioId]"
     * @param checkIn        la data di check-in in formato stringa "yyyy-MM-dd"
     * @param checkOut       la data di check-out in formato stringa "yyyy-MM-dd"
     * @param utente         l'utente autenticato che effettua la prenotazione
     * @param model          il modello da utilizzare per aggiungere attributi
     * @return il nome della vista "confermaPrenotazione" con i dettagli della prenotazione, o "error" in caso di errore
     */
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

        if(checkIn.trim().equals("")){
            model.addAttribute("generalError", "Nessuna data di check-in selezionata");
            return "error";
        }

        if(checkOut.trim().equals("")){
            model.addAttribute("generalError", "Nessuna data di check-out selezionata");
            return "error";
        }


        LocalDate dataCheckIn = LocalDate.parse(checkIn);
        LocalDate dataCheckOut = LocalDate.parse(checkOut);

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

        return "confermaPrenotazione";
    }
}
