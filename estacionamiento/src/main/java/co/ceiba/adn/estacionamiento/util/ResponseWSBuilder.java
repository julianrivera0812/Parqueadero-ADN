package co.ceiba.adn.estacionamiento.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.enumeration.ResponseCodeEnum;

@Component
public class ResponseWSBuilder {

	public ResponseEntity<ResponseDTO> buildErrorResponse(Exception exception) {

		ResponseDTO response = new ResponseDTO();

		if (exception instanceof IllegalArgumentException) {

			response.setCode(ResponseCodeEnum.BAD_REQUEST.getCode());
			response.setMessage(exception.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		response.setCode(ResponseCodeEnum.GENERAL_ERROR.getCode());
		response.setMessage(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<ResponseDTO> buildBadRequestResponse() {

		ResponseDTO response = new ResponseDTO();
		response.setCode(ResponseCodeEnum.BAD_REQUEST.getCode());
		response.setMessage(ResponseCodeEnum.BAD_REQUEST.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
