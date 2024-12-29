package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.Servizio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServizioRepository extends JpaRepository<Servizio, Long> {
}
