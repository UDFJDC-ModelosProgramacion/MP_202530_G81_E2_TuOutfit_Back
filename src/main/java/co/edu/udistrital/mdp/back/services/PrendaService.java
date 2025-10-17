package co.edu.udistrital.mdp.back.services;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.entities.MarcaEntity;
import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.repositories.PrendaRepository;
import co.edu.udistrital.mdp.back.repositories.MarcaRepository;
import co.edu.udistrital.mdp.back.repositories.CategoriaRepository;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrendaService {

    @Autowired
    private PrendaRepository prendaRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public PrendaEntity createPrenda(PrendaEntity prenda) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de crear prenda");

        // Validar marca
        MarcaEntity marca = marcaRepository.findById(prenda.getMarca().getId())
                .orElseThrow(() -> new EntityNotFoundException("La marca no existe"));

        // Validar categoría
        CategoriaEntity categoria = categoriaRepository.findById(prenda.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("La categoría no existe"));

        // Validar campos requeridos
        if (prenda.getColor() == null || prenda.getImagen() == null)
            throw new IllegalOperationException("La prenda debe tener color e imagen");

        prenda.setMarca(marca);
        prenda.setCategoria(categoria);

        log.info("Prenda creada correctamente");
        return prendaRepository.save(prenda);
    }

    @Transactional
    public PrendaEntity updatePrenda(Long prendaId, PrendaEntity prendaDetails) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar prenda");

        PrendaEntity prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND));

        // Validar nombre
        if (prendaDetails.getNombre() == null || prendaDetails.getNombre().isEmpty())
            throw new IllegalOperationException("El nombre no puede estar vacío");

        // Validar marca y categoría
        MarcaEntity marca = marcaRepository.findById(prendaDetails.getMarca().getId())
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada"));
        CategoriaEntity categoria = categoriaRepository.findById(prendaDetails.getCategoria().getId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        // Validar imagen
        if (prendaDetails.getImagen() == null)
            throw new IllegalOperationException("La prenda debe tener imagen");

        prenda.setNombre(prendaDetails.getNombre());
        prenda.setMarca(marca);
        prenda.setCategoria(categoria);
        prenda.setColor(prendaDetails.getColor());
        prenda.setImagen(prendaDetails.getImagen());

        log.info("Prenda actualizada correctamente");
        return prendaRepository.save(prenda);
    }

    @Transactional
    public void deletePrenda(Long prendaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de eliminar prenda");

        PrendaEntity prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND));

        // Validar relaciones
        if (prenda.getImagen() != null || !prenda.getOutfits().isEmpty())
            throw new IllegalOperationException("No se puede eliminar la prenda, tiene imágenes o está en outfits");

        prendaRepository.delete(prenda);
        log.info("Prenda eliminada correctamente");
    }

    public List<PrendaEntity> getAllPrendas() {
        return prendaRepository.findAll();
    }

    public PrendaEntity getPrendaById(Long prendaId) throws EntityNotFoundException {
        return prendaRepository.findById(prendaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND));
    }
}
