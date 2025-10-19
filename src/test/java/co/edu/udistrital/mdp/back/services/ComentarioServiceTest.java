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
    private UsuarioEntity usuarioEntity;

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
		usuarioEntity = factory.manufacturePojo(UsuarioEntity.class);
		entityManager.persist(usuarioEntity);
		
		for (int i = 0; i < 3; i++) {
			ComentarioEntity entity = factory.manufacturePojo(ComentarioEntity.class);
			entity.setUsuario(usuarioEntity);
			entityManager.persist(entity);
			comentarioList.add(entity);
		}
		
		usuarioEntity.setComentarios(comentarioList);
	}

    /**
     * Prueba para crear un comentario.
     */
    @Test
	void testCreateComentario() throws EntityNotFoundException {
		ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
				
		ComentarioEntity result = comentarioService.createComentario(usuarioEntity.getId(), newEntity);
		assertNotNull(result);
		ComentarioEntity entity = entityManager.find(ComentarioEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getTexto(), entity.getTexto());
		assertEquals(newEntity.getCalificacion(), entity.getCalificacion());
	}


    /**
     * Prueba para crear un comentario de un usuario que no existe.
     */
    @Test
	void testCreateComentarioUsuarioNoValido() {
		assertThrows(EntityNotFoundException.class, () -> {
			ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
			comentarioService.createComentario(0L, newEntity);
		});
	}

    /**
     * Prueba para consultar todos los comentarios.
     */
   @Test
	void testGetComentarios() throws EntityNotFoundException {
		List<ComentarioEntity> list = comentarioService.getComentarios(usuarioEntity.getId());
		assertEquals(comentarioList.size(), list.size());
		for (ComentarioEntity entity : list) {
			boolean found = false;
			for (ComentarioEntity storedEntity : comentarioList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

    /**
	 * Prueba para consultar la lista de Comentarios de un usuario que no existe.
	 */
	@Test
	void testGetComentariosUsuarioNoValido() {
		assertThrows(EntityNotFoundException.class, () -> {
			comentarioService.getComentarios(0L);
		});
	}

    /**
     * Prueba para consultar un comentario.
     */
    @Test
	void testGetComentario() throws EntityNotFoundException {
		ComentarioEntity entity = comentarioList.get(0);
		ComentarioEntity resultEntity = comentarioService.getComentario(usuarioEntity.getId(), entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getTexto(), resultEntity.getTexto());
		assertEquals(entity.getCalificacion(), resultEntity.getCalificacion());
	}

    /**
     * Prueba para consultar un comentario de un libro que no existe.
     */
    @Test
	void testGetComentarioUsuarioNoValido() {
		assertThrows(EntityNotFoundException.class, () -> {
			comentarioService.getComentarios(0L);
		});
	}

    /**
     * Prueba para actualizar un comentario. 
     */
    @Test
	void testUpdateComentario() throws EntityNotFoundException {
		ComentarioEntity entity = comentarioList.get(0);
		ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);

		pojoEntity.setId(entity.getId());

		comentarioService.updateComentario(usuarioEntity.getId(), entity.getId(), pojoEntity);

		ComentarioEntity resp = entityManager.find(ComentarioEntity.class, entity.getId());

		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getTexto(), resp.getTexto());
		assertEquals(pojoEntity.getCalificacion(), resp.getCalificacion());
	}

    /**
     * Prueba para actualizar un comentario de un usuario que no existe.
     */
    @Test
	void testUpdateComentarioUsuarioNoValido() {
		assertThrows(EntityNotFoundException.class, ()->{
			ComentarioEntity entity = comentarioList.get(0);
			ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
			pojoEntity.setId(entity.getId());
			comentarioService.updateComentario(0L, entity.getId(), pojoEntity);
		});
	}


    /**
     * Prueba para actualizar un Comentario que no existe de un usuario.
     */
    @Test
	void testUpdateComentarioNoValido(){
		assertThrows(EntityNotFoundException.class, ()->{
			ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
			comentarioService.updateComentario(usuarioEntity.getId(), 0L, pojoEntity);
		});
	}

    /**
     * Prueba para eliminar un Comentario.
	 * @throws IllegalOperationException 
     */
	@Test
	void testDeleteComentario() throws EntityNotFoundException, IllegalOperationException {
		ComentarioEntity entity = comentarioList.get(0);
		comentarioService.deleteComentario(usuarioEntity.getId(), entity.getId());
		ComentarioEntity deleted = entityManager.find(ComentarioEntity.class, entity.getId());
		assertNull(deleted);
	}





    /**
     * Prueba para eliminar un Comentario de un usuario que no existe.
     */
	@Test
	void testDeleteComentarioUsuarioNoValido()  {
		assertThrows(EntityNotFoundException.class, ()->{
			ComentarioEntity entity = comentarioList.get(0);
			comentarioService.deleteComentario(0L, entity.getId());
		});
	}

	/**
     * Prueba para eliminar un Comentario que no existe de un usuario.
     */
	@Test
	void testDeleteComentarioNoValido()  {
		assertThrows(EntityNotFoundException.class, ()->{
			comentarioService.deleteComentario(usuarioEntity.getId(), 0L);
		});
	}
	
	 /**
     * Prueba para eliminarle un comentario a un usuario del cual no pertenece.
     */
	@Test
	void testDeleteComentarioSinUsuarioAsociado() {
		assertThrows(IllegalOperationException.class, () -> {
			
			UsuarioEntity newUsuario =  factory.manufacturePojo(UsuarioEntity.class);
			entityManager.persist(newUsuario);
			
			ComentarioEntity entity = comentarioList.get(0);
			comentarioService.deleteComentario(newUsuario.getId(), entity.getId());
		});
	}
}
