package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;

/**
 * Interfaz que persiste una tienda
 */

@Repository
public interface TiendaRepository extends JpaRepository<TiendaEntity, Long> {
}
