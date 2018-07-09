package co.ceiba.adn.estacionamiento.restcontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ceiba.adn.estacionamiento.constant.ConfigConstants;
import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.exception.ApplicationException;
import co.ceiba.adn.estacionamiento.model.CarModel;
import co.ceiba.adn.estacionamiento.model.MotorcycleModel;
import co.ceiba.adn.estacionamiento.model.VehicleModel;
import co.ceiba.adn.estacionamiento.service.VehicleControlService;

@RestController
@RequestMapping("/api/vehicle")
@CrossOrigin(origins = ConfigConstants.FRONT_END_URL)
public class VehicleControlController extends BaseRestController {

	private VehicleControlService vehicleControlService;

	@Autowired
	public VehicleControlController(VehicleControlService vehicleControlService) {
		this.vehicleControlService = vehicleControlService;
	}

	@PostMapping("/registerMotorcycleEntry")
	public ResponseEntity<ResponseDTO> registerMotorcycleEntry(
			@Valid @RequestBody(required = true) MotorcycleModel motorcycleModel, Errors errors)
			throws ApplicationException {

		return callService(() -> vehicleControlService.registerVehicleEntry(motorcycleModel), errors);
	}

	@PostMapping("/registerCarEntry")
	public ResponseEntity<ResponseDTO> registerCarEntry(@Valid @RequestBody(required = true) CarModel carModel,
			Errors errors) throws ApplicationException {

		return callService(() -> vehicleControlService.registerVehicleEntry(carModel), errors);
	}

	@PostMapping("/registerVehicleExit")
	public ResponseEntity<ResponseDTO> registerVehicleExit(
			@Valid @RequestBody(required = true) VehicleModel vehicleModel, Errors errors) throws ApplicationException {

		return callService(() -> vehicleControlService.registerVehicleExit(vehicleModel), errors);
	}

	@GetMapping("/getVehicleInParking")
	public ResponseEntity<ResponseDTO> getVehicleInParking(@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) throws ApplicationException {

		return callService(() -> vehicleControlService.getVehicleInParking(page, size));
	}

}
