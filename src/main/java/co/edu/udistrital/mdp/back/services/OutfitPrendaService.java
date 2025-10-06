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
public class OutfitPrendaService {
 @Autowired
	private OutfitRepository outfitRepository;

	@Autowired
	private PrendaRepository prendaRepository;

    @Transactional
	public PrendaEntity addPrenda(Long outfitId, Long prendaId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle una prenda al outfit con id = {0}", outfitId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		outfitEntity.get().getPrendas().add(prendaEntity.get());
		log.info("Termina proceso de asociarle una prenda al outfit con id = {0}", outfitId);
		return prendaEntity.get();
	}


    @Transactional
	public List<PrendaEntity> getPrendas(Long outfitId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos las prendas del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
		log.info("Finaliza proceso de consultar todos las prendas del outfit con id = {0}", outfitId);
		return outfitEntity.get().getPrendas();
	}


    @Transactional
	public PrendaEntity getPrenda(Long outfitId, Long prendaId)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar una prenda del outfit con id = {0}", outfitId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
		log.info("Termina proceso de consultar una prenda del outfit con id = {0}", outfitId);
		if (!outfitEntity.get().getPrendas().contains(prendaEntity.get()))
			throw new IllegalOperationException("The Prenda is not associated to the Outfit");
		
		return prendaEntity.get();
	}



    @Transactional
    public List<PrendaEntity> replacePrendas(Long outfitId, List<PrendaEntity> list) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar las prendas del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		for (PrendaEntity prenda : list) {
			Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prenda.getId());
			if (prendaEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

			if (!outfitEntity.get().getPrendas().contains(prendaEntity.get()))
				outfitEntity.get().getPrendas().add(prendaEntity.get());
		}
		log.info("Termina proceso de reemplazar las prendas del outfit con id = {0}", outfitId);
		return outfitEntity.get().getPrendas();
	}


    @Transactional
    public void removePrenda(Long outfitId, Long prendaId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar una prenda del outfit con id = {0}", outfitId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		outfitEntity.get().getPrendas().remove(prendaEntity.get());

		log.info("Termina proceso de borrar una prenda del outfit con id = {0}", outfitId);
	}

}
