package co.edu.udistrital.mdp.back.dto;

import java.util.ArrayList;

import lombok.Data;

import java.util.List;

@Data
public class OcasionDetailDTO extends OcasionDTO {
    private List<CategoriaDTO> categoria = new ArrayList<>();
}
