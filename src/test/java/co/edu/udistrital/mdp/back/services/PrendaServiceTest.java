package co.edu.udistrital.mdp.back.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.entities.MarcaEntity;
import co.edu.udistrital.mdp.back.entities.CategoriaEntity;
import co.edu.udistrital.mdp.back.entities.ColorEntity;
import co.edu.udistrital.mdp.back.entities.ImagenEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PrendaService.class)
class PrendaServiceTest {

    @Autowired
    private PrendaService prendaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<PrendaEntity> prendaList = new ArrayList<>();
    private List<MarcaEntity> marcaList = new ArrayList<>();
    private List<CategoriaEntity> categoriaList = new ArrayList<>();
    private List<ColorEntity> colorList = new ArrayList<>();
    private List<ImagenEntity> imagenList = new ArrayList<>();

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MarcaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from CategoriaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ColorEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ImagenEntity").executeUpdate();
    }


    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        // Marcas
        for (int i = 0; i < 2; i++) {
            MarcaEntity marca = factory.manufacturePojo(MarcaEntity.class);
            marca.setNombre("Marca " + i);
            entityManager.persist(marca);
            marcaList.add(marca);
        }

        // Categorías
        for (int i = 0; i < 2; i++) {
            CategoriaEntity categoria = factory.manufacturePojo(CategoriaEntity.class);
            categoria.setNombre("Categoria " + i);
            entityManager.persist(categoria);
            categoriaList.add(categoria);
        }

        // Colores
        for (int i = 0; i < 2; i++) {
            ColorEntity color = factory.manufacturePojo(ColorEntity.class);
            entityManager.persist(color);
            colorList.add(color);
        }

        // Imágenes y prendas
        for (int i = 0; i < 3; i++) {
            ImagenEntity imagen = factory.manufacturePojo(ImagenEntity.class);
            entityManager.persist(imagen);
            imagenList.add(imagen);

            PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
            prenda.setMarca(marcaList.get(0));
            prenda.setCategoria(categoriaList.get(0));
            prenda.setColor(colorList.get(0));
            prenda.setImagen(imagen); // cada prenda con imagen única
            entityManager.persist(prenda);
            prendaList.add(prenda);
        }

    }

    /**
     * Prueba para crear una prenda correctamente.
     */
    @Test
    void testCreatePrendaCorrecto() throws EntityNotFoundException, IllegalOperationException {
        ColorEntity color = factory.manufacturePojo(ColorEntity.class);
        entityManager.persist(color);

        ImagenEntity imagen = factory.manufacturePojo(ImagenEntity.class);
        entityManager.persist(imagen);

        PrendaEntity toCreate = factory.manufacturePojo(PrendaEntity.class);
        toCreate.setNombre("Prenda Nueva");
        toCreate.setColor(color);
        toCreate.setImagen(imagen);
        toCreate.setMarca(marcaList.get(1));
        toCreate.setCategoria(categoriaList.get(1));

        PrendaEntity result = prendaService.createPrenda(toCreate);

        assertNotNull(result);
        assertEquals("Prenda Nueva", result.getNombre());
        assertEquals(color.getId(), result.getColor().getId());
        assertEquals(imagen.getId(), result.getImagen().getId());
    }


    /**
     * Prueba para crear una prenda sin imagen, lo cual debe lanzar excepción.
     */
    @Test
    void testCreatePrendaSinImagen() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity toCreate = factory.manufacturePojo(PrendaEntity.class);
            toCreate.setNombre("Prenda Sin Imagen");
            toCreate.setColor(colorList.get(0));
            toCreate.setMarca(marcaList.get(0));
            toCreate.setCategoria(categoriaList.get(0));

            prendaService.createPrenda(toCreate);
        });
    }

    /**
     * Prueba para consultar todas las prendas.
     */
    @Test
    void testGetAllPrendas() {
        List<PrendaEntity> result = prendaService.getAllPrendas();
        prendaList = prendaService.getAllPrendas();
        assertEquals(prendaList.size(), result.size());
    }

    /**
     * Prueba para consultar una prenda existente.
     */
    @Test
    void testGetPrendaExistente() throws EntityNotFoundException {
        PrendaEntity prenda = prendaList.get(0);
        PrendaEntity result = prendaService.getPrendaById(prenda.getId());
        assertEquals(prenda.getNombre(), result.getNombre());
    }

    /**
     * Prueba para consultar una prenda que no existe.
     */
    @Test
    void testGetPrendaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> prendaService.getPrendaById(0L));
    }

    /**
     * Prueba para actualizar una prenda correctamente.
     */
    @Test
    void testUpdatePrendaCorrecto() throws EntityNotFoundException, IllegalOperationException {
        PrendaEntity prenda = prendaList.get(0);
        PrendaEntity update = factory.manufacturePojo(PrendaEntity.class);
        update.setNombre("Prenda Actualizada");
        update.setColor(colorList.get(1));
        update.setImagen(imagenList.get(1));
        update.setMarca(marcaList.get(1));
        update.setCategoria(categoriaList.get(1));

        PrendaEntity result = prendaService.updatePrenda(prenda.getId(), update);
        assertEquals("Prenda Actualizada", result.getNombre());
        assertEquals(colorList.get(1).getId(), result.getColor().getId());
    }

    /**
     * Prueba para actualizar una prenda con nombre vacío.
     */
    @Test
    void testUpdatePrendaSinNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity prenda = prendaList.get(0);
            PrendaEntity update = factory.manufacturePojo(PrendaEntity.class);
            update.setNombre("");
            update.setColor(colorList.get(0));
            update.setImagen(imagenList.get(0));
            update.setMarca(marcaList.get(0));
            update.setCategoria(categoriaList.get(0));

            prendaService.updatePrenda(prenda.getId(), update);
        });
    }

    /**
     * Prueba para eliminar una prenda correctamente (sin imagen ni outfits).
     */
    @Test
    void testDeletePrendaCorrecto() throws EntityNotFoundException, IllegalOperationException {
        PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
        prenda.setNombre("Prenda a eliminar");
        prenda.setColor(colorList.get(0));
        prenda.setImagen(null); // Para poder eliminarla
        prenda.setMarca(marcaList.get(0));
        prenda.setCategoria(categoriaList.get(0));
        entityManager.persist(prenda);

        prendaService.deletePrenda(prenda.getId());
        assertNull(entityManager.find(PrendaEntity.class, prenda.getId()));
    }

    /**
     * Prueba para eliminar una prenda que no existe.
     */
    @Test
    void testDeletePrendaNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> prendaService.deletePrenda(0L));
    }

    /**
     * Prueba para eliminar una prenda con imagen, lo cual debe lanzar excepción.
     */
    @Test
    void testDeletePrendaConImagen() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity prenda = prendaList.get(0);
            prendaService.deletePrenda(prenda.getId());
        });
    }
}
