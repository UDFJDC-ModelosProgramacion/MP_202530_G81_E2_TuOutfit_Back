package co.edu.udistrital.mdp.back.entities;
import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa una imagen en la persistencia.
 */

@Data
@Entity
public class ImagenPrendaEntity extends BaseEntity{
    private String imagen;
    @PodamExclude
   // Una imagen puede pertenecer a una prenda
   @OneToOne(mappedBy = "imagen")
   private PrendaEntity prenda;

   @PodamExclude
   // Una imagen puede pertenecer a una marca (ej: logo)
   @OneToOne(mappedBy = "imagen")
   private MarcaEntity marca;
}
