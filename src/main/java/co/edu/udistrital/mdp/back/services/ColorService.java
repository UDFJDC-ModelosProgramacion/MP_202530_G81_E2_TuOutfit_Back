package co.edu.udistrital.mdp.back.services;

import co.edu.udistrital.mdp.back.entities.ColorEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.ColorRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    /**
     * Crear un color
     */
    @Transactional
    public ColorEntity createColor(ColorEntity colorEntity) throws IllegalOperationException {
        log.info("Inicia proceso de creación de color");

        if (colorEntity.getNombre() == null || colorEntity.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre del color no es válido");
        }

        if (colorEntity.getCodigoHex() == null || !((String) colorEntity.getCodigoHex()).matches("^#([A-Fa-f0-9]{6})$")) {
            throw new IllegalOperationException("El código HEX del color no es válido (ej: #FFFFFF)");
        }

        // Evitar duplicados por nombre
        Optional<ColorEntity> existente = colorRepository.findByNombreIgnoreCase(colorEntity.getNombre());
        if (existente.isPresent()) {
            throw new IllegalOperationException("Ya existe un color con ese nombre");
        }

        ColorEntity saved = colorRepository.save(colorEntity);
        log.info("Finaliza proceso de creación de color con id = {}", saved.getId());
        return saved;
    }

    /**
     * Obtener todos los colores
     */
    @Transactional(readOnly = true)
    public List<ColorEntity> getColores() {
        log.info("Inicia proceso de consulta de todos los colores");
        return colorRepository.findAll();
    }

    /**
     * Obtener un color por id
     */
    @Transactional(readOnly = true)
    public ColorEntity getColor(Long colorId) throws EntityNotFoundException {
        log.info("Inicia proceso de consulta del color con id = {}", colorId);
        return colorRepository.findById(colorId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.COLOR_NOT_FOUND));
    }

    /**
     * Actualizar un color
     */
    @Transactional
    public ColorEntity updateColor(Long colorId, ColorEntity colorEntity)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de actualización del color con id = {}", colorId);

        ColorEntity existente = colorRepository.findById(colorId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.COLOR_NOT_FOUND));

        if (colorEntity.getNombre() == null || colorEntity.getNombre().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre del color no es válido");
        }

        if (colorEntity.getCodigoHex() == null || !((String) colorEntity.getCodigoHex()).matches("^#([A-Fa-f0-9]{6})$")) {
            throw new IllegalOperationException("El código HEX del color no es válido (ej: #FFFFFF)");
        }

        // Evitar duplicado de nombre
        Optional<ColorEntity> duplicado = colorRepository.findByNombreIgnoreCase(colorEntity.getNombre());
        if (duplicado.isPresent() && !duplicado.get().getId().equals(colorId)) {
            throw new IllegalOperationException("Ya existe un color con ese nombre");
        }

        existente.setNombre(colorEntity.getNombre());
        existente.setCodigoHex(colorEntity.getCodigoHex());

        ColorEntity updated = colorRepository.save(existente);
        log.info("Finaliza proceso de actualización del color con id = {}", colorId);
        return updated;
    }

    /**
     * Eliminar un color
     */
    @Transactional
    public void deleteColor(Long colorId) throws EntityNotFoundException {
        log.info("Inicia proceso de eliminación del color con id = {}", colorId);

        if (!colorRepository.existsById(colorId)) {
            throw new EntityNotFoundException(ErrorMessage.COLOR_NOT_FOUND);
        }

        colorRepository.deleteById(colorId);
        log.info("Finaliza proceso de eliminación del color con id = {}", colorId);
    }
}