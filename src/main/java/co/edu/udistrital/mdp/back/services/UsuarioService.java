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
     * Crea un nuevo Usuario en la base de datos.
     *
     * @param usuario Objeto de UsuarioEntity con los datos nuevos.
     * @return UsuarioEntity creado con su ID.
     * @throws IllegalOperationException si el nombre o correo son inválidos o si ya existe un usuario con ese correo.
     */
    @Transactional
    public UsuarioEntity createUsuario(UsuarioEntity usuario) throws IllegalOperationException {
        log.info("Inicia proceso de creación del usuario");

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre no puede estar vacío");
        }

        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new IllegalOperationException("El correo no puede estar vacío");
        }

        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new IllegalOperationException("Ya existe un usuario con ese correo");
        }

        UsuarioEntity creado = usuarioRepository.save(usuario);
        log.info("Finaliza proceso de creación del usuario con id = {}", creado.getId());
        return creado;
    }

	/**
     * Obtiene todos los usuarios registrados.
     *
     * @return Lista de UsuarioEntity.
     */
    @Transactional
    public List<UsuarioEntity> getUsuarios() {
        log.info("Inicia proceso de consultar todos los usuarios");
        return usuarioRepository.findAll();
    }

	/**
     * Obtiene un usuario por su ID.
     *
     * @param usuarioId ID del usuario a consultar.
     * @return UsuarioEntity encontrado.
     * @throws EntityNotFoundException si no existe el usuario.
     */
    @Transactional
    public UsuarioEntity getUsuario(Long usuarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el usuario con id = {}", usuarioId);
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);

        if (usuarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        }

        log.info("Termina proceso de consultar el usuario con id = {}", usuarioId);
        return usuarioEntity.get();
    }

	/**
     * Actualiza la información de un usuario existente.
     *
     * @param usuarioId ID del usuario a actualizar.
     * @param usuario   Datos nuevos del usuario.
     * @return UsuarioEntity actualizado.
     * @throws EntityNotFoundException si no existe el usuario.
     * @throws IllegalOperationException si los datos son inválidos o el correo está duplicado.
     */
    @Transactional
    public UsuarioEntity updateUsuario(Long usuarioId, UsuarioEntity usuario)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar el usuario con id = {}", usuarioId);

        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
        if (usuarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        }

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre no puede estar vacío");
        }

        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new IllegalOperationException("El correo no puede estar vacío");
        }

        // Verificar duplicidad de correo
        Optional<UsuarioEntity> existingUser = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(usuarioId)) {
            throw new IllegalOperationException("Ya existe un usuario con ese correo");
        }

        usuario.setId(usuarioId);
        UsuarioEntity actualizado = usuarioRepository.save(usuario);
        log.info("Finaliza proceso de actualizar el usuario con id = {}", usuarioId);
        return actualizado;
    }

	/**
     * Elimina un usuario por su ID.
     *
     * @param usuarioId ID del usuario a eliminar.
     * @throws EntityNotFoundException si el usuario no existe.
     */
    @Transactional
    public void deleteUsuario(Long usuarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de eliminar el usuario con id = {}", usuarioId);

        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
        if (usuarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        }

        usuarioRepository.deleteById(usuarioId);
        log.info("Finaliza proceso de eliminar el usuario con id = {}", usuarioId);
    }


    // Relación Usuario - Comentarios

    /**
     * Asocia un comentario a un usuario.
     *
     * @param usuarioId ID del usuario.
     * @param comentario Comentario a agregar.
     * @return ComentarioEntity creado.
     * @throws EntityNotFoundException si no se encuentra el usuario.
     * @throws IllegalOperationException si el texto del comentario está vacío.
     */
    @Transactional
    public ComentarioEntity addComentario(Long usuarioId, ComentarioEntity comentario)
            throws EntityNotFoundException, IllegalOperationException {

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));

        if (comentario.getTexto() == null || comentario.getTexto().trim().isEmpty()) {
            throw new IllegalOperationException("El comentario no puede estar vacío");
        }

        comentario.setUsuario(usuario);
        ComentarioEntity creado = comentarioRepository.save(comentario);
        log.info("Comentario agregado al usuario con id = {}", usuarioId);
        return creado;
    }

	/**
     * Obtiene todos los comentarios de un usuario.
     *
     * @param usuarioId ID del usuario.
     * @return Lista de comentarios.
     * @throws EntityNotFoundException si no existe el usuario.
     */
    @Transactional
    public List<ComentarioEntity> getComentarios(Long usuarioId) throws EntityNotFoundException {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));
        return usuario.getComentarios();
    }

    
    // Relación Usuario - ListaDeseos

    /**
     * Asigna una lista de deseos a un usuario.
     *
     * @param usuarioId ID del usuario.
     * @param listaDeseos Lista de deseos a asignar.
     * @return ListaDeseosEntity asociada.
     * @throws EntityNotFoundException si el usuario no existe.
     */
    @Transactional
    public ListaDeseosEntity setListaDeseos(Long usuarioId, ListaDeseosEntity listaDeseos)
            throws EntityNotFoundException {

        log.info("Inicia proceso de asignar lista de deseos al usuario con id = {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));

        listaDeseos.setUsuario(usuario);
        usuario.setWishlist(listaDeseos);

        ListaDeseosEntity creada = listaDeseosRepository.save(listaDeseos);
        log.info("Finaliza proceso de asignar lista de deseos al usuario con id = {}", usuarioId);
        return creada;
    }

    /**
     * Obtiene la lista de deseos de un usuario.
     *
     * @param usuarioId ID del usuario.
     * @return ListaDeseosEntity asociada.
     * @throws EntityNotFoundException si el usuario no existe.
     */
    @Transactional
    public ListaDeseosEntity getListaDeseos(Long usuarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar lista de deseos del usuario con id = {}", usuarioId);

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));

        log.info("Finaliza proceso de consultar lista de deseos del usuario con id = {}", usuarioId);
        return usuario.getWishlist();
    }
}
