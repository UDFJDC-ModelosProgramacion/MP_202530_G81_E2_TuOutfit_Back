package co.edu.udistrital.mdp.back.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import java.util.ArraysList;
import java.util.List;

@author

@Data

@Entity
pubblic class ColorEntity extends BaseEntity {
private String nombre;
@OneToMany(mappedBy = "color", cascade = CascadeType.ALL, orphanRemoval = true)
private List<PrendaEntity> prendas = new ArrayList<>();
}

