package co.edu.udistrital.mdp.back.dto;
import java.util.ArrayList;
import lombok.Data;
import java.util.List;

import lombok.Getter;

import lombok.Data;
@Data
public class CategoriaDetailDTO extends CategoriaDTO {
private List<OutfitDTO> outfit = new ArrayList<>();
private List<OcasionDTO> ocasion = new ArrayList<>();
}
