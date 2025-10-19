package co.edu.udistrital.mdp.back.exceptions;

public class ErrorMessage {
    public static final String CATEGORIA_NOT_FOUND ="La categoria con el id dado no fue encontrado";
    public static final String COLOR_NOT_FOUND ="El color con el id dado no fue encontrado";
    public static final String COMENTARIO_NOT_FOUND ="El comentario con el id dado no fue encontrado";
    public static final String IMAGEN_NOT_FOUND ="La imagen con el id dado no fue encontrado";
    public static final String LISTADESEOS_NOT_FOUND ="La lista de deseos con el id dado no fue encontrado";
    public static final String MARCA_NOT_FOUND ="La marca con el id dado no fue encontrado";
    public static final String OCASION_NOT_FOUND ="La ocasión con el id dado no fue encontrado";
    public static final String OUTFIT_NOT_FOUND ="El outfit con el id dado no fue encontrado";
    public static final String PRENDA_NOT_FOUND ="La prenda con el id dado no fue encontrado";
    public static final String RECOMENDACION_NOT_FOUND ="La recomendación con el id dado no fue encontrado";
    public static final String TIENDA_NOT_FOUND ="La tienda con el id dado no fue encontrado";
    public static final String USUARIO_NOT_FOUND ="El usuario con el id dado no fue encontrado";
    public static final String COMENTARIO_NO_ASOCIADO_A_USUARIO ="El comentario no esta asociado con el usuario";
    
    private ErrorMessage() {
		throw new IllegalStateException("Clase de utilidad");
	}
}
