package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.CategoriaEntity;

/**
 * Interfaz que persiste una categoria
 */

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
}
