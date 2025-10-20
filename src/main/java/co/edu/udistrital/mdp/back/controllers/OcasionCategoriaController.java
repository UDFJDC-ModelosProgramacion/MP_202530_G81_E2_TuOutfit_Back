package co.edu.udistrital.mdp.back.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.back.dto.CategoriaDTO;
import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.services.OcasionCategoriaService;

@RestController
@RequestMapping("/ocasiones/{ocasionId}/categorias")
public class OcasionCategoriaController {

    @Autowired
    private OcasionCategoriaService ocasionCategoriaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Asocia una categoría existente a una ocasión.
     */
    @PostMapping("/{categoriaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoriaDTO addCategoriaToOcasion(@PathVariable Long ocasionId, @PathVariable Long categoriaId)
            throws EntityNotFoundException {
        CategoriaEntity categoriaEntity = ocasionCategoriaService.addCategoriaToOcasion(ocasionId, categoriaId);
        return modelMapper.map(categoriaEntity, CategoriaDTO.class);
    }

    /**
     * Obtiene todas las categorías asociadas a una ocasión.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CategoriaDTO> getCategoriasByOcasion(@PathVariable Long ocasionId) throws EntityNotFoundException {
        List<CategoriaEntity> categorias = ocasionCategoriaService.getCategoriasByOcasion(ocasionId);
        return modelMapper.map(categorias, new TypeToken<List<CategoriaDTO>>() {}.getType());
    }

    /**
     * Obtiene una categoría específica asociada a una ocasión.
     */
    @GetMapping("/{categoriaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoriaDTO getCategoriaByOcasion(@PathVariable Long ocasionId, @PathVariable Long categoriaId)
            throws EntityNotFoundException {
        CategoriaEntity categoriaEntity = ocasionCategoriaService.getCategoriaByOcasion(ocasionId, categoriaId);
        return modelMapper.map(categoriaEntity, CategoriaDTO.class);
    }

    /**
     * Elimina la asociación entre una categoría y una ocasión.
     */
    @DeleteMapping("/{categoriaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeCategoriaFromOcasion(@PathVariable Long ocasionId, @PathVariable Long categoriaId)
            throws EntityNotFoundException {
        ocasionCategoriaService.removeCategoriaFromOcasion(ocasionId, categoriaId);
    }
}
