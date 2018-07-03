package co.ceiba.adn.estacionamiento.restcontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.model.CarModel;
import co.ceiba.adn.estacionamiento.model.MotorcycleModel;
import co.ceiba.adn.estacionamiento.model.VehicleModel;
import co.ceiba.adn.estacionamiento.service.VehicleControlService;
import co.ceiba.adn.estacionamiento.util.ResponseWSBuilder;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleControlController {

	private VehicleControlService vehicleControlService;

	private ResponseWSBuilder responseWSUtil;

	@Autowired
	public VehicleControlController(VehicleControlService vehicleControlService, ResponseWSBuilder responseWSUtil) {
		this.vehicleControlService = vehicleControlService;
		this.responseWSUtil = responseWSUtil;
	}

	@PostMapping("/registerMotorcycleEntry")
	public ResponseEntity<ResponseDTO> registerMotorcycleEntry(
			@Valid @RequestBody(required = true) MotorcycleModel motorcycleModel, Errors errors) {

		try {
			return responseWSUtil.buildResponse(vehicleControlService.registerVehicleEntry(motorcycleModel), errors);

		} catch (Exception e) {

			return responseWSUtil.buildErrorResponse(e);
		}
	}

	@PostMapping("/registerCarEntry")
	public ResponseEntity<ResponseDTO> registerCarEntry(@Valid @RequestBody(required = true) CarModel carModel,
			Errors errors) {

		try {

			return responseWSUtil.buildResponse(vehicleControlService.registerVehicleEntry(carModel), errors);

		} catch (Exception e) {

			return responseWSUtil.buildErrorResponse(e);
		}
	}

	@PostMapping("/registerVehicleExit")
	public ResponseEntity<ResponseDTO> registerVehicleExit(
			@Valid @RequestBody(required = true) VehicleModel vehicleModel, Errors errors) {

		try {

			return responseWSUtil.buildResponse(vehicleControlService.registerVehicleExit(vehicleModel), errors);

		} catch (Exception e) {

			return responseWSUtil.buildErrorResponse(e);
		}
	}

}
