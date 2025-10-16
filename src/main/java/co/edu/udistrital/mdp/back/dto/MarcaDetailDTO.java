package co.edu.udistrital.mdp.back.dto;

import java.util.List;

import lombok.Data;

@Data
public class MarcaDetailDTO extends MarcaDTO {
    private List<TiendaDTO> tiendas;
    private List<PrendaDTO> prendas;
}
