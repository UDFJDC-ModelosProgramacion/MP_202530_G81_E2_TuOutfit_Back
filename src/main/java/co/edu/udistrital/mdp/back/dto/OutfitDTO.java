package co.edu.udistrital.mdp.back.dto;
import java.util.Date;

import lombok.Data;
import lombok.Getter;

import lombok.Setter;

@Data
public class OutfitDTO {
    private String nombre;
    private Double precioEstimado;
    private ImagenDTO imagen;
}
