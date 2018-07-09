package co.ceiba.adn.estacionamiento.exception;

import co.ceiba.adn.estacionamiento.enumeration.ResponseCodeEnum;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = 1L;

	private final ResponseCodeEnum responseCodeEnum;

	public ApplicationException(ResponseCodeEnum responseCodeEnum) {
		this.responseCodeEnum = responseCodeEnum;
	}

	public ResponseCodeEnum getResponseCodeEnum() {
		return responseCodeEnum;
	}
}
