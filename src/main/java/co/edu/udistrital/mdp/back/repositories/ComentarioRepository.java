package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.ComentarioEntity;

/**
 * Interfaz que persiste un comentario
 *
 * @author ISIS2603
 *
 */
@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Long> {
}