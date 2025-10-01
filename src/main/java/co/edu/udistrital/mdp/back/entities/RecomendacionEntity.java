package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa una recomendación de outfit para un usuario.
 */

@Data
@Entity
public class RecomendacionEntity extends BaseEntity {

    // Texto opcional que explica la razón de la recomendación
    private String motivo;

    // Puntaje interno que indica qué tan relevante es esta recomendación
    private Double puntaje;

    @PodamExclude
    // La recomendación está dirigida a un único usuario
    // Un usuario puede recibir muchas recomendaciones
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @PodamExclude
    // La recomendación apunta a un único outfit
    // Un outfit puede aparecer en muchas recomendaciones para distintos usuarios
    @ManyToOne
    @JoinColumn(name = "outfit_id", nullable = false)
    private OutfitEntity outfit;
}
