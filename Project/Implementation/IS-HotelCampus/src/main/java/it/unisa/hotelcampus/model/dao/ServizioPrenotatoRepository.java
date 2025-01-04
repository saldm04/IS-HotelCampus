package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.ServizioPrenotato;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository per l'entità {@link ServizioPrenotato}.
 * Fornisce metodi per eseguire operazioni di accesso e manipolazione dei dati relativi ai servizi prenotati.
 * Estende {@link JpaRepository} per sfruttare le funzionalità CRUD di base.
 *
 * @see ServizioPrenotato
 */
public interface ServizioPrenotatoRepository extends JpaRepository<ServizioPrenotato, Long> {
    // Altri metodi personalizzati possono essere aggiunti qui se necessario
}
