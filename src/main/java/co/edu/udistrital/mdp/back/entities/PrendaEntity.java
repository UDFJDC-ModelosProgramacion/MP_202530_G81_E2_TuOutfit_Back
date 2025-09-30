package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import java.util.List;

/**
 * Clase que representa una prenda en la persistencia.
 */

@Data
@Entity
public class PrendaEntity extends BaseEntity {

    // Nombre de la prenda (ej: "Camisa", "Pantalón", "Chaqueta")
    private String nombre;

    // Precio de la prenda
    private Double precio;

    @PodamExclude
    // Una prenda pertenece a un color
    @ManyToOne
    @JoinColumn(name = "color_id")
    private ColorEntity color;

    @PodamExclude
    // Una prenda pertenece a una marca
    @ManyToOne
    @JoinColumn(name = "marca_id")
    private MarcaEntity marca;

    @PodamExclude
    // Una prenda puede estar en muchos outfits
    @ManyToMany(mappedBy = "prendas")
    private List<OutfitEntity> outfits;

    @PodamExclude
    // Una prenda pertenece a una tienda
    @ManyToOne
    @JoinColumn(name = "tienda_id")
    private TiendaEntity tienda;

    @PodamExclude
    // Una prenda tiene una sola imagen
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "imagen_id", referencedColumnName = "id")
    private ImagenEntity imagen;
}
