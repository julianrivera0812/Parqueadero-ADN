package co.ceiba.adn.estacionamiento.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.service.ServiceCaller;
import co.ceiba.adn.estacionamiento.util.ResponseWSBuilder;

public abstract class BaseRestController {

	private ResponseWSBuilder responseWSBuilder;

	protected BaseRestController(ResponseWSBuilder responseWSBuilder) {
		this.responseWSBuilder = responseWSBuilder;
	}

	protected ResponseEntity<ResponseDTO> callService(ServiceCaller serviceCaller, Errors errors) {

		ResponseEntity<ResponseDTO> responseEntity = null;

		try {

			if (errors.hasErrors()) {

				responseEntity = responseWSBuilder.buildBadRequestResponse();

			} else {

				responseEntity = new ResponseEntity<>(serviceCaller.callService(), HttpStatus.OK);
			}

		} catch (Exception e) {

			responseEntity = responseWSBuilder.buildErrorResponse(e);
		}

		return responseEntity;
	}

	protected ResponseEntity<ResponseDTO> callService(ServiceCaller serviceCaller) {

		ResponseEntity<ResponseDTO> responseEntity = null;

		try {

			responseEntity = new ResponseEntity<>(serviceCaller.callService(), HttpStatus.OK);

		} catch (Exception e) {

			responseEntity = responseWSBuilder.buildErrorResponse(e);
		}

		return responseEntity;
	}

}
