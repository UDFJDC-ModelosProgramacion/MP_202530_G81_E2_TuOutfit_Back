package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.ImagenOutfitEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.back.repositories.ImagenOutfitRepository;
import co.edu.udistrital.mdp.back.repositories.OutfitRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ImagenOutfitService.class)
class ImagenOutfitServiceTest {

    @Autowired
    private ImagenOutfitService imagenService;

    @Autowired
    private ImagenOutfitRepository imagenRepository;

     @Autowired
    private OutfitRepository outfitRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private OutfitEntity outfit;

    @BeforeEach
    void setUp() {
        outfit = factory.manufacturePojo(OutfitEntity.class);
        outfit.setImagen(null); 
        entityManager.persist(outfit);
    }

    @Test
    void createImagenTest() throws EntityNotFoundException, IllegalOperationException {
        ImagenOutfitEntity imagen = factory.manufacturePojo(ImagenOutfitEntity.class);
        imagen.setOutfit(outfit);

        ImagenOutfitEntity nueva = imagenService.createImagenOutfit(null, imagen);

        assertNotNull(nueva);
        ImagenOutfitEntity encontrada = entityManager.find(ImagenOutfitEntity.class, nueva.getId());
        assertEquals(imagen.getImagen(), encontrada.getImagen());
    }

    @Test
    void getImagenesTest() {
        ImagenOutfitEntity imagen = factory.manufacturePojo(ImagenOutfitEntity.class);
        imagen.setOutfit(outfit);
        entityManager.persist(imagen);

        List<ImagenOutfitEntity> lista = imagenService.getImagenOutfits(null);

        assertFalse(lista.isEmpty());
        assertTrue(lista.stream().anyMatch(i -> i.getId().equals(imagen.getId())));
    }

    @Test
void getImagenTest() throws EntityNotFoundException {
   
    OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
    outfitRepository.save(outfit);

  
    ImagenOutfitEntity imagen = factory.manufacturePojo(ImagenOutfitEntity.class);
    imagen.setOutfit(outfit);
    imagen = imagenRepository.save(imagen);

  
    ImagenOutfitEntity encontrada = imagenService.getImagenOutfit(outfit.getId(), imagen.getId());

    
    assertNotNull(encontrada);
    assertEquals(imagen.getId(), encontrada.getId());
    assertEquals(outfit.getId(), encontrada.getOutfit().getId());
}

@Test
void updateImagenTest() throws EntityNotFoundException, IllegalOperationException {
    
    OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
    outfit = outfitRepository.save(outfit);

  
    ImagenOutfitEntity imagen = factory.manufacturePojo(ImagenOutfitEntity.class);
    imagen.setOutfit(outfit);
    imagen = imagenRepository.save(imagen);

    
    ImagenOutfitEntity nuevaImagen = new ImagenOutfitEntity();
    nuevaImagen.setImagen("https://cdn.test.com/nueva-imagen.png");

  
    ImagenOutfitEntity resultado = imagenService.updateImagenOutfit(
            outfit.getId(),
            imagen.getId(),
            nuevaImagen
    );

    assertNotNull(resultado);
    assertEquals("https://cdn.test.com/nueva-imagen.png", resultado.getImagen());
    assertEquals(outfit.getId(), resultado.getOutfit().getId());
}
    
    @Test
    @Transactional
    void deleteImagenSinOutfitTest() throws Exception {
      
        ImagenOutfitEntity imagen = new ImagenOutfitEntity();
        imagen.setImagen("url_imagen_libre.jpg");
        imagen = imagenRepository.save(imagen);

      
        imagenService.deleteImagenOutfit(imagen.getId(), null);

     
        Optional<ImagenOutfitEntity> deleted = imagenRepository.findById(imagen.getId());
        assertTrue(deleted.isEmpty(), "La imagen sin outfit debería eliminarse correctamente");
    }

}
