package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.List;

/**
 * Clase que representa un outfit en la persistencia
 */

@Data
@Entity
public class OutfitEntity extends BaseEntity {

    private String nombre;
    private Double precioEstimado;

    @PodamExclude
    // Un outfit puede tener muchas prendas
    @ManyToMany
    @JoinTable(
        name = "outfit_prenda",
        joinColumns = @JoinColumn(name = "outfit_id"),
        inverseJoinColumns = @JoinColumn(name = "prenda_id")
    )
    private List<PrendaEntity> prendas;

    @PodamExclude
    // Un outfit tiene una sola imagen
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "imagen_id", referencedColumnName = "id")
    private ImagenEntity imagen;

    @PodamExclude
    // Un outfit puede estar en muchas listas de deseos (muchos usuarios pueden guardarlo)
    @ManyToMany(mappedBy = "outfits")
    private List<ListaDeseosEntity> listasDeseos;
}
