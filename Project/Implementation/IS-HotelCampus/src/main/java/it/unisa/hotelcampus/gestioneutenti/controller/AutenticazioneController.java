package it.unisa.hotelcampus.gestioneutenti.controller;

import it.unisa.hotelcampus.gestioneutenti.services.GestioneUtentiService;
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

@Controller
@RequestMapping("/utenti")
public class AutenticazioneController {
    private GestioneUtentiService gestioneUtentiService;

    @Autowired
    public AutenticazioneController(GestioneUtentiService gestioneUtentiService) {
        this.gestioneUtentiService = gestioneUtentiService;
    }

    private List<String> nazioniEuropee = Arrays.asList(
            "Albania", "Andorra", "Armenia", "Austria", "Azerbaigian", "Bielorussia", "Belgio",
            "Bosnia ed Erzegovina", "Bulgaria", "Croazia", "Cipro", "Repubblica Ceca", "Danimarca",
            "Estonia", "Finlandia", "Francia", "Georgia", "Germania", "Grecia", "Ungheria", "Islanda",
            "Irlanda", "Italia", "Kazakistan", "Kosovo", "Lettonia", "Liechtenstein", "Lituania",
            "Lussemburgo", "Malta", "Moldavia", "Monaco", "Montenegro", "Paesi Bassi", "Macedonia del Nord",
            "Norvegia", "Polonia", "Portogallo", "Romania", "Russia", "San Marino", "Serbia", "Slovacchia",
            "Slovenia", "Spagna", "Svezia", "Svizzera", "Turchia", "Ucraina", "Regno Unito", "Città del Vaticano", "Altro"
    );

    @GetMapping("/login")
    public String showLogin(@SessionAttribute(name = "utente", required = false) Object utente) {
        if (utente != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/signIn")
    public String showSignIn(@SessionAttribute(name = "utente", required = false) Object utente, Model model) {
        if (utente != null) {
            return "redirect:/";
        }
        model.addAttribute("currentDate", LocalDate.now());
        return "signIn";
    }

    @GetMapping("/CercaNazione")
    @ResponseBody
    public List<String> cercaNazionalita(@RequestParam(required = false) String nazionalita) {
        if (nazionalita == null || nazionalita.isEmpty()) {
            return List.of(); // Restituisci una lista vuota se il parametro è mancante
        }
        return nazioniEuropee.stream()
                .filter(n -> n.toLowerCase().startsWith(nazionalita.toLowerCase()))
                .collect(Collectors.toList());
    }

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
        }catch (Exception e){
            model.addAttribute("generalError", e.getMessage());
            model.addAttribute("currentDate", LocalDate.now());
            return "signIn";
        }
        return "redirect:/";
    }

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
        }catch (Exception e){
            model.addAttribute("generalError", e.getMessage());
            return "login";
        }
        return "redirect:/";
    }
}

