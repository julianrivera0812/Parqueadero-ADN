package co.ceiba.adn.estacionamiento.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.enumeration.ResponseCodeEnum;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = ApplicationException.class)
	protected ResponseEntity<Object> handleApplicationException(ApplicationException applicationException,
			WebRequest request) {

		ResponseDTO response = new ResponseDTO();
		response.setCode(applicationException.getResponseCodeEnum().getCode());
		response.setMessage(applicationException.getResponseCodeEnum().getMessage());

		return handleExceptionInternal(applicationException, response, new HttpHeaders(), HttpStatus.BAD_REQUEST,
				request);
	}

	@ExceptionHandler(value = Exception.class)
	protected ResponseEntity<Object> handleGenericException(Exception exception, WebRequest request) {

		ResponseDTO response = new ResponseDTO();
		response.setCode(ResponseCodeEnum.GENERAL_ERROR.getCode());
		response.setMessage(exception.getMessage());

		return handleExceptionInternal(exception, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
				request);
	}
}
