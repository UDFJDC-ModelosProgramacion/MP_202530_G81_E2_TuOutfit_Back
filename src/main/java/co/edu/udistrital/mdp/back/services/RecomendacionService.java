package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.entities.RecomendacionEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import co.edu.udistrital.mdp.back.repositories.RecomendacionRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service

public class RecomendacionService {

     @Autowired
    private RecomendacionRepository recomendacionRepository;

     @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OutfitRepository outfitRepository;
    
    /**
     * Crear una nueva recomendación
     */
    @Transactional
    public RecomendacionEntity createRecomendacion(RecomendacionEntity recomendacionEntity)
        throws EntityNotFoundException, IllegalOperationException {

    log.info("Inicia proceso de creación de recomendación");

    if (recomendacionEntity.getUsuario() == null || recomendacionEntity.getUsuario().getId() == null) {
        throw new IllegalOperationException("Usuario is not valid");
    }
    if (recomendacionEntity.getOutfit() == null || recomendacionEntity.getOutfit().getId() == null) {
        throw new IllegalOperationException("Outfit is not valid");
    }

    UsuarioEntity usuario = usuarioRepository.findById(recomendacionEntity.getUsuario().getId())
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));

    OutfitEntity outfit = outfitRepository.findById(recomendacionEntity.getOutfit().getId())
            .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));

    // 🔹 Validar duplicados por usuario + outfit
    if (recomendacionRepository.existsByUsuarioAndOutfit(usuario, outfit)) {
        throw new IllegalOperationException("Ya existe una recomendación con el mismo usuario y outfit");
    }

    recomendacionEntity.setUsuario(usuario);
    recomendacionEntity.setOutfit(outfit);

    RecomendacionEntity saved = recomendacionRepository.save(recomendacionEntity);

    log.info("Finaliza proceso de creación de recomendación con id = {}", saved.getId());
    return saved;
}
     /**
     * Consultar todas las recomendaciones
     */
    @Transactional(readOnly = true)
    public List<RecomendacionEntity> getRecomendaciones() {
        log.info("Inicia proceso de consultar todas las recomendaciones");
        return recomendacionRepository.findAll();
    }

    /**
     * Consultar una recomendación por ID
     */
    @Transactional(readOnly = true)
    public RecomendacionEntity getRecomendacion(Long recomendacionId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar recomendación con id = {}", recomendacionId);
        return recomendacionRepository.findById(recomendacionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.RECOMENDACION_NOT_FOUND));
    }

    /**
     * Actualizar una recomendación 
     */
    @Transactional
    public RecomendacionEntity updateRecomendacion(Long recomendacionId, RecomendacionEntity recomendacionEntity)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de actualización de recomendación con id = {}", recomendacionId);

        RecomendacionEntity existing = recomendacionRepository.findById(recomendacionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.RECOMENDACION_NOT_FOUND));

        //  No se puede cambiar el usuario
        recomendacionEntity.setUsuario(existing.getUsuario());

        if (recomendacionEntity.getOutfit() != null && recomendacionEntity.getOutfit().getId() != null) {
            OutfitEntity outfit = outfitRepository.findById(recomendacionEntity.getOutfit().getId())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));
            recomendacionEntity.setOutfit(outfit);
        } else {
            recomendacionEntity.setOutfit(existing.getOutfit());
        }

        recomendacionEntity.setId(recomendacionId);
        RecomendacionEntity updated = recomendacionRepository.save(recomendacionEntity);

        log.info("Finaliza proceso de actualización de recomendación con id = {}", recomendacionId);
        return updated;
    }

    /**
     * Eliminar una recomendación 
     */
    @Transactional
    public void deleteRecomendacion(Long recomendacionId, Long usuarioIdSolicitante)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de eliminación de recomendación con id = {}", recomendacionId);

        RecomendacionEntity recomendacion = recomendacionRepository.findById(recomendacionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.RECOMENDACION_NOT_FOUND));

        UsuarioEntity solicitante = usuarioRepository.findById(usuarioIdSolicitante)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));

        //  Solo creador o ADMIN
        if (!solicitante.getId().equals(recomendacion.getUsuario().getId())) {
            throw new IllegalOperationException("Solo el creador puede eliminar esta recomendación");
        }

        recomendacionRepository.deleteById(recomendacionId);

        log.info("Finaliza proceso de eliminación de recomendación con id = {}", recomendacionId);
    }
}