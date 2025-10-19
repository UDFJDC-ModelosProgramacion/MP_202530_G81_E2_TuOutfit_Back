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

import co.edu.udistrital.mdp.back.dto.ComentarioDTO;
import co.edu.udistrital.mdp.back.dto.UsuarioDTO;
import co.edu.udistrital.mdp.back.entities.ComentarioEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.ComentarioService;

/**
 * Clase que implementa el recurso "comentarios".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/comentarios")
public class ComentarioController {
    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private ModelMapper modelMapper;

    /**
	 * Crea un nuevo comentario con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param usaurioId El ID del usaurio que agrega el comentario
	 * @param comentario {@link ComentarioDTO} - El comentario que se desea guardar.
	 * @return JSON {@link ComentarioDTO} - El comentario guardado con el atributo id
	 *         autogenerado.
     * @throws IllegalOperationException 
	 */
	@PostMapping(value = "/{usuarioId}/comentarios")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ComentarioDTO createReview(@PathVariable Long usuarioId, @RequestBody ComentarioDTO comentario)
			throws EntityNotFoundException, IllegalOperationException {
		ComentarioEntity comentarioEnity = modelMapper.map(comentario, ComentarioEntity.class);
		ComentarioEntity newComentario = comentarioService.createComentario(usuarioId, comentarioEnity);
		return modelMapper.map(newComentario, ComentarioDTO.class);
	}

    /**
	 * Busca y devuelve todos los comentarios que existen de un usuario.
	 *
	 * @param usuarioId El ID del usaurio del cual se buscan los comentarios
	 * @return JSONArray {@link ComentarioDTO} - Los comentarios encontradas del usuario. Si
	 *         no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{usuarioId}/comentarios")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ComentarioDTO> getComentarios(@PathVariable Long usuarioId) throws EntityNotFoundException {
		List<ComentarioEntity> comentarios = comentarioService.getComentarios(usuarioId);
		return modelMapper.map(comentarios, new TypeToken<List<ComentarioDTO>>() {
		}.getType());
	}

    /**
	 * Busca y devuelve el comentario con el ID recibido en la URL, relativa a un usuario.
	 *
	 * @param usuarioId   El ID del usuario del cual se buscan los comentarios
	 * @param comentarioId El ID del comentario que se busca
	 * @return {@link UsuarioDTO} - El comentario encontrado en el usuario.
	 */
	@GetMapping(value = "/{usuarioId}/comentarios/{comentarioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDTO getComentario(@PathVariable Long usuarioId, @PathVariable Long comentarioId)
			throws EntityNotFoundException {
		ComentarioEntity entity = comentarioService.getComentario(usuarioId, comentarioId);
		return modelMapper.map(entity, ComentarioDTO.class);
	}

    /**
	 * Actualiza un comentario con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa el objeto actualizado.
	 *
	 * @param usuarioId   El ID del usaurio del cual se guarda el comentario
	 * @param comentarioId El ID del comentario que se va a actualizar
	 * @param comentario   {@link ComentarioDTO} - El comentario que se desea guardar.
	 * @return JSON {@link ComentarioDTO} - El comentario actualizado.
	 */
	@PutMapping(value = "/{usaurioId}/comentarios/{comentariosId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDTO updateComentario(@PathVariable Long usuarioId, @PathVariable("comentariosId") Long comentarioId,
			@RequestBody ComentarioDTO comentario) throws EntityNotFoundException {
		ComentarioEntity comentarioEntity = modelMapper.map(comentario, ComentarioEntity.class);
		ComentarioEntity newEntity = comentarioService.updateComentario(usuarioId, comentarioId, comentarioEntity);
		return modelMapper.map(newEntity, ComentarioDTO.class);
	}

    /**
     * Borra el comentario con el id asociado recibido en la URL.
     *
     * @param usuarioId El ID del usuario del cual se va a eliminar el comentario.
     * @param comentarioId El ID del comentario que se va a eliminar.
	 * @throws IllegalOperationException 
     * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} -
     */
	@DeleteMapping(value = "/{usuarioId}/comentarios/{comentarioId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteComentario(@PathVariable Long usuarioId, @PathVariable Long comentarioId)
			throws EntityNotFoundException, IllegalOperationException {
		comentarioService.deleteComentario(usuarioId, comentarioId);
	}
}
