package co.edu.udistrital.mdp.back.dto;

import lombok.Data;

@Data
public class RecomendacionDTO {
    private String motivo;
    private Double puntaje;
    private OutfitDTO outfit;
}
