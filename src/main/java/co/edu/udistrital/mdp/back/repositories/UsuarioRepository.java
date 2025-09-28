package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;

/**
 * Interfaz que persiste un usuario
 *
 * @author ISIS2603
 *
 */
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}

