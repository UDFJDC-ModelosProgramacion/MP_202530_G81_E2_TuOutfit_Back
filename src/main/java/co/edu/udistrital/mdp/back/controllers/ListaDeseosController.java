package co.edu.udistrital.mdp.back.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.back.dto.ListaDeseosDTO;
import co.edu.udistrital.mdp.back.dto.ListaDeseosDetailDTO;
import co.edu.udistrital.mdp.back.entities.ListaDeseosEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.ListaDeseosService;

/**
 * Clase que implementa el recurso "books".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/listasdeseos")
public class ListaDeseosController {
    @Autowired
	private ListaDeseosService listaService;

	@Autowired
	private ModelMapper modelMapper;

    /**
	 * Crea una nueva lista de deseos con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param listadeseos {@link ListaDeseosDTO} - La lista de deseos que se desea guardar.
	 * @return JSON {@link ListaDeseosDTO} - La lista de deseos guardada con el atributo id
	 *         autogenerado.
	 */
	@PostMapping("/usuarios/{usuarioId}/listas")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ListaDeseosDTO create(@PathVariable Long usuarioId, @RequestBody ListaDeseosDTO listadeseosDTO) throws IllegalOperationException, EntityNotFoundException {
		ListaDeseosEntity listadeseosEntity = listaService.createListaDeseos(usuarioId, modelMapper.map(listadeseosDTO, ListaDeseosEntity.class));
		return modelMapper.map(listadeseosEntity, ListaDeseosDTO.class);
	}

	/**
	 * Busca y devuelve todas las listas de deseos que existen en la aplicacion.
	 *
	 * @return JSONArray {@link ListaDeseosDetailDTO} - Las listas de deseos encontradas en la
	 *         aplicación. Si no hay ninguna retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<ListaDeseosDetailDTO> findAll() {
		List<ListaDeseosEntity> listasdeseos = listaService.getListaDeseos();
		return modelMapper.map(listasdeseos, new TypeToken<List<ListaDeseosDetailDTO>>() {
		}.getType());
	}

    /**
	 * Busca la lista de deseos con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param listaDeseosId Identificador del lista de deseos que se esta buscando. Este debe ser una
	 *               cadena de dígitos.
	 * @return JSON {@link ListaDeseosDetailDTO} - La lista de deseos buscada
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ListaDeseosDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
		ListaDeseosEntity listadeseosEntity = listaService.getListaDeseos(id);
		return modelMapper.map(listadeseosEntity, ListaDeseosDetailDTO.class);
	}

    /**
	 * Actualiza la lista de deseos con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param listadeseosId Identificador de la lista de deseos que se desea actualizar. Este debe ser
	 *               una cadena de dígitos.
	 * @param listadeseos   {@link ListaDeseosDTO} La lista de deseos que se desea guardar.
	 * @return JSON {@link ListaDeseosDTO} - La lista de deseos guardada.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ListaDeseosDTO update(@PathVariable Long id, @RequestBody ListaDeseosDTO listadeseosDTO)
			throws EntityNotFoundException, IllegalOperationException {
		ListaDeseosEntity listadeseosEntity = listaService.updateListaDeseos(id, modelMapper.map(listadeseosDTO, ListaDeseosEntity.class));
		return modelMapper.map(listadeseosEntity, ListaDeseosDTO.class);
	}
    
}
