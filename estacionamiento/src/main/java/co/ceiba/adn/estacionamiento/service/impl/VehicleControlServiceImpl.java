package co.ceiba.adn.estacionamiento.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.ceiba.adn.estacionamiento.converter.VehicleConverter;
import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.entity.Vehicle;
import co.ceiba.adn.estacionamiento.entity.VehicleControl;
import co.ceiba.adn.estacionamiento.model.VehicleModel;
import co.ceiba.adn.estacionamiento.entity.Vehicle.VehicleTypeEnum;
import co.ceiba.adn.estacionamiento.repository.VehicleControlRepository;
import co.ceiba.adn.estacionamiento.repository.VehicleRepository;
import co.ceiba.adn.estacionamiento.service.VehicleControlService;
import co.ceiba.adn.estacionamiento.util.DateValidator;

@Service("vehicleControlService")
public class VehicleControlServiceImpl implements VehicleControlService {

	private static final int MAX_AMOUNT_MOTORCYCLE = 10;

	private static final int MAX_AMOUNT_CAR = 20;

	private VehicleRepository vehicleRepository;

	private VehicleControlRepository vehicleControlRepository;

	private VehicleConverter vehicleConverter;

	@Autowired
	public VehicleControlServiceImpl(VehicleRepository vehicleRepository,
			VehicleControlRepository vehicleControlRepository, VehicleConverter vehicleConverter) {
		this.vehicleRepository = vehicleRepository;
		this.vehicleControlRepository = vehicleControlRepository;
		this.vehicleConverter = vehicleConverter;
	}

	public ResponseDTO registerVehicleEntry(VehicleModel vehicleModel) {

		Date currentDate = new Date();

		Vehicle vehicle = vehicleConverter.modelToEntity(vehicleModel);

		ResponseDTO response = new ResponseDTO();

		if (hasSpaceForVehicle(vehicle)) {

			if (isEnableDayByPlate(vehicleModel.getPlate(), currentDate)) {

				saveRegisterEntryData(vehicleModel, currentDate, vehicle);

			} else {

				response.setCode(1);
				response.setMessage("no puede ingresar porque no está en un dia hábil");
			}

		} else {
			response.setCode(2);
			response.setMessage("no puede ingresar porque no hay cupo disponible");
		}

		return response;
	}

	private void saveRegisterEntryData(VehicleModel vehicleModel, Date currentDate, Vehicle vehicle) {
		if (!vehicleRepository.existsById(vehicleModel.getPlate())) {
			vehicle = vehicleRepository.save(vehicle);
		}

		vehicleControlRepository.save(new VehicleControl(vehicle, currentDate));
	}

	public boolean isEnableDayByPlate(String plate, Date date) {

		boolean result = true;

		if (plate.startsWith("A") && !DateValidator.isMondayOrSunday(date)) {
			result = false;
		}

		return result;
	}

	public long countVehicleInParkingByType(Class<? extends Vehicle> vehicleType) {
		return vehicleControlRepository.countByDepartureDateIsNull(vehicleType).orElse(0L);
	}

	public boolean hasSpaceForVehicle(Vehicle vehicle) {

		int maxValue = VehicleTypeEnum.MOTORCYCLE.equals(vehicle.getType()) ? MAX_AMOUNT_MOTORCYCLE
				: MAX_AMOUNT_CAR;

		return countVehicleInParkingByType(vehicle.getClass()) < maxValue;
	}

}
