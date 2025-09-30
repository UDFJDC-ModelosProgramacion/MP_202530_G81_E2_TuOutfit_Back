package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.ColorEntity;

/**
 * Interfaz que persiste un color
 */

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity, Long> {
}
