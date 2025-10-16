package co.edu.udistrital.mdp.back.dto;

import java.util.List;

import lombok.Data;

@Data
public class UsuarioDetailDTO extends UsuarioDTO{
    private List<ComentarioDTO> comentarios;
}
