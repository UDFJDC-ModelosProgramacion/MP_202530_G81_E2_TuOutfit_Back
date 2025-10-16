package co.edu.udistrital.mdp.back.dto;
import java.util.Date;

import lombok.Data;
import lombok.Getter;

import lombok.Setter;

@Data
public class ImagenDTO {
private String imagen;
private OutfitDTO outfit;
private PrendaDTO prenda;
private MarcaDTO marca;
}
