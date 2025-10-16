package co.edu.udistrital.mdp.back.dto;
import java.util.ArrayList;
import lombok.Data;
import java.util.List;

import lombok.Getter;

import lombok.Data;
@Data
public class OcasionDetailDTO {
private List<CategoriaDTO> categoria = new ArrayList<>();
}
