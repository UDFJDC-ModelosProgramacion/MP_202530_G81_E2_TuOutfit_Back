package co.edu.udistrital.mdp.back.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa un usuario en la persistencia
 */

@Data
@Entity
public class UsuarioEntity extends BaseEntity {

    private String nombre;

    @PodamExclude
    // Un usuario puede tener muchos comentarios
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComentarioEntity> comentarios = new ArrayList<>();

    @PodamExclude
    // Un usuario tiene exactamente una lista de deseos
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private ListaDeseosEntity wishlist;
}
