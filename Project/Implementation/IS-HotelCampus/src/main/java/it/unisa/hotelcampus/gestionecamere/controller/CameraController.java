package it.unisa.hotelcampus.gestionecamere.controller;

import it.unisa.hotelcampus.gestionecamere.service.GestioneCamereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CameraController {

    @Autowired
    private GestioneCamereService gestioneCamereService;


    @GetMapping("/prenota")
    public String mostraCamere(Model model){
        model.addAttribute("camere", gestioneCamereService.getCamere());
        return "prenotaOra";
    }
}
