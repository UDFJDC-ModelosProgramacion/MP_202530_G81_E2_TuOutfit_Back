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

import co.edu.udistrital.mdp.back.entities.UsuarioEntity;
import co.edu.udistrital.mdp.back.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.back.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(UsuarioService.class)
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<UsuarioEntity> usuarioList = new ArrayList<>();

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
        entityManager.getEntityManager().createQuery("delete from ListaDeseosEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();        
    }

    /**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
    private void insertData(){
        for (int i = 0; i < 3; i++) {
            UsuarioEntity usuarioEntity = factory.manufacturePojo(UsuarioEntity.class);
            if (usuarioEntity.getCorreo() == null) usuarioEntity.setCorreo("user" + i + "@test.com");
            entityManager.persist(usuarioEntity);
            usuarioList.add(usuarioEntity);
        }
    }

    /**
	 * Prueba para crear un Usuario.
	 * @throws IllegalOperationException 
	 */
    @Test
    void testCreateUsuarioCorrecto() throws IllegalOperationException {
        UsuarioEntity toCreate = factory.manufacturePojo(UsuarioEntity.class);
        toCreate.setNombre("Nicolas Vargas");
        toCreate.setCorreo("nivargas@test.com");
        UsuarioEntity result = usuarioService.createUsuario(toCreate);
        assertNotNull(result);
        assertEquals("Nicolas Vargas", result.getNombre());
        assertEquals("nivargas@test.com", result.getCorreo());
    }

    /**
	 * Prueba para crear un usuario con un correo vacio.
	 * @throws IllegalOperationException 
	 */
    @Test
    void testCreateUsuarioCorreoVacio() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity toCreate = factory.manufacturePojo(UsuarioEntity.class);
            toCreate.setNombre("");
            toCreate.setCorreo("");
            usuarioService.createUsuario(toCreate);
        });
    }

    /**
	 * Prueba para consultar la lista de usuarios.
	 */
    @Test
    void testGetUsuarios() {
        List<UsuarioEntity> usuarioList = usuarioService.getUsuarios();
        assertEquals(usuarioList.size(), usuarioList.size());
    }

    /**
	 * Prueba para consultar un usuario.
	 */
    @Test
    void testGetUsuarioExistente() throws EntityNotFoundException {
        UsuarioEntity usuarioEntity = usuarioList.get(0);
        UsuarioEntity result = usuarioService.getUsuario(usuarioEntity.getId());
        assertEquals(usuarioEntity.getNombre(), result.getNombre());
        assertEquals(usuarioEntity.getCorreo(), result.getCorreo());
    }

    /**
	 * Prueba para consultar un usuario que no existe.
	 */
    @Test
    void testGetUsuarioNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> usuarioService.getUsuario(0L));
    }

    /**
	 * Prueba para actualizar un usuario.
	 */
    @Test
    void testUpdateUsuarioCorrecto() throws EntityNotFoundException, IllegalOperationException {
        UsuarioEntity entity = usuarioList.get(0);
        UsuarioEntity update = factory.manufacturePojo(UsuarioEntity.class);
        update.setNombre("Nombre actualizado");
        update.setCorreo("updated@test.com");

        UsuarioEntity result = usuarioService.updateUsuario(entity.getId(), update);
        assertEquals("Nombre actualizado", result.getNombre());
        assertEquals("updated@test.com", result.getCorreo());
    }

    /**
	 * Prueba para actualizar un usuario que no existe.
	 */
    @Test
    void testUpdateUsuarioCorreoVacio() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity entity = usuarioList.get(0);
            UsuarioEntity update = factory.manufacturePojo(UsuarioEntity.class);
            update.setNombre("");
            update.setCorreo("");
            usuarioService.updateUsuario(entity.getId(), update);
        });
    }

    /**
	 * Prueba para eliminar un usuario
	 */
    @Test
    void testDeleteUsuarioCorrecto() throws EntityNotFoundException, IllegalOperationException {
        UsuarioEntity entity = usuarioList.get(0);
        usuarioService.deleteUsuario(entity.getId());
        assertNull(entityManager.find(UsuarioEntity.class, entity.getId()));
    }

    /**
	 * Prueba para eliminar un usuario que no existe
	 */
    @Test
    void testDeleteUsuarioNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> usuarioService.deleteUsuario(0L));
    }
}
