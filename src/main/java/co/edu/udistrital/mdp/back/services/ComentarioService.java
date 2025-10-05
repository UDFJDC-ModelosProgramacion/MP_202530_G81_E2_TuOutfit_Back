package co.edu.udistrital.mdp.back.services;

import java.util.List;

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
     * Crea un comentario y lo asocia a un usuario.
     *
     * @param usuarioId   Identificador del usuario
     * @param comentario  Comentario a crear
     * @return Comentario creado
     * @throws EntityNotFoundException    si el usuario no existe
     * @throws IllegalOperationException  si el comentario no tiene texto
     */

    @Transactional
    public ComentarioEntity createComentario(Long usuarioId, ComentarioEntity comentario)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de creación de comentario para usuario con id = {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));

        if (comentario.getTexto() == null || comentario.getTexto().isEmpty()) {
            throw new IllegalOperationException("El comentario no puede estar vacío");
        }

        comentario.setUsuario(usuario);
        ComentarioEntity saved = comentarioRepository.save(comentario);

        log.info("Finaliza proceso de creación de comentario con id = {} para usuario con id = {}", saved.getId(), usuarioId);
        return saved;
    }

	/**
	 * Obtiene la lista de los registros de Comentario.
	 *
	 * @return Colección de objetos de ComentarioEntity.
	 */
	@Transactional
	public List<ComentarioEntity> getComentarios() {
		log.info("Inicia proceso de consultar todos los comentarios");
		return comentarioRepository.findAll();
	}

	/**
	 * Obtiene los datos de una instancia de Comentario a partir de su ID.
	 *
	 * @param comentarioId Identificador de la instancia a consultar
	 * @return Instancia de ComentarioEntity con los datos del Comentario consultado.
	 */
	@Transactional
    public ComentarioEntity getComentario(Long comentarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar comentario con id = {}", comentarioId);
        return comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND));
    }

	/**
	 * Actualiza la información de una instancia de Comentario.
	 *
	 * @param comentarioId     Identificador de la instancia a actualizar
	 * @param comentarioEntity Instancia de ComentarioEntity con los nuevos datos.
	 * @return Instancia de ComentarioEntity con los datos actualizados.
	 */
	@Transactional
    public ComentarioEntity updateComentario(Long comentarioId, ComentarioEntity comentario)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de actualizar comentario con id = {}", comentarioId);

        ComentarioEntity entity = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND));

        if (comentario.getTexto() == null || comentario.getTexto().isEmpty()) {
            throw new IllegalOperationException("El comentario no puede estar vacío");
        }

        entity.setTexto(comentario.getTexto());
        ComentarioEntity updated = comentarioRepository.save(entity);

        log.info("Finaliza proceso de actualizar comentario con id = {}", comentarioId);
        return updated;
    }

	/**
	 * Elimina una instancia de Comentario de la base de datos.
	 *
	 * @param comentarioId Identificador de la instancia a eliminar.
	 * @throws EntityNotFoundException si el Comentario no existe.
	 */
    
	@Transactional
    public void deleteComentario(Long comentarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de eliminar comentario con id = {}", comentarioId);

        ComentarioEntity entity = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND));

        comentarioRepository.delete(entity);

        log.info("Finaliza proceso de eliminar comentario con id = {}", comentarioId);
    }
}
