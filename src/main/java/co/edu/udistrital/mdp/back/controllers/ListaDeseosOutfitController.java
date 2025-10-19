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

import co.edu.udistrital.mdp.back.dto.OutfitDTO;
import co.edu.udistrital.mdp.back.dto.OutfitDetailDTO;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.ListaDeseosOutfitService;

/**
 * Clase que implementa el recurso "listasdeseos/{id}/outfits".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/listasdeseos")
public class ListaDeseosOutfitController {
    
    @Autowired
	private ListaDeseosOutfitService listaOutfitsService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Busca y devuelve el outfit con el ID recibido en la URL, relativo a una lista de deseos.
	 *
	 * @param listaDeseosId El ID de la lista de deseos del cual se busca el outfit
	 * @param outfitId   El ID del outfit que se busca
	 * @return {@link OutfitDetailDTO} - El outfit encontrado en la lista de deseos.
	 */
	@GetMapping(value = "/{listadeseosId}/outfits/{outfitId}")
	@ResponseStatus(code = HttpStatus.OK)
	public OutfitDetailDTO getOutfit(@PathVariable Long listaDeseosId, @PathVariable Long outfitId)
			throws EntityNotFoundException, IllegalOperationException {
		OutfitEntity outfitEntity = listaOutfitsService.getOutfit(listaDeseosId, outfitId);
		return modelMapper.map(outfitEntity, OutfitDetailDTO.class);
	}

    /**
	 * Busca y devuelve todos los outfits que existen en una lista de deseos.
	 *
	 * @param listasDeseosId El ID de la lista de deseos del cual se buscan los outfits
	 * @return JSONArray {@link OutfitDetailDTO} - Los outfits encontrados en la lista de deseos.
	 *         Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{listadeseosId}/outfits")
	@ResponseStatus(code = HttpStatus.OK)
	public List<OutfitDetailDTO> getOutfits(@PathVariable Long listaDeseosId) throws EntityNotFoundException {
		List<OutfitEntity> outfitEntity = listaOutfitsService.getOutfits(listaDeseosId);
		return modelMapper.map(outfitEntity, new TypeToken<List<OutfitDetailDTO>>() {
		}.getType());
	}

    /**
	 * Asocia un outfit existente con una lista de deseos existente
	 *
	 * @param listaDeseosId El ID de la lista de deseos a la cual se le va a asociar el outfit
	 * @param outfitId   El ID del outfit que se asocia
	 * @return JSON {@link OutfitDetailDTO} - El outfit asociado.
	 */
	@PostMapping(value = "/{authorId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.OK)
	public OutfitDetailDTO addBook(@PathVariable Long listaDeseosId, @PathVariable Long outfitId)
			throws EntityNotFoundException {
		OutfitEntity outfitEntity = listaOutfitsService.addOutfit(listaDeseosId, outfitId);
		return modelMapper.map(outfitEntity, OutfitDetailDTO.class);
	}

    /**
	 * Actualiza la lista de outfits de una lista de deseos con la lista que se recibe en el
	 * cuerpo
	 *
	 * @param listaDeseosId El ID de la lista de deseos a la cual se le va a asociar el outfit
	 * @param outfits    JSONArray {@link OutfitDTO} - La lista de outfits que se desea
	 *                 guardar.
	 * @return JSONArray {@link OutfitDetailDTO} - La lista actualizada.
	 */
	@PutMapping(value = "/{listadeseosId}/outfits")
	@ResponseStatus(code = HttpStatus.OK)
	public List<OutfitDetailDTO> replaceOutfits(@PathVariable Long listaDeseosId, @RequestBody List<OutfitDTO> outfits)
			throws EntityNotFoundException {
		List<OutfitEntity> entities = modelMapper.map(outfits, new TypeToken<List<OutfitEntity>>() {
		}.getType());
		List<OutfitEntity> outfitsList = listaOutfitsService.addOutfits(listaDeseosId, entities);
		return modelMapper.map(outfitsList, new TypeToken<List<OutfitDetailDTO>>() {
		}.getType());
	}

    /**
	 * Elimina la conexión entre el outfit y la lista de deseos recibidos en la URL.
	 *
	 * @param listaDeseosId El ID de la lista de deseos a la cual se le va a desasociar el outfit
	 * @param outfitId   El ID del outfit que se desasocia
	 */
	@DeleteMapping(value = "/{listadeseosId}/outfits/{outfitId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeOutfit(@PathVariable Long listaDeseosId, @PathVariable Long outfitId)
			throws EntityNotFoundException {
		listaOutfitsService.removeOutfit(listaDeseosId, outfitId);
	}
}
