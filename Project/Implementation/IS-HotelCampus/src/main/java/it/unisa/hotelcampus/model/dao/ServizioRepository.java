package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.Servizio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Repository per l'entità {@link Servizio}.
 * Fornisce metodi per eseguire operazioni di accesso e manipolazione dei dati relativi ai servizi offerti dall'hotel.
 * Estende {@link JpaRepository} per sfruttare le funzionalità CRUD di base.
 *
 * @see Servizio
 */
public interface ServizioRepository extends JpaRepository<Servizio, Long> {

    /**
     * Recupera tutti i servizi che non sono stati eliminati.
     *
     * @return una collezione di servizi non eliminati
     */
    Collection<Servizio> findAllByisDeletedIsFalse();
}
