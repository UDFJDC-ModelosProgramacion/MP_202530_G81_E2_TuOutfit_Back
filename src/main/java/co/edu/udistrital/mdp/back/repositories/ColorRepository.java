package co.edu.udistrital.mdp.back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.ColorEntity;

/**
 * Interfaz que persiste un color
 */

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity, Long> {
    Optional<ColorEntity> findByNombreIgnoreCase (String nombre);
    Optional<ColorEntity> findByCodigoHexIgnoreCase (String codigoHex);
}
