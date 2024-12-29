package it.unisa.hotelcampus.gestionecamere.controller;

import it.unisa.hotelcampus.gestionecamere.service.GestioneCamereService;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import it.unisa.hotelcampus.model.entity.Camera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/camere")
public class CameraController {

    private final GestioneCamereService gestioneCamereService;

    @Autowired
    public CameraController(GestioneCamereService gestioneCamereService){
        this.gestioneCamereService = gestioneCamereService;
    }


    @GetMapping("/prenota")
    public String mostraCamere(Model model){
        Collection<Camera> camere = gestioneCamereService.getCamere();
        int maxOspiti = camere.stream().mapToInt(Camera::getNumeroMaxOspiti).max().orElse(1);
        model.addAttribute("camere", camere);
        model.addAttribute("maxOspiti", maxOspiti);
        return "prenotaOra";
    }

    @PostMapping("/disponibiliPerPrenotazione")
    public String mostraCamereDisponibili(
            @RequestParam(value = "checkindate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkIn,
            @RequestParam(value = "checkoutdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOut,
            @RequestParam(value = "numOspiti", required = false) Integer numeroOspiti,
            Model model){

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            model.addAttribute("camere", gestioneCamereService.getCamereDisponibili(checkIn, checkOut, numeroOspiti));
            model.addAttribute("checkIn", checkIn != null ? dateFormat.format(checkIn) : "");
            model.addAttribute("checkOut", checkOut != null ? dateFormat.format(checkOut) : "");
            model.addAttribute("numeroOspiti", numeroOspiti);
            model.addAttribute("ricercaEffettuata", true);
            Collection<Camera> camere = gestioneCamereService.getCamere();
            int maxOspiti = camere.stream().mapToInt(Camera::getNumeroMaxOspiti).max().orElse(1);
            model.addAttribute("maxOspiti", maxOspiti);
        }catch (IllegalArgumentException e){
            model.addAttribute("generalError", e.getMessage());
            mostraCamere(model);
        }

        return "prenotaOra";
    }

}
