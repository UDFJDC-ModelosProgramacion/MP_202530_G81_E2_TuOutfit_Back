package co.edu.udistrital.mdp.back.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.back.entities.ListaDeseosEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.ListaDeseosRepository;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con la persistencia para la relación entre
 * la entidad de Outfit y ListaDeseos
 */

@Slf4j
@Service
public class OutfitListaDeseosService {

    @Autowired
    private OutfitRepository outfitRepository;

    @Autowired
    private ListaDeseosRepository listaDeseosRepository;

    /**
     * Asocia una lista de deseos a un outfit.
     *
     * @param outfitId     Identificador del outfit.
     * @param listaId      Identificador de la lista de deseos.
     * @return ListaDeseosEntity asociada.
     * @throws EntityNotFoundException si el outfit o la lista no existen.
     * @throws IllegalOperationException si la lista ya está asociada al outfit.
     */
    @Transactional
    public ListaDeseosEntity addListaDeseos(Long outfitId, Long listaId)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de asociar lista de deseos {} al outfit {}", listaId, outfitId);

        OutfitEntity outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));

        ListaDeseosEntity listaDeseos = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        if (outfit.getListasDeseos().contains(listaDeseos)) {
            throw new IllegalOperationException("La lista ya está asociada a este outfit");
        }

        outfit.getListasDeseos().add(listaDeseos);
        log.info("Finaliza proceso de asociar lista de deseos {} al outfit {}", listaId, outfitId);

        return listaDeseos;
    }

    /**
     * Obtiene todas las listas de deseos asociadas a un outfit.
     *
     * @param outfitId Identificador del outfit.
     * @return Listado de listas de deseos.
     * @throws EntityNotFoundException si el outfit no existe.
     */
    @Transactional
    public List<ListaDeseosEntity> getListasDeseos(Long outfitId) throws EntityNotFoundException {
        log.info("Inicia consulta de listas de deseos del outfit con id = {}", outfitId);

        OutfitEntity outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));

        return outfit.getListasDeseos();
    }

    /**
     * Obtiene una lista de deseos específica asociada a un outfit.
     *
     * @param outfitId Identificador del outfit.
     * @param listaId  Identificador de la lista de deseos.
     * @return ListaDeseosEntity asociada.
     * @throws EntityNotFoundException si el outfit o la lista no existen.
     * @throws IllegalOperationException si la lista no está asociada al outfit.
     */
    @Transactional
    public ListaDeseosEntity getListaDeseos(Long outfitId, Long listaId)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia consulta de lista de deseos {} asociada al outfit {}", listaId, outfitId);

        OutfitEntity outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));

        ListaDeseosEntity listaDeseos = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        if (!outfit.getListasDeseos().contains(listaDeseos)) {
            throw new IllegalOperationException("La lista de deseos no está asociada a este outfit");
        }

        log.info("Finaliza consulta de lista de deseos {} asociada al outfit {}", listaId, outfitId);
        return listaDeseos;
    }

    /**
     * Reemplaza las listas de deseos asociadas a un outfit.
     *
     * @param outfitId Identificador del outfit.
     * @param nuevasListas Nuevas listas de deseos a asociar.
     * @return Lista actualizada de listas de deseos.
     * @throws EntityNotFoundException si el outfit o alguna lista no existen.
     */
    @Transactional
    public List<ListaDeseosEntity> replaceListasDeseos(Long outfitId, List<ListaDeseosEntity> nuevasListas)
            throws EntityNotFoundException {

        log.info("Inicia proceso de reemplazar listas de deseos del outfit {}", outfitId);

        OutfitEntity outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));

        for (ListaDeseosEntity lista : nuevasListas) {
            listaDeseosRepository.findById(lista.getId())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));
        }

        outfit.setListasDeseos(nuevasListas);
        log.info("Finaliza proceso de reemplazo de listas de deseos para el outfit {}", outfitId);
        return nuevasListas;
    }

    /**
     * Desasocia una lista de deseos de un outfit.
     *
     * @param outfitId Identificador del outfit.
     * @param listaId  Identificador de la lista de deseos.
     * @throws EntityNotFoundException si el outfit o la lista no existen.
     */
    @Transactional
    public void removeListaDeseos(Long outfitId, Long listaId) throws EntityNotFoundException {
        log.info("Inicia proceso de eliminar asociación de la lista de deseos {} del outfit {}", listaId, outfitId);

        OutfitEntity outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));

        ListaDeseosEntity listaDeseos = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        outfit.getListasDeseos().remove(listaDeseos);
        log.info("Finaliza proceso de eliminar asociación de la lista de deseos {} del outfit {}", listaId, outfitId);
    }
}
