package it.unisa.hotelcampus.model.dao;


import it.unisa.hotelcampus.model.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;


@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {

    Collection<Camera> findCameraByisDeletedIsFalse();

    @Query("""
        SELECT c 
        FROM Camera c 
        WHERE c.numeroMaxOspiti >= :numeroOspiti
          AND c.isDeleted = false
          AND c.id NOT IN (
              SELECT p.camera.id 
              FROM Prenotazione p 
              WHERE p.dataCheckIn < :dataCheckOut 
                AND p.dataCheckOut > :dataCheckIn
          )
    """)
    Collection<Camera> findCamereDisponibili(
            @Param("dataCheckIn") Date dataCheckIn,
            @Param("dataCheckOut") Date dataCheckOut,
            @Param("numeroOspiti") int numeroOspiti
    );
}