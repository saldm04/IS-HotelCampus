package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.Servizio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ServizioRepository extends JpaRepository<Servizio, Long> {

    public Collection<Servizio> findAllByisDeletedIsFalse();
}
