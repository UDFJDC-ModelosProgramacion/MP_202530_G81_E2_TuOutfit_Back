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

import co.edu.udistrital.mdp.back.dto.ListaDeseosDTO;
import co.edu.udistrital.mdp.back.dto.ListaDeseosDetailDTO;
import co.edu.udistrital.mdp.back.entities.ListaDeseosEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.OutfitListaDeseosService;

/**
 * Clase que implementa el recurso "outfits/{id}/listasdeseos".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/outfits")
public class OutfitListaDeseosController {

    @Autowired
	private OutfitListaDeseosService outfitListaService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Asocia una lista de deseos existente con un outfit existente
	 *
	 * @param listaDeseosId El ID de la lista de deseos que se va a asociar
	 * @param outfitId   El ID del outfit al cual se le va a asociar la lista de deseos
	 * @return JSON {@link ListaDeseosDetailDTO} - La lista de deseos asociada.
	 * @throws IllegalOperationException 
	 */
	@PostMapping(value = "/{outfitId}/listasdeseos/{listaDeseosId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ListaDeseosDetailDTO addListaDeseos(@PathVariable Long listaDeseosId, @PathVariable Long outfitId)
			throws EntityNotFoundException, IllegalOperationException {
		ListaDeseosEntity listadeseosEntity = outfitListaService.addListaDeseos(outfitId, listaDeseosId);
		return modelMapper.map(listadeseosEntity, ListaDeseosDetailDTO.class);
	}

    /**
	 * Busca y devuelve la lista de deseos con el ID recibido en la URL, relativo a un outfit.
	 *
	 * @param listaDeseosId El ID de la lista de deseos que se busca
	 * @param outfitId   El ID de la lista de deseos de la cual se busca el outfit
	 * @return {@link ListaDeseosDetailDTO} - La lista de deseos encontrada en el outfit.
	 */
	@GetMapping(value = "/{outfitId}/listasdeseos/{listaDeseosId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ListaDeseosDetailDTO getListaDeseos(@PathVariable Long listaDeseosId, @PathVariable Long outfitId)
			throws EntityNotFoundException, IllegalOperationException {
		ListaDeseosEntity listadeseosEntity = outfitListaService.getListaDeseos(outfitId, listaDeseosId);
		return modelMapper.map(listadeseosEntity, ListaDeseosDetailDTO.class);
	}

    /**
	 * Actualiza la lista de listas de deseos de un outfit con la lista que se recibe en el
	 * cuerpo.
	 *
	 * @param outfitId  El ID del outfit al cual se le va a asociar la lista de listas de deseos
	 * @param listasdeseos JSONArray {@link ListaDeseosDTO} - La lista de listas de deseos que se desea
	 *                guardar.
	 * @return JSONArray {@link ListaDeseosDetailDTO} - La lista actualizada.
	 */
	@PutMapping(value = "/{outfitId}/listasdeseos")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ListaDeseosDetailDTO> addListasDeseos(@PathVariable Long outfitId, @RequestBody List<ListaDeseosDTO> listasdeseos)
			throws EntityNotFoundException {
		List<ListaDeseosEntity> entities = modelMapper.map(listasdeseos, new TypeToken<List<ListaDeseosEntity>>() {
		}.getType());
		List<ListaDeseosEntity> listasdeseosList = outfitListaService.replaceListasDeseos(outfitId, entities);
		return modelMapper.map(listasdeseosList, new TypeToken<List<ListaDeseosDetailDTO>>() {
		}.getType());
	}

    /**
	 * Busca y devuelve todos las listas de deseos que existen en un outfit.
	 *
	 * @param outfitsId El ID de la lista de deseos del cual se buscan los outfits
	 * @return JSONArray {@link ListaDeseosDetailDTO} - Las listas de deseos encontradas en el
	 *         outfit. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{outfitId}/listasdeseos")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ListaDeseosDetailDTO> getListasDeseos(@PathVariable Long outfitId) throws EntityNotFoundException {
		List<ListaDeseosEntity> listadeseosEntity = outfitListaService.getListasDeseos(outfitId);
		return modelMapper.map(listadeseosEntity, new TypeToken<List<ListaDeseosDetailDTO>>() {
		}.getType());
	}

    /**
	 * Elimina la conexión entre la lista de deseos y el outfit recibidos en la URL.
	 *
	 * @param outfitId   El ID del outfit al cual se le va a desasociar la lista de deseos
	 * @param listaDeseosId El ID de la lista de deseos que se desasocia
	 */
	@DeleteMapping(value = "/{outfitId}/listasdeseos/{listaDeseosId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeListaDeseos(@PathVariable Long listaDeseosId, @PathVariable Long outfitId)
			throws EntityNotFoundException {
		outfitListaService.removeListaDeseos(outfitId, listaDeseosId);
	}
}
