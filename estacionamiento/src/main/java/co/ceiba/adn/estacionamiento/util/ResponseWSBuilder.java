package co.ceiba.adn.estacionamiento.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import co.ceiba.adn.estacionamiento.dto.ResponseDTO;

@Component
public class ResponseWSBuilder {

	public ResponseEntity<ResponseDTO> buildErrorResponse(Exception exception) {
		ResponseDTO response = new ResponseDTO();
		response.setCode(99);
		response.setMessage(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<ResponseDTO> buildBadRequestResponse() {

		ResponseDTO response = new ResponseDTO();
		response.setCode(3);
		response.setMessage("Petición incorrecta");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
