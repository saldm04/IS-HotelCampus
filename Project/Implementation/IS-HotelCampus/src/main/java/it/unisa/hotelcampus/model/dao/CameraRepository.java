package it.unisa.hotelcampus.model.dao;


import it.unisa.hotelcampus.model.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface CameraRepository extends JpaRepository<Camera, Integer> {

    Collection<Camera> findCameraByisDeletedIsFalse();
}