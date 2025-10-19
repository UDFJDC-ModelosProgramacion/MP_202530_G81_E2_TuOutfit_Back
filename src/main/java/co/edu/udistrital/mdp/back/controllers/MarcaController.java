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

import co.edu.udistrital.mdp.back.dto.MarcaDTO;
import co.edu.udistrital.mdp.back.dto.MarcaDetailDTO;
import co.edu.udistrital.mdp.back.entities.MarcaEntity;
import co.edu.udistrital.mdp.back.services.MarcaService;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MarcaDTO> findAll() {
        List<MarcaEntity> marcas = marcaService.getAllMarcas();
        
        // CORRECCIÓN: Mapear a List<MarcaDTO> ya que la firma del método es List<MarcaDTO>
        return modelMapper.map(marcas, new TypeToken<List<MarcaDTO>>() {
        }.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MarcaDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        MarcaEntity marcaEntity = marcaService.getMarcaById(id);
        
        return modelMapper.map(marcaEntity, MarcaDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MarcaDTO create(@RequestBody MarcaDTO marcaDTO) throws IllegalOperationException, EntityNotFoundException {
        MarcaEntity marcaToCreate = modelMapper.map(marcaDTO, MarcaEntity.class);
        
        MarcaEntity marcaCreadaEntity = marcaService.createMarca(marcaToCreate);
        
        return modelMapper.map(marcaCreadaEntity, MarcaDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MarcaDTO update(@PathVariable Long id, @RequestBody MarcaDTO marcaDTO)
            throws EntityNotFoundException, IllegalOperationException {
        
        MarcaEntity marcaDetailsEntity = modelMapper.map(marcaDTO, MarcaEntity.class);
        
        MarcaEntity marcaActualizadaEntity = marcaService.updateMarca(id, marcaDetailsEntity);
        
        return modelMapper.map(marcaActualizadaEntity, MarcaDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        marcaService.deleteMarca(id);
    }
}