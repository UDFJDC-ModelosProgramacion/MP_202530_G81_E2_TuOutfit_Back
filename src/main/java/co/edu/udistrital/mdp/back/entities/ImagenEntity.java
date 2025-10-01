package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Clase que representa una imagen en la persistencia.
 */

@Data
@Entity
public class ImagenEntity extends BaseEntity {

    // URL o ruta de la imagen almacenada
    private String imagen;

    // Una imagen puede pertenecer a un outfit
    @OneToOne(mappedBy = "imagen")
    private OutfitEntity outfit;

    // Una imagen puede pertenecer a una prenda
    @OneToOne(mappedBy = "imagen")
    private PrendaEntity prenda;

    // Una imagen puede pertenecer a una marca (ej: logo)
    @OneToOne(mappedBy = "imagen")
    private MarcaEntity marca;
}
