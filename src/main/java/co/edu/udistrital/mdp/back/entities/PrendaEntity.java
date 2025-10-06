package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una prenda en la persistencia.
 */
@Data
@Entity
public class PrendaEntity extends BaseEntity {

    private String nombre;
    private Double precio;

    @PodamExclude
    @ManyToOne
    private ColorEntity color;

    @PodamExclude
    @ManyToOne
    private MarcaEntity marca;

    // Relación Prenda - Outfit (este es el lado dueño de la relación)
    @PodamExclude
    @ManyToMany
    @JoinTable(
        name = "prenda_outfit",
        joinColumns = @JoinColumn(name = "prenda_id"),
        inverseJoinColumns = @JoinColumn(name = "outfit_id")
    )
    private List<OutfitEntity> outfits = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private TiendaEntity tienda;

    @PodamExclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "imagen_id")
    private ImagenEntity imagen;

    @PodamExclude
    @ManyToOne
    private CategoriaEntity categoria;
}