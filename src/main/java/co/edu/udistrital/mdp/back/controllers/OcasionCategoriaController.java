package co.edu.udistrital.mdp.back.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.back.dto.CategoriaDetailDTO;
import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.services.OcasionCategoriaService;

/**
 * Clase que implementa el recurso "ocasiones/{id}/categorias".
 * 
 * Permite gestionar las asociaciones entre una ocasión y sus categorías.
 * 
 * @author 
 */
@RestController
@RequestMapping("/ocasiones")
public class OcasionCategoriaController {

    @Autowired
    private OcasionCategoriaService ocasionCategoriaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca y devuelve todas las categorías asociadas a una ocasión.
     *
     * @param ocasionId El ID de la ocasión.
     * @return Lista de {@link CategoriaDetailDTO} con las categorías asociadas.
     */
    @GetMapping(value = "/{ocasionId}/categorias")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CategoriaDetailDTO> getCategoriasPorOcasion(@PathVariable Long ocasionId)
            throws EntityNotFoundException {
        List<CategoriaEntity> categorias = ocasionCategoriaService.getCategoriasPorOcasion(ocasionId);
        return modelMapper.map(categorias, new TypeToken<List<CategoriaDetailDTO>>() {
        }.getType());
    }

    /**
     * Asocia una categoría existente a una ocasión existente.
     *
     * @param ocasionId   El ID de la ocasión.
     * @param categoriaId El ID de la categoría que se va a asociar.
     * @return {@link CategoriaDetailDTO} con la categoría asociada.
     */
    @PostMapping(value = "/{ocasionId}/categorias/{categoriaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoriaDetailDTO addCategoriaAOcasion(@PathVariable Long ocasionId, @PathVariable Long categoriaId)
            throws EntityNotFoundException {
        CategoriaEntity categoria = ocasionCategoriaService.addCategoriaAOcasion(ocasionId, categoriaId);
        return modelMapper.map(categoria, CategoriaDetailDTO.class);
    }

    /**
     * Elimina la relación entre una ocasión y una categoría específica.
     *
     * @param ocasionId   El ID de la ocasión.
     * @param categoriaId El ID de la categoría que se desea eliminar de la ocasión.
     */
    @DeleteMapping(value = "/{ocasionId}/categorias/{categoriaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeCategoriaDeOcasion(@PathVariable Long ocasionId, @PathVariable Long categoriaId)
            throws EntityNotFoundException {
        ocasionCategoriaService.removeCategoriaDeOcasion(ocasionId, categoriaId);
    }
}
