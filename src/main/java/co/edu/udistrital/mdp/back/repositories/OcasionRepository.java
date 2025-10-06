package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.OcasionEntity;

import java.util.Optional;

/**
 * Interfaz que maneja la persistencia de las ocasiones.
 */
@Repository
public interface OcasionRepository extends JpaRepository<OcasionEntity, Long> {

    /**
     * Verifica si ya existe una ocasión con el mismo nombre (sin importar mayúsculas o minúsculas)
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Busca una ocasión por su nombre (sin importar mayúsculas o minúsculas)
     */
    Optional<OcasionEntity> findByNombreIgnoreCase(String nombre);
}
