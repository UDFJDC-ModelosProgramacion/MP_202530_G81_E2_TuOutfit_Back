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

import co.edu.udistrital.mdp.back.dto.UsuarioDTO;
import co.edu.udistrital.mdp.back.dto.UsuarioDetailDTO;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.services.UsuarioService;

/**
 * Clase que implementa el recurso "usuarios".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/usuarios")

public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;

	/**
	 * Crea un nuevo usuario con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param usuario {@link UsuarioDTO} - EL usuario que se desea guardar.
	 * @return JSON {@link UsuarioDTO} - El usuario guardado con el atributo id
	 *         autogenerado.
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public UsuarioDTO create(@RequestBody UsuarioDTO usuarioDTO) throws IllegalOperationException {
		UsuarioEntity usuarioEntity = usuarioService.createUsuario(modelMapper.map(usuarioDTO, UsuarioEntity.class));
		return modelMapper.map(usuarioEntity, UsuarioDTO.class);
	}

    /**
	 * Busca y devuelve todos los usuarios que existen en la aplicacion.
	 *
	 * @return JSONArray {@link UsuarioDetailDTO} - Los usuarios encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<UsuarioDetailDTO> findAll() {
		List<UsuarioEntity> usuarios = usuarioService.getUsuarios();
		return modelMapper.map(usuarios, new TypeToken<List<UsuarioDetailDTO>>() {
		}.getType());
	}

    /**
	 * Busca el usuario con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param usuarioId Identificador del usuario que se esta buscando. Este debe ser una
	 *               cadena de dígitos.
	 * @return JSON {@link UsuarioDetailDTO} - El usuario buscado
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public UsuarioDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
		UsuarioEntity usuarioEntity = usuarioService.getUsuario(id);
		return modelMapper.map(usuarioEntity, UsuarioDetailDTO.class);
	}

    /**
	 * Actualiza el usuario con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param usuarioId Identificador del usuario que se desea actualizar. Este debe ser
	 *               una cadena de dígitos.
	 * @param usuario   {@link UsuarioDTO} El usuario que se desea guardar.
	 * @return JSON {@link UsuarioDTO} - El usuario guardado.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public UsuarioDTO update(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO)
			throws EntityNotFoundException, IllegalOperationException {
		UsuarioEntity usuarioEntity = usuarioService.updateUsuario(id, modelMapper.map(usuarioDTO, UsuarioEntity.class));
		return modelMapper.map(usuarioEntity, UsuarioDTO.class);
	}

    /**
	 * Borra el usuario con el id asociado recibido en la URL.
	 *
	 * @param usuarioId Identificador del usuario que se desea borrar. Este debe ser una
	 *               cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
		usuarioService.deleteUsuario(id);
	}
}
