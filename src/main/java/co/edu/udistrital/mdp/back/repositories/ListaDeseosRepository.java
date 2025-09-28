package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.ListaDeseosEntity;

/**
 * Interfaz que persiste una lista de deseos
 *
 * @author ISIS2603
 *
 */
@Repository
public interface ListaDeseosRepository extends JpaRepository<ListaDeseosEntity, Long> {
}