package co.edu.udistrital.mdp.back.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Clase que representa una lista de deseos en la persistencia
 *
 * @author ISIS2603
 */

@Data
@Entity
public class ListaDeseosEntity extends BaseEntity {
	@OneToOne // cada usuario tiene UNA lista
    private UsuarioEntity usuario;

    // Una lista de deseos puede tener muchos outfits
    @OneToMany(mappedBy = "listaDeseos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OutfitEntity> outfits = new ArrayList<>();
}