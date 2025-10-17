package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa una imagen en la persistencia.
 */

@Data
@Entity
public class ImagenOutfitEntity extends BaseEntity {

   // URL o ruta de la imagen almacenada
   private String imagen;

   @PodamExclude
   // Una imagen puede pertenecer a un outfit
   @OneToOne(mappedBy = "imagen")
   private OutfitEntity outfit;

}
