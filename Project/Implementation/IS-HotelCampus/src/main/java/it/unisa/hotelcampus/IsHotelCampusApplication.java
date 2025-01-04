package it.unisa.hotelcampus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale per l'applicazione Spring Boot IsHotelCampus.
 * Questa classe funge da punto di ingresso per l'applicazione, avviando il contesto Spring.
 *
 * <p>
 * L'annotazione {@code @SpringBootApplication} abilita la configurazione automatica, la scansione dei componenti e
 * altre funzionalità di Spring Boot necessarie per avviare l'applicazione.
 * </p>
 *
 * <p>
 * Questa classe utilizza {@link SpringApplication} per avviare l'applicazione Spring Boot.
 * </p>
 *
 * @version 1.0
 */
@SpringBootApplication
public class IsHotelCampusApplication {

    /**
     * Il metodo principale che avvia l'applicazione Spring Boot.
     *
     * @param args gli argomenti della linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        SpringApplication.run(IsHotelCampusApplication.class, args);
    }

}
