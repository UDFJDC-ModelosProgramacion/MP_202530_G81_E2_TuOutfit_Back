package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.List;

/**
 * Clase que representa una lista de deseos en la persistencia.
 */

@Data
@Entity
public class ListaDeseosEntity extends BaseEntity {

    @PodamExclude
    // Cada usuario tiene una sola lista de deseos
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @PodamExclude
    // Una lista de deseos puede contener muchos outfits
    @ManyToMany
    @JoinTable(
        name = "lista_outfit",
        joinColumns = @JoinColumn(name = "lista_id"),
        inverseJoinColumns = @JoinColumn(name = "outfit_id")
    )
    private List<OutfitEntity> outfits;
}
