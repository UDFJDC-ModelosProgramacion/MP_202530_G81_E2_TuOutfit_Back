package co.edu.udistrital.mdp.back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.RecomendacionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;

@Repository
public interface RecomendacionRepository extends JpaRepository<RecomendacionEntity, Long> {

    /**
     * Verifica si ya existe una recomendación con el mismo usuario y outfit
     */
    boolean existsByUsuarioAndOutfit(UsuarioEntity usuario, OutfitEntity outfit);
}
