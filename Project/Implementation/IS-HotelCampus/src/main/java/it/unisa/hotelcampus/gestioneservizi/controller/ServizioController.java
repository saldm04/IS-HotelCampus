package it.unisa.hotelcampus.gestioneservizi.controller;

import it.unisa.hotelcampus.gestioneservizi.service.GestioneServiziService;
import it.unisa.hotelcampus.model.entity.Servizio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/servizi")
public class ServizioController {

    private GestioneServiziService gestioneServiziService;

    @Autowired
    public ServizioController(final GestioneServiziService gestioneServiziService) {
        this.gestioneServiziService = gestioneServiziService;
    }

    @GetMapping
    public String getServizi(Model model) {
        model.addAttribute("servizi", gestioneServiziService.getServizi());
        return "servizi";
    }

    @PostMapping
    @ResponseBody
    public Collection<Servizio> getServiziJson() {
        return gestioneServiziService.getServizi();
    }
}
