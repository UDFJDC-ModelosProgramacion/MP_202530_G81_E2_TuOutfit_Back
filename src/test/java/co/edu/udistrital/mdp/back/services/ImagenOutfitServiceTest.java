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

        ImagenOutfitEntity nueva = imagenService.createImagen(imagen);

        assertNotNull(nueva);
        ImagenOutfitEntity encontrada = entityManager.find(ImagenOutfitEntity.class, nueva.getId());
        assertEquals(imagen.getImagen(), encontrada.getImagen());
    }

    @Test
    void getImagenesTest() {
        ImagenOutfitEntity imagen = factory.manufacturePojo(ImagenOutfitEntity.class);
        imagen.setOutfit(outfit);
        entityManager.persist(imagen);

        List<ImagenOutfitEntity> lista = imagenService.getImagenes();

        assertFalse(lista.isEmpty());
        assertTrue(lista.stream().anyMatch(i -> i.getId().equals(imagen.getId())));
    }

    @Test
    void getImagenTest() throws EntityNotFoundException {
        ImagenOutfitEntity imagen = factory.manufacturePojo(ImagenOutfitEntity.class);
        imagen.setOutfit(outfit);
        entityManager.persist(imagen);

        ImagenOutfitEntity encontrada = imagenService.getImagen(imagen.getId());
        assertNotNull(encontrada);
        assertEquals(imagen.getId(), encontrada.getId());
    }

    @Test
    void updateImagenTest() throws EntityNotFoundException, IllegalOperationException {
        ImagenOutfitEntity imagen = factory.manufacturePojo(ImagenOutfitEntity.class);
        imagen.setOutfit(outfit);
        entityManager.persist(imagen);

        String nuevaRuta = "https://servidor.com/nueva_imagen.png";
        imagen.setImagen(nuevaRuta);

        ImagenOutfitEntity actualizada = imagenService.updateImagen(imagen.getId(), imagen);

        assertEquals(nuevaRuta, actualizada.getImagen());
    }

    
    @Test
    @Transactional
    void deleteImagenSinOutfitTest() throws Exception {
      
        ImagenOutfitEntity imagen = new ImagenOutfitEntity();
        imagen.setImagen("url_imagen_libre.jpg");
        imagen = imagenRepository.save(imagen);

      
        imagenService.deleteImagen(imagen.getId());

     
        Optional<ImagenOutfitEntity> deleted = imagenRepository.findById(imagen.getId());
        assertTrue(deleted.isEmpty(), "La imagen sin outfit debería eliminarse correctamente");
    }

}
