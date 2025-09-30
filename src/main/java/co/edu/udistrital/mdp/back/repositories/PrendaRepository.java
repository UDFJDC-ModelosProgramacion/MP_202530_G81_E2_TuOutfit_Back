package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.PrendaEntity;

/**
 * Interfaz que persiste una prenda
 */

@Repository
public interface PrendaRepository extends JpaRepository<PrendaEntity, Long> {  
}
