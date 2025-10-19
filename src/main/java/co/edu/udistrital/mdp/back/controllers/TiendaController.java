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

import co.edu.udistrital.mdp.back.dto.TiendaDTO;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.services.TiendaService;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;

@RestController
@RequestMapping("/api/tiendas")
public class TiendaController {

    @Autowired
    private TiendaService tiendaService;

    @Autowired
    private ModelMapper modelMapper;

    // 1. CREATE (POST /api/tiendas)
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public TiendaDTO create(@RequestBody TiendaDTO tiendaDTO) throws IllegalOperationException {

        TiendaEntity tiendaToCreate = modelMapper.map(tiendaDTO, TiendaEntity.class);
        
        TiendaEntity tiendaCreadaEntity = tiendaService.createTienda(tiendaToCreate);
        
        return modelMapper.map(tiendaCreadaEntity, TiendaDTO.class);
    }

    // 2. FIND ALL (GET /api/tiendas)
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TiendaDTO> findAll() {

        List<TiendaEntity> tiendas = tiendaService.getAllTiendas(); 
        
        return modelMapper.map(tiendas, new TypeToken<List<TiendaDTO>>() {
        }.getType());
    }

    // 3. FIND ONE (GET /api/tiendas/{id})
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TiendaDTO findOne(@PathVariable Long id) throws EntityNotFoundException {

        TiendaEntity tiendaEntity = tiendaService.getTiendaById(id);

        return modelMapper.map(tiendaEntity, TiendaDTO.class);
    }

    // 4. UPDATE (PUT /api/tiendas/{id})
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TiendaDTO update(@PathVariable Long id, @RequestBody TiendaDTO tiendaDTO)
            throws EntityNotFoundException, IllegalOperationException {

        TiendaEntity tiendaDetailsEntity = modelMapper.map(tiendaDTO, TiendaEntity.class);
        
        TiendaEntity tiendaActualizadaEntity = tiendaService.updateTienda(id, tiendaDetailsEntity);
        
        return modelMapper.map(tiendaActualizadaEntity, TiendaDTO.class);
    }

    // 5. DELETE (DELETE /api/tiendas/{id})
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        tiendaService.deleteTienda(id);
    }
}