package it.unisa.hotelcampus.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Interceptor per aggiungere l'oggetto "utente" al modello di tutte le richieste.
 * Consente di accedere alle informazioni dell'utente autenticato nelle viste.
 */
@Component
public class UtenteInterceptor implements HandlerInterceptor {

    /**
     * Dopo l'esecuzione del metodo del controller, aggiunge l'oggetto "utente" al modello se presente nella sessione.
     *
     * @param request      la richiesta HTTP
     * @param response     la risposta HTTP
     * @param handler      l'handler (controller) che ha gestito la richiesta
     * @param modelAndView il modello e la vista associati alla richiesta
     * @throws Exception se si verifica un errore durante l'elaborazione
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && request.getSession() != null) {
            HttpSession session = request.getSession();
            Object utente = session.getAttribute("utente");
            if (utente != null) {
                modelAndView.addObject("utente", utente);
            }
        }
    }
}
