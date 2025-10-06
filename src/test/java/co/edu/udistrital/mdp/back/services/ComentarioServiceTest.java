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

import co.edu.udistrital.mdp.back.entities.ComentarioEntity;
import co.edu.udistrital.mdp.back.entities.OutfitEntity;
import co.edu.udistrital.mdp.back.entities.PrendaEntity;
import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ComentarioService.class)
class ComentarioServiceTest {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ComentarioEntity> comentarioList = new ArrayList<>();
    private UsuarioEntity usuario;

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
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        usuario = factory.manufacturePojo(UsuarioEntity.class);
        usuario.setCorreo("user@test.com");
        usuario.setNombre("Juan Pérez");
        entityManager.persist(usuario);

        // Crear y guardar un outfit de prueba
        OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(outfit);

        // Crear y guardar una prenda de prueba
        PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
        entityManager.persist(prenda);

        for (int i = 0; i < 3; i++) {
            ComentarioEntity comentarioEntity = factory.manufacturePojo(ComentarioEntity.class);
            comentarioEntity.setUsuario(usuario);
            comentarioEntity.setTexto("Comentario " + (i + 1));
            comentarioEntity.setOutfit(outfit);
            comentarioEntity.setPrenda(prenda);
            entityManager.persist(comentarioEntity);
            comentarioList.add(comentarioEntity);
        }
    }

    /**
     * Prueba para crear un comentario correctamente.
     */
    @Test
    void testCreateComentarioCorrecto() throws IllegalOperationException, EntityNotFoundException {
        ComentarioEntity toCreate = factory.manufacturePojo(ComentarioEntity.class);
        toCreate.setTexto("Excelente outfit");
        // Evita referencias transientes:
        toCreate.setUsuario(null);
        toCreate.setOutfit(null);
        toCreate.setPrenda(null);

        ComentarioEntity result = comentarioService.createComentario(usuario.getId(), toCreate);
        assertNotNull(result);
        assertEquals("Excelente outfit", result.getTexto());
        assertEquals(usuario.getId(), result.getUsuario().getId());
    }

    /**
     * Prueba para crear un comentario vacío.
     */
    @Test
    void testCreateComentarioVacio() {
        assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity toCreate = factory.manufacturePojo(ComentarioEntity.class);
            toCreate.setTexto("");
            comentarioService.createComentario(usuario.getId(), toCreate);
        });
    }

    /**
     * Prueba para crear un comentario con usuario inexistente.
     */
    @Test
    void testCreateComentarioUsuarioNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity toCreate = factory.manufacturePojo(ComentarioEntity.class);
            toCreate.setTexto("Comentario de prueba");
            comentarioService.createComentario(0L, toCreate);
        });
    }

    /**
     * Prueba para consultar todos los comentarios.
     */
    @Test
    void testGetComentarios() {
        List<ComentarioEntity> lista = comentarioService.getComentarios();
        assertEquals(comentarioList.size(), lista.size());
    }

    /**
     * Prueba para consultar un comentario existente.
     */
    @Test
    void testGetComentarioExistente() throws EntityNotFoundException {
        ComentarioEntity entity = comentarioList.get(0);
        ComentarioEntity result = comentarioService.getComentario(entity.getId());
        assertNotNull(result);
        assertEquals(entity.getTexto(), result.getTexto());
        assertEquals(entity.getUsuario().getId(), result.getUsuario().getId());
    }

    /**
     * Prueba para consultar un comentario que no existe.
     */
    @Test
    void testGetComentarioNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> comentarioService.getComentario(0L));
    }

    /**
     * Prueba para actualizar un comentario correctamente.
     * @throws IllegalOperationException 
     */
    @Test
    void testUpdateComentarioCorrecto() throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity entity = comentarioList.get(0);
        ComentarioEntity update = factory.manufacturePojo(ComentarioEntity.class);
        update.setTexto("Comentario actualizado");

        ComentarioEntity result = comentarioService.updateComentario(entity.getId(), update);
        assertEquals("Comentario actualizado", result.getTexto());
    }

    /**
     * Prueba para actualizar un comentario vacío.
     */
    @Test
    void testUpdateComentarioVacio() {
        assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity entity = comentarioList.get(0);
            ComentarioEntity update = factory.manufacturePojo(ComentarioEntity.class);
            update.setTexto("");
            comentarioService.updateComentario(entity.getId(), update);
        });
    }

    /**
     * Prueba para eliminar un comentario correctamente.
     */
    @Test
    void testDeleteComentarioCorrecto() throws EntityNotFoundException {
        ComentarioEntity entity = comentarioList.get(0);
        comentarioService.deleteComentario(entity.getId());
        assertNull(entityManager.find(ComentarioEntity.class, entity.getId()));
    }

    /**
     * Prueba para eliminar un comentario que no existe.
     */
    @Test
    void testDeleteComentarioNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> comentarioService.deleteComentario(0L));
    }
}
