package co.ceiba.adn.estacionamiento.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.model.CarModel;
import co.ceiba.adn.estacionamiento.model.MotorcycleModel;
import co.ceiba.adn.estacionamiento.service.VehicleControlService;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleControlController {

	private VehicleControlService vehicleControlService;

	@Autowired
	public VehicleControlController(VehicleControlService vehicleControlService) {
		this.vehicleControlService = vehicleControlService;
	}

	@PostMapping("/registerMotorcycleEntry")
	public ResponseDTO registerMotorcycleEntry(@RequestBody(required = true) MotorcycleModel motorcycleModel) {

		return vehicleControlService.registerVehicleEntry(motorcycleModel);
	}

	@PostMapping("/registerCarEntry")
	public ResponseDTO registerCarEntry(@RequestBody(required = true) CarModel carModel) {

		return vehicleControlService.registerVehicleEntry(carModel);
	}
}
