package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.util.ArrayList;
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

    // Relación ListaDeseos ↔ Outfit (lado dueño con JoinTable)
    @PodamExclude
    @ManyToMany
    @JoinTable(
        name = "lista_outfit", // nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "lista_id"), // FK a ListaDeseos
        inverseJoinColumns = @JoinColumn(name = "outfit_id") // FK a Outfit
    )
    private List<OutfitEntity> outfits = new ArrayList<>();
}