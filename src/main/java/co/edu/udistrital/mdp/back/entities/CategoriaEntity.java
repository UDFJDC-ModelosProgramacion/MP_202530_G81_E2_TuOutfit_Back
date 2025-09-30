package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una categoría de prendas o estilos.
 */

@Data
@Entity
public class CategoriaEntity extends BaseEntity {

    // Edad recomendada para la categoría (ej: 18, 25, 40)
    private Integer edad;

    // Nombre de la categoría (ej: "Juvenil", "Adulto", "Clásico")
    private String nombre;

    // Una categoría puede estar en muchas ocasiones
    @ManyToMany
    @JoinTable(
        name = "categoria_ocasion",
        joinColumns = @JoinColumn(name = "categoria_id"),
        inverseJoinColumns = @JoinColumn(name = "ocasion_id")
    )
    private List<OcasionEntity> ocasiones = new ArrayList<>();
}
