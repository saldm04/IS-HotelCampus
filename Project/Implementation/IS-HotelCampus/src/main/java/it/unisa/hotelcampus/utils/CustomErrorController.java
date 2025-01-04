package it.unisa.hotelcampus.utils;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Il controller CustomErrorController gestisce gli errori globali dell'applicazione.
 */
@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    /**
     * Costruisce un'istanza di CustomErrorController con gli attributi di errore iniettati.
     *
     * @param errorAttributes gli attributi di errore forniti da Spring Boot
     */
    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    /**
     * Gestisce le richieste di errore e reindirizza alla vista "error" con i dettagli dell'errore.
     *
     * @param webRequest l'oggetto WebRequest che contiene i dettagli della richiesta
     * @param model      il modello da utilizzare per aggiungere attributi
     * @return il nome della vista "error" con i dettagli dell'errore
     */
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
