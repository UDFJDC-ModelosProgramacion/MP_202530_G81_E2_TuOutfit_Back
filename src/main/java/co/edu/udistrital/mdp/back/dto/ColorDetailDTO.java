package co.edu.udistrital.mdp.back.dto;

import java.util.List;

import lombok.Data;

@Data
public class ColorDetailDTO extends ColorDTO { 
     private List<PrendaDTO> prendas;
}
