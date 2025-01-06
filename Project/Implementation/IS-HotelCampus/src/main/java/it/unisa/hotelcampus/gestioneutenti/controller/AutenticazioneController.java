package it.unisa.hotelcampus.gestioneutenti.controller;

import it.unisa.hotelcampus.gestioneutenti.service.GestioneUtentiService;
import it.unisa.hotelcampus.model.entity.Utente;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller per la gestione dell'autenticazione e della registrazione degli utenti.
 * Gestisce le richieste HTTP relative al login, alla registrazione (signIn) e al logout degli utenti.
 * Fornisce anche funzionalità di ricerca delle nazionalità europee per l'autocompletamento.
 *
 * @version 1.0
 */
@Controller
@RequestMapping("/utenti")
public class AutenticazioneController {
    private GestioneUtentiService gestioneUtentiService;

    /**
     * Lista delle nazioni europee utilizzata per la funzionalità di ricerca autocompletata.
     */
    private List<String> nazioniEuropee = Arrays.asList(
            "Albania", "Andorra", "Armenia", "Austria", "Azerbaigian", "Bielorussia", "Belgio",
            "Bosnia ed Erzegovina", "Bulgaria", "Croazia", "Cipro", "Repubblica Ceca", "Danimarca",
            "Estonia", "Finlandia", "Francia", "Georgia", "Germania", "Grecia", "Ungheria", "Islanda",
            "Irlanda", "Italia", "Kazakistan", "Kosovo", "Lettonia", "Liechtenstein", "Lituania",
            "Lussemburgo", "Malta", "Moldavia", "Monaco", "Montenegro", "Paesi Bassi", "Macedonia del Nord",
            "Norvegia", "Polonia", "Portogallo", "Romania", "Russia", "San Marino", "Serbia", "Slovacchia",
            "Slovenia", "Spagna", "Svezia", "Svizzera", "Turchia", "Ucraina", "Regno Unito", "Città del Vaticano", "Altro"
    );

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param gestioneUtentiService il servizio per la gestione degli utenti
     */
    @Autowired
    public AutenticazioneController(GestioneUtentiService gestioneUtentiService) {
        this.gestioneUtentiService = gestioneUtentiService;
    }

    /**
     * Mostra la pagina di login.
     * Se l'utente è già autenticato (presente nella sessione), reindirizza alla home page.
     *
     * @param utente l'utente attualmente autenticato (se presente nella sessione)
     * @return la vista della pagina di login o un reindirizzamento alla home page
     */
    @GetMapping("/login")
    public String showLogin(@SessionAttribute(name = "utente", required = false) Object utente) {
        if (utente != null) {
            return "redirect:/";
        }
        return "login";
    }

    /**
     * Mostra la pagina di registrazione (signIn).
     * Se l'utente è già autenticato (presente nella sessione), reindirizza alla home page.
     * Aggiunge la data corrente al modello per visualizzarla nella vista.
     *
     * @param utente l'utente attualmente autenticato (se presente nella sessione)
     * @param model  il modello per aggiungere attributi alla vista
     * @return la vista della pagina di registrazione o un reindirizzamento alla home page
     */
    @GetMapping("/signIn")
    public String showSignIn(@SessionAttribute(name = "utente", required = false) Object utente, Model model) {
        if (utente != null) {
            return "redirect:/";
        }
        model.addAttribute("currentDate", LocalDate.now());
        return "signIn";
    }

    /**
     * Fornisce una lista di nazionalità europee che iniziano con una determinata stringa.
     * Utilizzata per l'autocompletamento nel campo di selezione della nazionalità nella pagina di registrazione.
     *
     * @param nazionalita la stringa con cui filtrare le nazionalità
     * @return una lista di nazionalità europee corrispondenti al filtro
     */
    @GetMapping("/CercaNazione")
    @ResponseBody
    public List<String> cercaNazionalita(@RequestParam(required = false) String nazionalita) {
        if (nazionalita == null || nazionalita.isEmpty()) {
            return List.of(); // Restituisce una lista vuota se il parametro è mancante
        }
        return nazioniEuropee.stream()
                .filter(n -> n.toLowerCase().startsWith(nazionalita.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Gestisce la richiesta di registrazione di un nuovo utente.
     * Crea un nuovo account utente e i relativi dettagli cliente.
     *
     * @param nome          il nome dell'utente
     * @param cognome       il cognome dell'utente
     * @param email         l'email dell'utente
     * @param password      la password dell'utente
     * @param dataNascita   la data di nascita dell'utente (formato YYYY-MM-DD)
     * @param nazionalita   la nazionalità dell'utente
     * @param model         il modello per aggiungere attributi alla vista
     * @return un reindirizzamento alla home page in caso di successo, oppure la vista della pagina di registrazione con un messaggio di errore
     */
    @PostMapping("/signIn")
    public String signIn(
            @RequestParam String nome,
            @RequestParam String cognome,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String dataNascita,
            @RequestParam String nazionalita,
            Model model
    ){
        Date dataDiNascita = Date.valueOf(dataNascita);
        try {
            Utente utente = gestioneUtentiService.creaUtente(nome, cognome, dataDiNascita, nazionalita, email, password);
        } catch (Exception e){
            model.addAttribute("generalError", e.getMessage());
            model.addAttribute("currentDate", LocalDate.now());
            return "signIn";
        }
        return "redirect:/";
    }

    /**
     * Gestisce la richiesta di login di un utente.
     * Autentica l'utente e aggiunge l'oggetto utente alla sessione in caso di successo.
     *
     * @param email    l'email dell'utente
     * @param password la password dell'utente
     * @param session  la sessione HTTP per memorizzare l'utente autenticato
     * @param model    il modello per aggiungere attributi alla vista in caso di errore
     * @return un reindirizzamento alla home page in caso di successo, oppure la vista della pagina di login con un messaggio di errore
     */
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ){
        try {
            Utente utente = gestioneUtentiService.autentica(email, password);
            session.setAttribute("utente", utente);
        } catch (Exception e){
            model.addAttribute("generalError", e.getMessage());
            return "login";
        }
        return "redirect:/";
    }

    /**
     * Gestisce la richiesta di logout di un utente.
     * Invalida la sessione corrente e reindirizza alla home page.
     *
     * @param session la sessione HTTP da invalidare
     * @return un reindirizzamento alla home page
     */
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
