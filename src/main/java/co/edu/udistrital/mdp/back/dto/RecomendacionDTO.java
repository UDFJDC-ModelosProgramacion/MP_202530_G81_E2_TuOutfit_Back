package co.edu.udistrital.mdp.back.dto;
import java.util.Date;

import lombok.Data;
import lombok.Getter;

import lombok.Setter;

@Data
public class RecomendacionDTO {
    private String motivo;
    private Double puntaje;
    private OutfitDTO outfit;
}
