package it.unisa.hotelcampus.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class UtenteInterceptor implements HandlerInterceptor {

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
