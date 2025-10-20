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
import co.edu.udistrital.mdp.back.entities.ListaDeseosEntity;
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
        usuarioList = usuarioService.getUsuarios();
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
    void testDeleteUsuarioCorrecto() throws EntityNotFoundException {
        UsuarioEntity entity = usuarioList.get(0);

        usuarioService.deleteUsuario(entity.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.getUsuario(entity.getId());
        });
}

    /**
	 * Prueba para eliminar un usuario que no existe
	 */
    @Test
    void testDeleteUsuarioNoExistente() {
        assertThrows(EntityNotFoundException.class, () -> usuarioService.deleteUsuario(0L));
    }

    // Relación Usuario - Comentarios

    /**
     * Prueba para agregar un comentario a un usuario existente.
     */
    @Test
    void testAddComentarioCorrecto() throws Exception {
        UsuarioEntity usuario = usuarioList.get(0);
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setTexto("Excelente usuario");
        comentario.setUsuario(usuario);

        ComentarioEntity creado = usuarioService.addComentario(usuario.getId(), comentario);

        assertNotNull(creado);
        assertEquals("Excelente usuario", creado.getTexto());
    }

    /**
     * Prueba para agregar un comentario con texto vacío.
     */
    @Test
    void testAddComentarioTextoVacio() {
        UsuarioEntity usuario = usuarioList.get(0);
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setTexto("   ");

        assertThrows(IllegalOperationException.class, () ->
            usuarioService.addComentario(usuario.getId(), comentario));
    }

    /**
     * Prueba para agregar un comentario a un usuario inexistente.
     */
    @Test
    void testAddComentarioUsuarioNoExiste() {
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setTexto("Buen servicio");

        assertThrows(EntityNotFoundException.class, () ->
            usuarioService.addComentario(999L, comentario));
    }

    /**
     * Prueba para obtener los comentarios de un usuario.
     */
    @Test
    void testGetComentariosCorrecto() throws Exception {
        UsuarioEntity usuario = usuarioList.get(0);

        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setTexto("Comentario de prueba");
        comentario.setUsuario(usuario); // <-- lado del comentario

        // Muy importante: agregarlo también en la lista del usuario
        usuario.getComentarios().add(comentario); // <-- lado del usuario

        entityManager.persist(usuario);
        entityManager.flush();

        List<ComentarioEntity> comentarios = usuarioService.getComentarios(usuario.getId());
        assertFalse(comentarios.isEmpty());
    }

    /**
     * Prueba para obtener los comentarios de un usuario inexistente.
     */
    @Test
    void testGetComentariosUsuarioNoExiste() {
        assertThrows(EntityNotFoundException.class, () ->
            usuarioService.getComentarios(999L));
    }


    // Relación Usuario - ListaDeseos

    /**
     * Prueba para asignar una lista de deseos a un usuario existente.
     */
    @Test
    void testSetListaDeseosCorrecto() throws Exception {
        UsuarioEntity usuario = usuarioList.get(0);

        ListaDeseosEntity lista = new ListaDeseosEntity();

        ListaDeseosEntity creada = usuarioService.setListaDeseos(usuario.getId(), lista);

        assertNotNull(creada);
        assertEquals(usuario.getId(), creada.getUsuario().getId());
    }

    /**
     * Prueba para asignar una lista de deseos a un usuario inexistente.
     */
    @Test
    void testSetListaDeseosUsuarioNoExiste() {
        ListaDeseosEntity lista = new ListaDeseosEntity();

        assertThrows(EntityNotFoundException.class, () ->
            usuarioService.setListaDeseos(999L, lista));
    }

    /**
     * Prueba para consultar la lista de deseos de un usuario existente.
     */
    @Test
    void testGetListaDeseosCorrecto() throws Exception {
        UsuarioEntity usuario = usuarioList.get(0);
        ListaDeseosEntity lista = new ListaDeseosEntity();
        lista.setUsuario(usuario);
        usuario.setWishlist(lista);
        entityManager.persist(lista);

        ListaDeseosEntity result = usuarioService.getListaDeseos(usuario.getId());
        assertNotNull(result);
        assertEquals(usuario.getId(), result.getUsuario().getId());
    }

    /**
     * Prueba para consultar la lista de deseos de un usuario inexistente.
     */
    @Test
    void testGetListaDeseosUsuarioNoExiste() {
        assertThrows(EntityNotFoundException.class, () ->
            usuarioService.getListaDeseos(999L));
    }
}
