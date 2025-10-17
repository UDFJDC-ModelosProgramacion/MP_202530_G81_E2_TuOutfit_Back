package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.back.entities.ComentarioEntity;
import co.edu.udistrital.mdp.back.entities.ListaDeseosEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.repositories.ComentarioRepository;
import co.edu.udistrital.mdp.back.repositories.ListaDeseosRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service

public class UsuarioService {
    
    @Autowired
	private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ListaDeseosRepository listaDeseosRepository;

    /**
	 * Se encarga de crear un Usuario en la base de datos.
	 *
	 * @param usuario Objeto de UsuarioEntity con los datos nuevos
	 * @return Objeto de UsuarioEntity con los datos nuevos y su ID.
	 * @throws IllegalOperationException 
	 */

    @Transactional
	public UsuarioEntity createUsuario(UsuarioEntity usuario) throws IllegalOperationException {
		log.info("Inicia proceso de creación del usuario");
		if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
    		throw new IllegalOperationException("El nombre no puede estar vacío");
		}
		if(usuario.getCorreo()== null  || usuario.getCorreo().isEmpty()) {
			throw new IllegalOperationException("El correo no puede estar vacio");
	    }
        if(usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
			throw new IllegalOperationException("Ya existe un usuario con ese correo");
	    }
		UsuarioEntity creado = usuarioRepository.save(usuario);
        log.info("Finaliza proceso de creación del usuario con id = {}", creado.getId());
        return creado;
	}

	/**
	 * Obtiene la lista de los registros de Usuario.
	 *
	 * @return Colección de objetos de UsuarioEntity.
	 */
	@Transactional
	public List<UsuarioEntity> getUsuarios() {
		log.info("Inicia proceso de consultar todos los usuarios");
		return usuarioRepository.findAll();
	}

	/**
	 * Obtiene los datos de una instancia de Usuario a partir de su ID.
	 *
	 * @param usuarioId Identificador de la instancia a consultar
	 * @return Instancia de UsuarioEntity con los datos del Usuario consultado.
	 */
	@Transactional
	public UsuarioEntity getUsuario(Long usuarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el usuario con id = {}", usuarioId);
		Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		if (usuarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
		log.info("Termina proceso de consultar el usuario con id = {}", usuarioId);
		return usuarioEntity.get();
	}

	/**
	 * Actualiza la información de una instancia de Usuario.
	 *
	 * @param usuarioId     Identificador de la instancia a actualizar
	 * @param usuarioEntity Instancia de UsuarioEntity con los nuevos datos.
	 * @return Instancia de UsuarioEntity con los datos actualizados.
	 * @throws IllegalOperationException 
	 */
	@Transactional
	public UsuarioEntity updateUsuario(Long usuarioId, UsuarioEntity usuario) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de actualizar el autor con id = {}", usuarioId);
		Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		if (usuarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
		if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
        	throw new IllegalOperationException("El nombre no puede estar vacío");
    	}
    	if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
        	throw new IllegalOperationException("El correo no puede estar vacío");
    	}
		// Verificar que el nuevo correo no esté repetido en otro usuario
		Optional<UsuarioEntity> existingUser = usuarioRepository.findByCorreo(usuario.getCorreo());
		if (existingUser.isPresent() && !existingUser.get().getId().equals(usuarioId)) {
			throw new IllegalOperationException("Ya existe un usuario con ese correo");
		}
		log.info("Termina proceso de actualizar el autor con id = {}", usuarioId);
		usuario.setId(usuarioId);
		return usuarioRepository.save(usuario);
	}

	/**
	 * Elimina una instancia de Usuario de la base de datos.
	 *
	 * @param usuarioId Identificador de la instancia a eliminar.
	 * @throws EntityNotFoundException si el usuario no existe.
	 */
	@Transactional
	public void deleteUsuario(Long usuarioId) throws IllegalOperationException, EntityNotFoundException {
		log.info("Inicia proceso de borrar el usuario con id = {}", usuarioId);
		Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		if (usuarioEntity.isEmpty()) {
        throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        }   
        usuarioRepository.deleteById(usuarioId);
		log.info("Termina proceso de borrar el usuario con id = {}", usuarioId);
	}


    // Relación Usuario - Comentarios

    @Transactional
    public ComentarioEntity addComentario(Long usuarioId, ComentarioEntity comentario)
            throws EntityNotFoundException, IllegalOperationException {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));

        if (comentario.getTexto() == null || comentario.getTexto().isEmpty()) {
            throw new IllegalOperationException("El comentario no puede estar vacío");
        }

        comentario.setUsuario(usuario);
        return comentarioRepository.save(comentario);
    }

    @Transactional
    public List<ComentarioEntity> getComentarios(Long usuarioId) throws EntityNotFoundException {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));
        return usuario.getComentarios();
    }

    
    // Relación Usuario - ListaDeseos

    @Transactional
    public ListaDeseosEntity setListaDeseos(Long usuarioId, ListaDeseosEntity listaDeseos)
        throws EntityNotFoundException {
    log.info("Inicia proceso de asignar lista de deseos al usuario con id = {}", usuarioId);

    UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));

    listaDeseos.setUsuario(usuario);
    usuario.setWishlist(listaDeseos);

    log.info("Finaliza proceso de asignar lista de deseos al usuario con id = {}", usuarioId);
    return listaDeseosRepository.save(listaDeseos);
    }

    @Transactional
    public ListaDeseosEntity getListaDeseos(Long usuarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar lista de deseos del usuario con id = {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));

        log.info("Finaliza proceso de consultar lista de deseos del usuario con id = {}", usuarioId);
        return usuario.getWishlist();
    }
}
