package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.OcasionEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.OcasionRepository;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OcasionService {

    @Autowired
    private OcasionRepository ocasionRepository;

    @Autowired
    private OutfitRepository outfitRepository;

    /**
     * Crear una nueva ocasión
     */
    @Transactional
    public OcasionEntity createOcasion(final OcasionEntity ocasionEntity)
            throws IllegalOperationException {

        log.info("Inicia proceso de creación de ocasión");

        validarOcasion(ocasionEntity);

        // Evitar duplicados por nombre
        if (ocasionRepository.existsByNombreIgnoreCase(ocasionEntity.getNombre())) {
            throw new IllegalOperationException("Ya existe una ocasión con ese nombre");
        }

        OcasionEntity saved = ocasionRepository.save(ocasionEntity);
        log.info("Finaliza proceso de creación de ocasión con id = {}", saved.getId());
        return saved;
    }

    /**
     * Consultar todas las ocasiones
     */
    @Transactional(readOnly = true)
    public List<OcasionEntity> getOcasiones() {
        log.info("Inicia proceso de consulta de todas las ocasiones");
        List<OcasionEntity> ocasiones = ocasionRepository.findAll();
        log.info("Finaliza proceso de consulta de todas las ocasiones ({} encontradas)", ocasiones.size());
        return ocasiones;
    }

    /**
     * Consultar una ocasión por ID
     */
    @Transactional(readOnly = true)
    public OcasionEntity getOcasion(final Long ocasionId) throws EntityNotFoundException {
        log.info("Inicia proceso de consulta de la ocasión con id = {}", ocasionId);
        OcasionEntity ocasion = ocasionRepository.findById(ocasionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OCASION_NOT_FOUND));
        log.info("Finaliza proceso de consulta de la ocasión con id = {}", ocasionId);
        return ocasion;
    }

    /**
     * Actualizar una ocasión
     */
    @Transactional
    public OcasionEntity updateOcasion(final Long ocasionId, final OcasionEntity ocasionEntity)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de actualización de la ocasión con id = {}", ocasionId);

        OcasionEntity existente = ocasionRepository.findById(ocasionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OCASION_NOT_FOUND));

        validarOcasion(ocasionEntity);

        // Validar duplicado de nombre
        Optional<OcasionEntity> duplicada = ocasionRepository.findByNombreIgnoreCase(ocasionEntity.getNombre());
        if (duplicada.isPresent() && !duplicada.get().getId().equals(ocasionId)) {
            throw new IllegalOperationException("Ya existe otra ocasión con ese nombre");
        }

        existente.setNombre(ocasionEntity.getNombre());
        existente.setCategorias(ocasionEntity.getCategorias());

        OcasionEntity updated = ocasionRepository.save(existente);
        log.info("Finaliza proceso de actualización de la ocasión con id = {}", ocasionId);
        return updated;
    }

    /**
     * Eliminar una ocasión
     */
    @Transactional
    public void deleteOcasion(final Long ocasionId)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de eliminación de la ocasión con id = {}", ocasionId);

        OcasionEntity ocasion = ocasionRepository.findById(ocasionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OCASION_NOT_FOUND));

        if (outfitRepository.existsById(ocasionId)) {
            throw new IllegalOperationException("No se puede eliminar la ocasión porque está asociada a un outfit");
        }

        ocasionRepository.delete(ocasion);
        log.info("Finaliza proceso de eliminación de la ocasión con id = {}", ocasionId);
    }

    /**
     * Validar datos de la ocasión
     */
    private void validarOcasion(final OcasionEntity ocasionEntity) throws IllegalOperationException {
        if (ocasionEntity.getNombre() == null || ocasionEntity.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre de la ocasión no puede estar vacío");
        }
    }
}
