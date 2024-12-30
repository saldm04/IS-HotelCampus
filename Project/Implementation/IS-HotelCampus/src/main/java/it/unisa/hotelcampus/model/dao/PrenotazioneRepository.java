package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    @Query("""
        SELECT COUNT(p) > 0 from Prenotazione p
        WHERE p.camera.id = :cameraId 
            AND (
                (p.dataCheckIn <= :dataCheckIn AND p.dataCheckOut >= :dataCheckIn) 
            OR (:dataCheckIn < p.dataCheckIn AND p.dataCheckIn < :dataCheckOut)
            OR (:dataCheckIn < p.dataCheckOut AND p.dataCheckOut < :dataCheckOut)     
            )   
    """)
    boolean existsPrenotazione(Long cameraId, Date dataCheckIn, Date dataCheckOut);
}
