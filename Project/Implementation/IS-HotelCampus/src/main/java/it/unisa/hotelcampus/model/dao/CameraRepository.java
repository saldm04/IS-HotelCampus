package it.unisa.hotelcampus.model.dao;

import it.unisa.hotelcampus.model.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

/**
 * Repository per l'entità {@link Camera}.
 * Fornisce metodi per eseguire operazioni di accesso e manipolazione dei dati relativi alle camere dell'hotel.
 * Estende {@link JpaRepository} per sfruttare le funzionalità CRUD di base.
 *
 * @see Camera
 */
@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {

    /**
     * Trova una camera per numero.
     *
     * @param numero il numero della camera
     * @return la camera con il numero specificato, se esiste
     */
    Camera findByNumero(int numero);

    /**
     * Recupera tutte le camere che non sono state eliminate.
     *
     * @return una collezione di camere non eliminate
     */
    Collection<Camera> findCameraByisDeletedIsFalse();

    /**
     * Trova le camere disponibili in un determinato intervallo di date e che possono ospitare un numero minimo di ospiti.
     * Una camera è considerata disponibile se:
     * <ul>
     *     <li>Non è eliminata.</li>
     *     <li>Il numero massimo di ospiti è maggiore o uguale al numero richiesto.</li>
     *     <li>Non è già prenotata nelle date specificate.</li>
     * </ul>
     *
     * @param dataCheckIn   la data di check-in desiderata
     * @param dataCheckOut  la data di check-out desiderata
     * @param numeroOspiti  il numero minimo di ospiti che la camera deve poter ospitare
     * @return una collezione di camere disponibili che soddisfano i criteri specificati
     */
    @Query("""
        SELECT c 
        FROM Camera c 
        WHERE c.numeroMaxOspiti >= :numeroOspiti
          AND c.isDeleted = false
          AND c.id NOT IN (
              SELECT p.camera.id 
              FROM Prenotazione p 
              WHERE (p.dataCheckIn <= :dataCheckIn AND p.dataCheckOut > :dataCheckIn) 
            OR (:dataCheckIn < p.dataCheckIn AND p.dataCheckIn < :dataCheckOut)
            OR (:dataCheckIn < p.dataCheckOut AND p.dataCheckOut < :dataCheckOut) 
          )
    """)
    Collection<Camera> findCamereDisponibili(
            @Param("dataCheckIn") LocalDate dataCheckIn,
            @Param("dataCheckOut") LocalDate dataCheckOut,
            @Param("numeroOspiti") int numeroOspiti
    );
}
