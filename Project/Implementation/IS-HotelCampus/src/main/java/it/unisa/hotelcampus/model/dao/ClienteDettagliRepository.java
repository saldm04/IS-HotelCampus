package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.ClienteDettagli;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per l'entità {@link ClienteDettagli}.
 * Fornisce metodi per eseguire operazioni di accesso e manipolazione dei dati relativi ai dettagli dei clienti.
 * Estende {@link JpaRepository} per sfruttare le funzionalità CRUD di base.
 *
 * @see ClienteDettagli
 */
@Repository
public interface ClienteDettagliRepository extends JpaRepository<ClienteDettagli, String> {
}
