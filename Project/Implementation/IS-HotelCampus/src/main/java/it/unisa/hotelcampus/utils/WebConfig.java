package it.unisa.hotelcampus.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Configurazione web dell'applicazione.
 * Registra gli interceptor.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UtenteInterceptor utenteInterceptor;

    /**
     * Aggiunge gli interceptor registrati all'InterceptorRegistry.
     *
     * @param registry l'oggetto InterceptorRegistry per registrare gli interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(utenteInterceptor).addPathPatterns("/**");
    }
}
