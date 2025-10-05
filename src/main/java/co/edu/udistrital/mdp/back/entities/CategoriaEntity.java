package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una categoría en la persistencia
 */

@Data
@Entity
public class CategoriaEntity extends BaseEntity {

    // Edad recomendada para la categoría (ej: 18, 25, 40)
    private Integer edad;

    // Nombre de la categoría (ej: "Juvenil", "Adulto", "Clásico")
    private String nombre;

    @PodamExclude
    // Muchas prendas pueden pertenecer a una categoría
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrendaEntity> prendas = new ArrayList<>();

    @PodamExclude
    // Muchas categorías pueden pertenecer a un Outfit
    @ManyToOne
    private OutfitEntity outfit;

    @PodamExclude
    @ManyToMany(mappedBy = "categorias")
    private List<OcasionEntity> ocasiones = new ArrayList<>();
}