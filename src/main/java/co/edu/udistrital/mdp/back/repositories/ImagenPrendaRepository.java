package co.edu.udistrital.mdp.back.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.udistrital.mdp.back.entities.ImagenPrendaEntity;
@Repository
public interface ImagenPrendaRepository extends JpaRepository<ImagenPrendaEntity, Long>{

}
