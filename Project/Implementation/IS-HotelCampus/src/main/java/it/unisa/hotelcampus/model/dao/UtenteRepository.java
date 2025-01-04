package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Repository per l'entità {@link Utente}.
 * Fornisce metodi per eseguire operazioni di accesso e manipolazione dei dati relativi agli utenti del sistema.
 * Estende {@link JpaRepository} per sfruttare le funzionalità CRUD di base.
 *
 * @see Utente
 */
@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {

    /**
     * Recupera tutti gli utenti con un determinato ruolo.
     *
     * @param ruolo il ruolo degli utenti da recuperare
     * @return una collezione di utenti che hanno il ruolo specificato
     */
    Collection<Utente> findAllByRuolo(Utente.Ruolo ruolo);
}
