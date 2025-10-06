package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una ocasión (ej: "Trabajo", "Fiesta", "Deporte") en la persistencia.
 */
@Data
@Entity
public class OcasionEntity extends BaseEntity {

    private String nombre;

    // Relación Ocasion - Categoria (lado dueño con JoinTable)
    @PodamExclude
    @ManyToMany
    @JoinTable(
        name = "ocasion_categoria",
        joinColumns = @JoinColumn(name = "ocasion_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<CategoriaEntity> categorias = new ArrayList<>();
}
