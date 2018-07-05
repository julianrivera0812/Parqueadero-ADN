package co.ceiba.adn.estacionamiento.dto;

import co.ceiba.adn.estacionamiento.enumeration.ResponseCodeEnum;

public class ResponseDTO {

	private int code;

	private String message;

	public ResponseDTO() {
		this.code = ResponseCodeEnum.SUCCESS.getCode();
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
