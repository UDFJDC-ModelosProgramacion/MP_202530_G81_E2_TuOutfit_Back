package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.MarcaEntity;

/**
 * Interfaz que persiste una marca
 */

@Repository
public interface MarcaRepository extends JpaRepository<MarcaEntity, Long> {
    boolean existsByNombre(String nombre);
}
