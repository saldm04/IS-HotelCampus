package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
}
