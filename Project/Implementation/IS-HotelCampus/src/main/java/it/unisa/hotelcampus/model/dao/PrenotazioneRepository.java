package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;

/**
 * Repository per l'entità {@link Prenotazione}.
 * Fornisce metodi per eseguire operazioni di accesso e manipolazione dei dati relativi alle prenotazioni dei clienti.
 * Estende {@link JpaRepository} per sfruttare le funzionalità CRUD di base.
 *
 * @see Prenotazione
 */
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    /**
     * Verifica se esiste almeno una prenotazione per una determinata camera in un intervallo di date specificato.
     *
     * @param cameraId    l'identificativo della camera
     * @param dataCheckIn la data di check-in desiderata
     * @param dataCheckOut la data di check-out desiderata
     * @return {@code true} se esiste almeno una prenotazione che soddisfa i criteri, {@code false} altrimenti
     */
    @Query("""
        SELECT COUNT(p) > 0 FROM Prenotazione p
        WHERE p.camera.id = :cameraId 
            AND (
                (p.dataCheckIn <= :dataCheckIn AND p.dataCheckOut > :dataCheckIn) 
            OR (:dataCheckIn < p.dataCheckIn AND p.dataCheckIn < :dataCheckOut)
            OR (:dataCheckIn < p.dataCheckOut AND p.dataCheckOut < :dataCheckOut)     
            )   
    """)
    boolean existsPrenotazione(Long cameraId, LocalDate dataCheckIn, LocalDate dataCheckOut);
}
