package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una ocasión de uso (ej: fiesta, trabajo, deporte).
 */

@Data
@Entity
public class OcasionEntity extends BaseEntity {

    // Nombre de la ocasión (ej: "Trabajo", "Boda", "Fiesta", "Deportivo")
    private String nombre;

    // Una ocasión puede tener muchas categorías
    @ManyToMany(mappedBy = "ocasiones", cascade = CascadeType.ALL)
    private List<CategoriaEntity> categorias = new ArrayList<>();
}
