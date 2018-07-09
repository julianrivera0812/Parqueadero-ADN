package co.ceiba.adn.estacionamiento.enumeration;

public enum ResponseCodeEnum {

	SUCCESS(0, ""),
	NOT_ENTRY_BY_DAY(1, "no puede ingresar porque no está en un dia hábil"),
	WITHOUT_SPACE(2,"no puede ingresar porque no hay cupo disponible"),
	BAD_REQUEST(3,"Petición incorrecta"),
	DUPLICATED_ENTRY(4,"Ya existe registro de ingreso del vehiculo"),
	VEHICULE_WITHOUT_ENTRY(5,"No existe registro de ingreso del vehiculo"),
	INVALID_VEHICLE_TYPE(6,"Tipo de vehiculo no valido"),
	INVALID_PAGINATED_PARAMS(7,"Parámetros de paginación incorrectos"),
	GENERAL_ERROR(99, "Error general");

	private int code;

	private String message;

	private ResponseCodeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
