package it.unisa.hotelcampus.utils;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(WebRequest webRequest, Model model) {
        // Ottieni gli attributi di errore
        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(
                webRequest, ErrorAttributeOptions.defaults()
        );

        // Estrai il messaggio di errore
        String generalError = (String) errorDetails.getOrDefault("error", "Errore sconosciuto");

        // Imposta il messaggio di errore nel modello
        model.addAttribute("generalError", generalError);

        // Reindirizza alla pagina di errore
        return "error";
    }

}
