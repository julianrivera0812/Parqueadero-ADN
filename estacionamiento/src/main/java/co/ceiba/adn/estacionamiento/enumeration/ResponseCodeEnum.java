package co.ceiba.adn.estacionamiento.enumeration;

public enum ResponseCodeEnum {

	SUCCESS(0, ""), NOT_ENTRY_BY_DAY(1, "no puede ingresar porque no está en un dia hábil"), WITHOUT_SPACE(2,
			"no puede ingresar porque no hay cupo disponible"), BAD_REQUEST(3,
					"Petición incorrecta"), GENERAL_ERROR(99, "Error general");

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
