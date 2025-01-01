package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.ClienteDettagli;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteDettagliRepository extends JpaRepository<ClienteDettagli, String> {
}
