package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.OcasionEntity;

/**
 * Interfaz que persiste una ocasión
 */

@Repository
public interface OcasionRepository extends JpaRepository<OcasionEntity, Long> {
}
