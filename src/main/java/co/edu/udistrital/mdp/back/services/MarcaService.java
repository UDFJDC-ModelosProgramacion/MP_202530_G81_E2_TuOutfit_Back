package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.MarcaEntity;
import co.edu.udistrital.mdp.back.entities.TiendaEntity;
import co.edu.udistrital.mdp.back.repositories.MarcaRepository;
import co.edu.udistrital.mdp.back.repositories.TiendaRepository;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    @Transactional
    public MarcaEntity createMarca(MarcaEntity marca) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de crear marca");

        // Validar nombre único
        if (marcaRepository.existsByNombre(marca.getNombre()))
            throw new IllegalOperationException("Ya existe una marca con ese nombre");

        // Validar tiendas asociadas
        if (marca.getTiendas() == null || marca.getTiendas().isEmpty())
            throw new IllegalOperationException("La marca debe tener al menos una tienda asociada");

        // Validar que todas las tiendas existan
        for (TiendaEntity tienda : marca.getTiendas()) {
            if (!tiendaRepository.existsById(tienda.getId()))
                throw new EntityNotFoundException("La tienda con id " + tienda.getId() + " no existe");
        }

        log.info("Marca creada correctamente");
        return marcaRepository.save(marca);
    }

    @Transactional
    public MarcaEntity updateMarca(Long marcaId, MarcaEntity marcaDetails) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar marca");

        MarcaEntity marca = marcaRepository.findById(marcaId)
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"));

        // Validar nombre único
        if (!marca.getNombre().equals(marcaDetails.getNombre()) && marcaRepository.existsByNombre(marcaDetails.getNombre()))
            throw new IllegalOperationException("Ya existe una marca con ese nombre");

        // Validar tiendas asociadas
        if (marcaDetails.getTiendas() == null || marcaDetails.getTiendas().isEmpty())
            throw new IllegalOperationException("La marca debe tener al menos una tienda asociada");

        // Validar que todas las tiendas existan
        for (TiendaEntity tienda : marcaDetails.getTiendas()) {
            if (!tiendaRepository.existsById(tienda.getId()))
                throw new EntityNotFoundException("La tienda con id " + tienda.getId() + " no existe");
        }

        marca.setNombre(marcaDetails.getNombre());
        marca.setImagen(marcaDetails.getImagen());
        marca.setTiendas(marcaDetails.getTiendas());

        log.info("Marca actualizada correctamente");
        return marcaRepository.save(marca);
    }

    @Transactional
    public void deleteMarca(Long marcaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de eliminar marca");

        MarcaEntity marca = marcaRepository.findById(marcaId)
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"));

        // Validar relación con prendas
        if (marca.getPrendas() != null && !marca.getPrendas().isEmpty())
            throw new IllegalOperationException("No se puede eliminar la marca, tiene prendas asociadas");

        marcaRepository.delete(marca);
        log.info("Marca eliminada correctamente");
    }

    public List<MarcaEntity> getAllMarcas() {
        return marcaRepository.findAll();
    }

    public MarcaEntity getMarcaById(Long marcaId) throws EntityNotFoundException {
        return marcaRepository.findById(marcaId)
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"));
    }
}
