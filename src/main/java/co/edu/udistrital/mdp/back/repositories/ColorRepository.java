package co.educacion.udistrito.mdp.atrás.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.educacion.udistrito.mdp.atrás.entidades.ColorEntity;

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity, Long> {
}
