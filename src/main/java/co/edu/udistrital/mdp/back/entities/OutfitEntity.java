package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un outfit en la persistencia
 */
@Data
@Entity
public class OutfitEntity extends BaseEntity {

    private String nombre;
    private Double precioEstimado;

    // Relación Outfit - Prenda (lado inverso, NO dueño)
    @PodamExclude
    @ManyToMany(mappedBy = "outfits")
    private List<PrendaEntity> prendas = new ArrayList<>();

    @PodamExclude
    @OneToOne
    @JoinColumn(name = "imagen_id")
    private ImagenEntity imagen;

     @PodamExclude
    @ManyToOne
    private CategoriaEntity categoria;

    // Relación Outfit - Lista de deseos (lado inverso, NO dueño)
    @PodamExclude
    @ManyToMany(mappedBy = "outfits")
    private List<ListaDeseosEntity> listasDeseos = new ArrayList<>();
}