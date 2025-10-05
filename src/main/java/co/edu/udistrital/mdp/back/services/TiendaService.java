package co.edu.udistrital.mdp.back.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TiendaService {

    @Autowired
    private TiendaRepository tiendaRepository;

    @Transactional
    public TiendaEntity createTienda(TiendaEntity tienda) throws IllegalOperationException {
        log.info("Inicia proceso de crear tienda");

        // Validar nombre
        if (tienda.getNombre() == null || tienda.getNombre().isEmpty())
            throw new IllegalOperationException("El nombre es obligatorio");

        // Validar marcas
        if (tienda.getMarcas() == null || tienda.getMarcas().isEmpty())
            throw new IllegalOperationException("La tienda debe tener al menos una marca asociada");

        // Validar ubicación
        if (tienda.getUbicacion() == null)
            throw new IllegalOperationException("La tienda debe tener ubicación");

        log.info("Tienda creada correctamente");
        return tiendaRepository.save(tienda);
    }

    @Transactional
    public TiendaEntity updateTienda(Long tiendaId, TiendaEntity tiendaDetails) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar tienda");

        TiendaEntity tienda = tiendaRepository.findById(tiendaId)
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada"));

        // Validar nombre duplicado
        if (!tienda.getNombre().equals(tiendaDetails.getNombre()) && tiendaRepository.existsByNombre(tiendaDetails.getNombre()))
            throw new IllegalOperationException("El nombre ya existe");

        // Validar marcas
        if (tiendaDetails.getMarcas() == null || tiendaDetails.getMarcas().isEmpty())
            throw new IllegalOperationException("La tienda debe tener al menos una marca asociada");

        tienda.setNombre(tiendaDetails.getNombre());
        tienda.setUbicacion(tiendaDetails.getUbicacion());
        tienda.setMarcas(tiendaDetails.getMarcas());

        log.info("Tienda actualizada correctamente");
        return tiendaRepository.save(tienda);
    }

    @Transactional
    public void deleteTienda(Long tiendaId) throws EntityNotFoundException {
        log.info("Inicia proceso de eliminar tienda");

        TiendaEntity tienda = tiendaRepository.findById(tiendaId)
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada"));

        tiendaRepository.delete(tienda);
        log.info("Tienda eliminada correctamente");
    }
}
