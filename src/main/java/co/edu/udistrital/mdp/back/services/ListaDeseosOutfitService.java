package co.edu.udistrital.mdp.back.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.ListaDeseosEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.ListaDeseosRepository;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con la persistencia para la relación entre
 * la entidad de ListaDeseos y Outfit.
 */

@Slf4j
@Service
public class ListaDeseosOutfitService {

    @Autowired
    private ListaDeseosRepository listaDeseosRepository;

    @Autowired
    private OutfitRepository outfitRepository;

    /**
     * Asocia un Outfit existente a una Lista de deseos.
     *
     * @param listaId  Identificador de la lista de deseos
     * @param outfitId Identificador del outfit
     * @return Instancia de OutfitEntity que fue asociada a la lista
     */
    @Transactional
    public OutfitEntity addOutfit(Long listaId, Long outfitId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociar el outfit {} a la lista de deseos {}", outfitId, listaId);

        ListaDeseosEntity lista = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        OutfitEntity outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));

        lista.getOutfits().add(outfit);
        log.info("Finaliza proceso de asociar outfit {} a la lista {}", outfitId, listaId);
        return outfit;
    }

    /**
     * Obtiene una colección de outfits asociadas a una lista de deseos.
     *
     * @param listaId Identificador de la lista de deseos
     * @return Colección de instancias de OutfitEntity asociadas a la lista
     */
    @Transactional
    public List<OutfitEntity> getOutfits(Long listaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todos los outfits de la lista {}", listaId);

        ListaDeseosEntity lista = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        return lista.getOutfits();
    }

    /**
     * Obtiene un outfit específico de una lista.
     *
     * @param listaId  Identificador de la lista
     * @param outfitId Identificador del outfit
     * @return Entidad del outfit
     * @throws EntityNotFoundException    si la lista o el outfit no existen
     * @throws IllegalOperationException  si el outfit no pertenece a la lista
     */
    @Transactional
    public OutfitEntity getOutfit(Long listaId, Long outfitId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar el outfit {} de la lista {}", outfitId, listaId);

        ListaDeseosEntity lista = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        OutfitEntity outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));

        if (!lista.getOutfits().contains(outfit)) {
            throw new IllegalOperationException("El outfit no está asociado a la lista de deseos");
        }

        return outfit;
    }

    /**
     * Reemplaza los outfits asociados a una lista.
     *
     * @param listaId  Identificador de la lista
     * @param outfits  Nueva lista de outfits
     * @return Nueva colección de outfits asociada
     * @throws EntityNotFoundException si la lista o alguno de los outfits no existen
     */
    @Transactional
    public List<OutfitEntity> replaceOutfits(Long listaId, List<OutfitEntity> outfits)
            throws EntityNotFoundException {
        log.info("Inicia proceso de reemplazar los outfits de la lista {}", listaId);

        ListaDeseosEntity lista = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        for (OutfitEntity outfit : outfits) {
            outfitRepository.findById(outfit.getId())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));
        }

        lista.setOutfits(outfits);
        log.info("Finaliza proceso de reemplazo de outfits en lista {}", listaId);
        return lista.getOutfits();
    }

    /**
     * Elimina un outfit de una lista de deseos.
     *
     * @param listaId  Identificador de la lista
     * @param outfitId Identificador del outfit
     * @throws EntityNotFoundException si la lista o el outfit no existen
     */
    @Transactional
    public void removeOutfit(Long listaId, Long outfitId) throws EntityNotFoundException {
        log.info("Inicia proceso de eliminar el outfit {} de la lista {}", outfitId, listaId);

        ListaDeseosEntity lista = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        OutfitEntity outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));

        lista.getOutfits().remove(outfit);
        log.info("Finaliza proceso de eliminación del outfit {} de la lista {}", outfitId, listaId);
    }
}
