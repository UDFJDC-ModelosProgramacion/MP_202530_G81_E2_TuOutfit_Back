package co.edu.udistrital.mdp.back.services;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.udistrital.mdp.back.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;

import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;

import co.edu.udistrital.mdp.back.repositories.PrendaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrendaOutfitService {
    @Autowired
	private OutfitRepository outfitRepository;

	@Autowired
	private PrendaRepository prendaRepository;

    @Transactional
	public OutfitEntity addOutfit(Long prendaId, Long outfitId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un outfit a la prenda con id = {0}", prendaId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		outfitEntity.get().getPrendas().add(prendaEntity.get());
		log.info("Termina proceso de asociarle un libro al autor con id = {0}", prendaId);
		return outfitEntity.get();
	}


	@Transactional
	public List<OutfitEntity> getOutfits(Long prendaId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los outfits de la prenda con id = {0}", prendaId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		log.info("Termina proceso de consultar todos los outfits de la prenda con id = {0}", prendaId);
		return prendaEntity.get().getOutfits();
	}


	@Transactional
	public OutfitEntity getOutfit(Long prendaId, Long outfitId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar el outfit con id = {0} de la prenda con id = " + prendaId, outfitId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		log.info("Termina proceso de consultar el libro con id = {0} del autor con id = " + prendaId, outfitId);
		if (!outfitEntity.get().getPrendas().contains(prendaEntity.get()))
			throw new IllegalOperationException("The book is not associated to the author");
		
		return outfitEntity.get();
	}


	@Transactional
	public List<OutfitEntity> addOutfits(Long prendaId, List<OutfitEntity> outfits) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los outfits asociados a la prenda con id = {0}", prendaId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		for (OutfitEntity outfit : outfits) {
			Optional<OutfitEntity> bookEntity = outfitRepository.findById(outfit.getId());
			if (bookEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		}
		log.info("Finaliza proceso de reemplazar los outfits asociados a la prenda con id = {0}", prendaId);
		prendaEntity.get().setOutfits(outfits);
		return prendaEntity.get().getOutfits();
	}


	@Transactional
	public void removeOutfit(Long prendaId, Long outfitId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un outfit de la prenda con id = {0}", prendaId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		outfitEntity.get().getPrendas().remove(prendaEntity.get());
		prendaEntity.get().getOutfits().remove(outfitEntity.get());
		log.info("Finaliza proceso de borrar un libro del author con id = {0}", prendaId);
	}
}
