package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.back.entities.ImagenPrendaEntity;
import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.repositories.PrendaRepository;

import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ImagenPrendaService.class)
class ImagenPrendaServiceTest {

    @Autowired
    private ImagenPrendaService imagenPrendaService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PrendaRepository prendaRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() {
       ImagenPrendaEntity imagenPrendaBase = new ImagenPrendaEntity();
    imagenPrendaBase.setImagen("https://servidor.com/imagenBase.png");
    imagenPrendaBase.setPrenda(null);
    imagenPrendaBase.setMarca(null);

    entityManager.persist(imagenPrendaBase);
    entityManager.flush();
    }

    @Test
void createImagenPrendaTest() throws EntityNotFoundException, IllegalOperationException {
    
    PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
    prendaRepository.save(prenda);

    
    ImagenPrendaEntity imagen = new ImagenPrendaEntity();
    imagen.setImagen("https://img.com/foto1.png");

    
    ImagenPrendaEntity result = imagenPrendaService.createImagenPrenda(prenda.getId(), imagen);

   
    assertNotNull(result);
    assertEquals(imagen.getImagen(), result.getImagen());
    assertEquals(prenda.getId(), result.getPrenda().getId());
}

   @Test
void createImagenPrendaSinImagenTest() throws EntityNotFoundException {
    PrendaEntity prenda = prendaRepository.save(factory.manufacturePojo(PrendaEntity.class));

    ImagenPrendaEntity imagen = new ImagenPrendaEntity();
    imagen.setImagen(""); 

    assertThrows(IllegalOperationException.class, () -> {
        imagenPrendaService.createImagenPrenda(prenda.getId(), imagen);
    });
}

    @Test
    void getImagenesPrendaTest() {
        ImagenPrendaEntity imagenPrenda = factory.manufacturePojo(ImagenPrendaEntity.class);
        imagenPrenda.setImagen("https://servidor.com/imagen2.png");

        entityManager.persist(imagenPrenda);

        List<ImagenPrendaEntity> lista = imagenPrendaService.getImagenPrendas(null);

        assertFalse(lista.isEmpty());
        assertTrue(lista.stream().anyMatch(i -> i.getId().equals(imagenPrenda.getId())));
    }

    @Test
    void getImagenPrendaTest() throws EntityNotFoundException {
        ImagenPrendaEntity imagenPrenda = factory.manufacturePojo(ImagenPrendaEntity.class);
        imagenPrenda.setImagen("https://servidor.com/imagen3.png");

        entityManager.persist(imagenPrenda);

        ImagenPrendaEntity encontrada = imagenPrendaService.getImagenPrenda(imagenPrenda.getId(), null);
        assertNotNull(encontrada);
        assertEquals(imagenPrenda.getId(), encontrada.getId());
    }

    @Test
    void getImagenPrendaInexistenteTest() {
        assertThrows(EntityNotFoundException.class, () -> {
            imagenPrendaService.getImagenPrenda(999L, null);
        });
    }

    @Test
    void updateImagenPrendaTest() throws EntityNotFoundException, IllegalOperationException {
        ImagenPrendaEntity imagenPrenda = factory.manufacturePojo(ImagenPrendaEntity.class);
        imagenPrenda.setImagen("https://servidor.com/imagen4.png");

        entityManager.persist(imagenPrenda);

        String nuevaRuta = "https://servidor.com/nueva_imagen.png";
        imagenPrenda.setImagen(nuevaRuta);

        ImagenPrendaEntity actualizada = imagenPrendaService.updateImagenPrenda(imagenPrenda.getId(), null, imagenPrenda);

        assertEquals(nuevaRuta, actualizada.getImagen());
    }

    @Test
    void deleteImagenPrendaTest() throws EntityNotFoundException, IllegalOperationException {
        ImagenPrendaEntity imagenPrenda = factory.manufacturePojo(ImagenPrendaEntity.class);
        imagenPrenda.setImagen("https://servidor.com/imagen5.png");
        imagenPrenda.setPrenda(null);
        imagenPrenda.setMarca(null);

        entityManager.persist(imagenPrenda);

        imagenPrendaService.deleteImagenPrenda(imagenPrenda.getId(), null);

        ImagenPrendaEntity eliminada = entityManager.find(ImagenPrendaEntity.class, imagenPrenda.getId());
        assertNull(eliminada);
    }

    @Test
    void deleteImagenPrendaAsociadaTest() {
        ImagenPrendaEntity imagenPrenda = factory.manufacturePojo(ImagenPrendaEntity.class);
        imagenPrenda.setImagen("https://servidor.com/imagen6.png");

        // Simular que tiene asociaciones (no nulas)
        entityManager.persist(imagenPrenda);
        imagenPrenda.setPrenda(factory.manufacturePojo(co.edu.udistrital.mdp.back.entities.PrendaEntity.class));

        entityManager.persist(imagenPrenda);

        assertThrows(IllegalOperationException.class, () -> {
            imagenPrendaService.deleteImagenPrenda(imagenPrenda.getId(), null);
        });
    }
}