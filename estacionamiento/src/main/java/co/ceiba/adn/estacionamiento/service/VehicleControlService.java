package co.ceiba.adn.estacionamiento.service;

import java.util.Date;

import javax.validation.Valid;

import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.entity.Vehicle.VehicleTypeEnum;
import co.ceiba.adn.estacionamiento.model.VehicleModel;

public interface VehicleControlService {

	public ResponseDTO registerVehicleEntry(@Valid VehicleModel vehicleModel);

	public boolean isEnableDay(String plate, Date date);

	public long countVehicleInParkingByType(VehicleTypeEnum vehicleType);

	public boolean hasSpaceForVehicle(VehicleTypeEnum vehicleType);

}
