package it.unisa.hotelcampus.gestioneservizi.controller;

import it.unisa.hotelcampus.gestioneservizi.service.GestioneServiziServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/servizi")
public class ServizioController {

    private GestioneServiziServiceImpl gestioneServiziService;

    @Autowired
    public ServizioController(final GestioneServiziServiceImpl gestioneServiziService) {
        this.gestioneServiziService = gestioneServiziService;
    }

    @GetMapping
    public String getServizi(Model model) {
        model.addAttribute("servizi", gestioneServiziService.getServizi());
        return "servizi";
    }
}