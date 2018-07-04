package co.ceiba.adn.estacionamiento.dto;

public class ResponseDTO {

	private int code;

	private String message;

	public ResponseDTO() {
		this.code = 0;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
