package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.back.entities.ComentarioEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.repositories.ComentarioRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service

public class ComentarioService {
    @Autowired
	private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
	 * Se encarga de crear un Comentario en la base de datos.
	 *
	 * @param comentarioEntity Objeto de ComentarioEntity con los datos nuevos
	 * @param usuarioId       id del Usuario el cual sera padre del nuevo Comentario.
	 * @return Objeto de ComentarioEntity con los datos nuevos y su ID.
	 * @throws EntityNotFoundException si el usuario no existe.
	 *
	 */
	@Transactional
	public ComentarioEntity createComentario(Long usuarioId, ComentarioEntity comentarioEntity) throws EntityNotFoundException {
		log.info("Inicia proceso de crear comentario");
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		if (usuarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		comentarioEntity.setUsuario(usuarioEntity.get());

		log.info("Termina proceso de creación del comentario");
		return comentarioRepository.save(comentarioEntity);
	}

	/**
	 * Obtiene la lista de los registros de Comentario que pertenecen a un Usuario.
	 *
	 * @param usuarioId id del Usuario el cual es padre de los Comentarios.
	 * @return Colección de objetos de ComentarioEntity.
	 */

	@Transactional
	public List<ComentarioEntity> getComentarios(Long usuarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar los comentarios asociados al usaurio con id = {0}", usuarioId);
		Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		if (usuarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		log.info("Termina proceso de consultar los comentarios asociados al usuario con id = {0}", usuarioId);
		return usuarioEntity.get().getComentarios();
	}

	/**
	 * Obtiene los datos de una instancia de Comentario a partir de su ID. La existencia
	 * del elemento padre Usuaro se debe garantizar.
	 *
	 * @param usuarioId   El id del Usuario buscado
	 * @param comentarioId Identificador del Comentario a consultar
	 * @return Instancia de ComentarioEntity con los datos del Comentario consultado.
	 *
	 */
	@Transactional
	public ComentarioEntity getComentario(Long usuarioId, Long comentarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el comentario con id = {0} del usuario con id = " + usuarioId,
				comentarioId);
		Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		if (usuarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		log.info("Termina proceso de consultar el comentario con id = {0} del usuario con id = " + usuarioId,
				comentarioId);
		return comentarioRepository.findByUsuarioIdAndId(usuarioId, comentarioId);
	}

	/**
	 * Actualiza la información de una instancia de Comentario.
	 *
	 * @param comentarioEntity Instancia de ComentarioEntity con los nuevos datos.
	 * @param usuarioId       id del Usuario el cual sera padre del Comentario actualizado.
	 * @param comentarioId     id del comentario que será actualizada.
	 * @return Instancia de ComentarioEntity con los datos actualizados.
	 *
	 */
	@Transactional
	public ComentarioEntity updateComentario(Long usuarioId, Long comentarioId, ComentarioEntity comentario) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar el comentario con id = {0} del usuario con id = " + usuarioId,
				comentarioId);
		Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		if (usuarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		comentario.setId(comentarioId);
		comentario.setUsuario(usuarioEntity.get());
		log.info("Termina proceso de actualizar el comentario con id = {0} del usuario con id = " + usuarioId,
				usuarioId);
		return comentarioRepository.save(comentario);
	}

	/**
	 * Elimina una instancia de Comentario de la base de datos.
	 *
	 * @param comentarioId Identificador de la instancia a eliminar.
	 * @param usuarioId   id del Usuario el cual es padre del Comentario.
	 * @throws EntityNotFoundException Si el comentario no esta asociado al usuario.
	 * @throws IllegalOperationException 
	 *
	 */
	@Transactional
	public void deleteComentario(Long usuarioId, Long comentarioId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar el comentario con id = {0} del usuario con id = " + usuarioId,
				comentarioId);
		Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		if (usuarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
		
		if(!comentarioEntity.get().getUsuario().getId().equals(usuarioId))
			throw new IllegalOperationException(ErrorMessage.COMENTARIO_NO_ASOCIADO_A_USUARIO);
		
		comentarioRepository.deleteById(comentarioId);
		log.info("Termina proceso de borrar el comentario con id = {0} del usuario con id = " + usuarioId,
				comentarioId);
	}
}
