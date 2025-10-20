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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.back.dto.ColorDTO;
import co.edu.udistrital.mdp.back.dto.ColorDetailDTO;
import co.edu.udistrital.mdp.back.entities.ColorEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.ColorService;

@RestController
@RequestMapping("/colores")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ColorDetailDTO> findAll() {
        List<ColorEntity> colores = colorService.getColores();
        return modelMapper.map(colores, new TypeToken<List<ColorDetailDTO>>() {}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ColorDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        ColorEntity colorEntity = colorService.getColor(id);
        return modelMapper.map(colorEntity, ColorDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ColorDTO create(@RequestBody ColorDTO colorDTO) throws IllegalOperationException {
        ColorEntity colorEntity = colorService.createColor(modelMapper.map(colorDTO, ColorEntity.class));
        return modelMapper.map(colorEntity, ColorDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ColorDTO update(@PathVariable Long id, @RequestBody ColorDTO colorDTO)
            throws EntityNotFoundException, IllegalOperationException {
        ColorEntity colorEntity = colorService.updateColor(id, modelMapper.map(colorDTO, ColorEntity.class));
        return modelMapper.map(colorEntity, ColorDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        colorService.deleteColor(id);
    }
}
