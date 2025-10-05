package co.edu.udistrital.mdp.back.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.back.entities.ListaDeseosEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.repositories.ListaDeseosRepository;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import co.edu.udistrital.mdp.back.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service

public class ListaDeseosService {

    @Autowired
    private ListaDeseosRepository listaDeseosRepository;

    @Autowired
	private UsuarioRepository usuarioRepository;

    @Autowired
    private OutfitRepository outfitRepository;

    /**
	 * Se encarga de crear una ListaDeseos en la base de datos.
	 *
	 * @param listaDeseosId Objeto de ListaDeseosEntity con los datos nuevos
	 * @return Objeto de ListaDeseosEntity con los datos nuevos y su ID.
	 * @throws IllegalOperationException
	 */

    @Transactional
    public ListaDeseosEntity createListaDeseos(Long usuarioId, ListaDeseosEntity listaDeseos)
            throws EntityNotFoundException {
        log.info("Inicia proceso de creación de lista de deseos para usuario con id = {}", usuarioId);
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND));
        listaDeseos.setUsuario(usuario);
        usuario.setWishlist(listaDeseos);

        ListaDeseosEntity saved = listaDeseosRepository.save(listaDeseos);
        log.info("Finaliza proceso de creación de lista de deseos con id = {}", saved.getId());
        return saved;
    }

	/**
	 * Obtiene la lista de los registros de ListaDeseos.
	 *
	 * @return Colección de objetos de ListaDeseosEntity.
	 */
	@Transactional
	public List<ListaDeseosEntity> getListaDeseos() {
		log.info("Inicia proceso de consultar todas los ListaDeseoss");
		return listaDeseosRepository.findAll();
	}

	/**
	 * Obtiene los datos de una instancia de ListaDeseos a partir de su ID.
	 *
	 * @param listaDeseosId Identificador de la instancia a consultar
	 * @return Instancia de ListaDeseosEntity con los datos del ListaDeseos consultado.
	 */
	@Transactional
    public ListaDeseosEntity getListaDeseos(Long listaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consulta de lista de deseos con id = {}", listaId);
        return listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));
    }

	/**
	 * Actualiza la información de una instancia de ListaDeseos.
	 *
	 * @param listaDeseosId     Identificador de la instancia a actualizar
	 * @param listaDeseosEntity Instancia de ListaDeseosEntity con los nuevos datos.
	 * @return Instancia de ListaDeseosEntity con los datos actualizados.
	 */
	@Transactional
    public ListaDeseosEntity updateListaDeseos(Long listaId, ListaDeseosEntity listaNueva)
            throws EntityNotFoundException {
        log.info("Inicia proceso de actualización de lista de deseos con id = {}", listaId);

        ListaDeseosEntity entity = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        // Aquí podrías actualizar atributos si tu ListaDeseosEntity tiene más datos
        entity.setOutfits(listaNueva.getOutfits());

        ListaDeseosEntity updated = listaDeseosRepository.save(entity);
        log.info("Finaliza proceso de actualización de lista de deseos con id = {}", listaId);
        return updated;
    }

	/**
	 * Elimina una instancia de ListaDeseos de la base de datos.
	 *
	 * @param listaDeseosId Identificador de la instancia a eliminar.
	 * @throws EntityNotFoundException si el ListaDeseos no existe.
	 */
	@Transactional
    public void deleteListaDeseos(Long listaId) throws EntityNotFoundException {
        log.info("Inicia proceso de eliminación de lista de deseos con id = {}", listaId);

        ListaDeseosEntity entity = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        listaDeseosRepository.delete(entity);
        log.info("Finaliza proceso de eliminación de lista de deseos con id = {}", listaId);
    }


    // Relación ListaDeseos - Comentarios

    /**
     * Agrega un outfit a la lista de deseos.
     *
     * @param listaId  Identificador de la lista de deseos
     * @param outfitId Identificador del outfit
     * @return Outfit agregado
     * @throws EntityNotFoundException si la lista o el outfit no existen
     * @throws IllegalOperationException si el outfit ya está en la lista
     */
    @Transactional
    public OutfitEntity addOutfit(Long listaId, Long outfitId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de agregar outfit {} a la lista de deseos {}", outfitId, listaId);

        ListaDeseosEntity lista = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        OutfitEntity outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND));

        if (lista.getOutfits().contains(outfit)) {
            throw new IllegalOperationException("El outfit ya está en la lista de deseos");
        }

        lista.getOutfits().add(outfit);
        log.info("Finaliza proceso de agregar outfit {} a la lista de deseos {}", outfitId, listaId);
        return outfit;
    }

    /**
     * Obtiene los outfits de una lista de deseos.
     *
     * @param listaId Identificador de la lista de deseos
     * @return Lista de outfits
     * @throws EntityNotFoundException si la lista no existe
     */
    @Transactional
    public List<OutfitEntity> getOutfits(Long listaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consulta de outfits de la lista de deseos con id = {}", listaId);

        ListaDeseosEntity lista = listaDeseosRepository.findById(listaId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LISTADESEOS_NOT_FOUND));

        return lista.getOutfits();
    }
}
