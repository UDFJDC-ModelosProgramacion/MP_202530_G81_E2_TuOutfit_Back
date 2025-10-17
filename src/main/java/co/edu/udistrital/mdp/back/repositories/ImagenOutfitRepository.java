package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.ImagenOutfitEntity;

/**
 * Interfaz que persiste una imagen
 */

@Repository
public interface ImagenOutfitRepository extends JpaRepository<ImagenOutfitEntity, Long> {
}
