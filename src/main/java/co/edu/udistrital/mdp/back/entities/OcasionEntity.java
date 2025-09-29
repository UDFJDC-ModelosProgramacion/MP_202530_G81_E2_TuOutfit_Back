package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una ocasión de uso (ej: fiesta, trabajo, deporte).
 *
 * Cada ocasión puede estar relacionada con muchas categorías.
 */
@Data
@Entity
public class OcasionEntity extends BaseEntity {

    private String nombre;  // Ej: "Formal", "Casual", "Deportivo"

    // Relación con Categoría
    @ManyToMany(mappedBy = "ocasiones", cascade = CascadeType.ALL)
    private List<CategoriaEntity> categorias = new ArrayList<>();
}