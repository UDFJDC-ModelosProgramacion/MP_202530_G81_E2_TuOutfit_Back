package co.edu.udistrital.mdp.back.exceptions;

public final class ErrorMessage {
	public static final String OUTFIT_NOT_FOUND = "The outfit with the given id was not found";
	public static final String CATEGORIA_NOT_FOUND = "The categoria with the given id was not found";
	public static final String IMAGEN_NOT_FOUND = "The imagen with the given id was not found";
    public static final String PRENDA_NOT_FOUND = "The PRENDA with the given id was not found";

	private ErrorMessage() {
		throw new IllegalStateException("Utility class");
	}
}