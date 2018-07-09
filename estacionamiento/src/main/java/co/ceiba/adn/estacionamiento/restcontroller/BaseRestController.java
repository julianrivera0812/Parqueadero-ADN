package co.ceiba.adn.estacionamiento.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.enumeration.ResponseCodeEnum;
import co.ceiba.adn.estacionamiento.exception.ApplicationException;
import co.ceiba.adn.estacionamiento.service.ServiceCaller;

public abstract class BaseRestController {

	protected ResponseEntity<ResponseDTO> callService(ServiceCaller serviceCaller, Errors errors)
			throws ApplicationException {

		ResponseEntity<ResponseDTO> responseEntity = null;

		if (errors.hasErrors()) {

			throw new ApplicationException(ResponseCodeEnum.BAD_REQUEST);

		} else {

			responseEntity = new ResponseEntity<>(serviceCaller.callService(), HttpStatus.OK);
		}

		return responseEntity;
	}

	protected ResponseEntity<ResponseDTO> callService(ServiceCaller serviceCaller) throws ApplicationException {

		return new ResponseEntity<>(serviceCaller.callService(), HttpStatus.OK);
	}

}
