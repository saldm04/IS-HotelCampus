package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {
    Collection<Utente> findAllByRuolo(Utente.Ruolo ruolo);
}
