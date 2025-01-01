package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.ServizioPrenotato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServizioPrenotatoRepository extends JpaRepository<ServizioPrenotato, Long> {
}
