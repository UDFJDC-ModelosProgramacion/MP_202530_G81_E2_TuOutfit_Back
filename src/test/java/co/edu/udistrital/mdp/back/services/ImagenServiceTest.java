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

import co.edu.udistrital.mdp.back.entities.ImagenEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.entities.MarcaEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ImagenService.class)
class ImagenServiceTest {

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private OutfitEntity outfit;
    private PrendaEntity prenda;
    private MarcaEntity marca;

    @BeforeEach
    void setUp() {
        outfit = factory.manufacturePojo(OutfitEntity.class);
        prenda = factory.manufacturePojo(PrendaEntity.class);
        marca = factory.manufacturePojo(MarcaEntity.class);

        entityManager.persist(outfit);
        entityManager.persist(prenda);
        entityManager.persist(marca);
    }

    @Test
    void createImagenTest() throws EntityNotFoundException, IllegalOperationException {
        ImagenEntity imagen = factory.manufacturePojo(ImagenEntity.class);
        imagen.setOutfit(outfit);
        imagen.setPrenda(prenda);
        imagen.setMarca(marca);

        ImagenEntity nueva = imagenService.createImagen(imagen);

        assertNotNull(nueva);
        ImagenEntity encontrada = entityManager.find(ImagenEntity.class, nueva.getId());
        assertEquals(imagen.getImagen(), encontrada.getImagen());
    }

    @Test
    void getImagenesTest() {
        ImagenEntity imagen = factory.manufacturePojo(ImagenEntity.class);
        imagen.setOutfit(outfit);
        imagen.setPrenda(prenda);
        imagen.setMarca(marca);
        entityManager.persist(imagen);

        List<ImagenEntity> lista = imagenService.getImagenes();

        assertFalse(lista.isEmpty());
        assertTrue(lista.stream().anyMatch(i -> i.getId().equals(imagen.getId())));
    }

    @Test
    void getImagenTest() throws EntityNotFoundException {
        ImagenEntity imagen = factory.manufacturePojo(ImagenEntity.class);
        imagen.setOutfit(outfit);
        imagen.setPrenda(prenda);
        imagen.setMarca(marca);
        entityManager.persist(imagen);

        ImagenEntity encontrada = imagenService.getImagen(imagen.getId());
        assertNotNull(encontrada);
        assertEquals(imagen.getId(), encontrada.getId());
    }

    @Test
    void updateImagenTest() throws EntityNotFoundException, IllegalOperationException {
        ImagenEntity imagen = factory.manufacturePojo(ImagenEntity.class);
        imagen.setOutfit(outfit);
        imagen.setPrenda(prenda);
        imagen.setMarca(marca);
        entityManager.persist(imagen);

        String nuevaRuta = "https://servidor.com/nueva_imagen.png";
        imagen.setImagen(nuevaRuta);

        ImagenEntity actualizada = imagenService.updateImagen(imagen.getId(), imagen);

        assertEquals(nuevaRuta, actualizada.getImagen());
    }

    @Test
    void deleteImagenTest() throws EntityNotFoundException, IllegalOperationException {
        ImagenEntity imagen = factory.manufacturePojo(ImagenEntity.class);
        imagen.setOutfit(null);
        imagen.setPrenda(null);
        imagen.setMarca(null);
        entityManager.persist(imagen);

        imagenService.deleteImagen(imagen.getId());

        ImagenEntity eliminada = entityManager.find(ImagenEntity.class, imagen.getId());
        assertNull(eliminada);
    }
}
