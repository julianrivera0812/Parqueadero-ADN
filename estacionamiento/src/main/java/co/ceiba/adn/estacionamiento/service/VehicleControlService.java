package co.ceiba.adn.estacionamiento.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.Valid;

import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.entity.Vehicle;
import co.ceiba.adn.estacionamiento.entity.Vehicle.VehicleTypeEnum;
import co.ceiba.adn.estacionamiento.model.VehicleModel;

public interface VehicleControlService {

	public ResponseDTO registerVehicleEntry(@Valid VehicleModel vehicleModel);

	public boolean isEnableDayByPlate(String plate, Date date);

	public long countVehicleInParkingByType(Class<? extends Vehicle> vehicleType);

	public boolean hasSpaceForVehicle(Vehicle vehicle);

	public BigDecimal calculatePayment(VehicleTypeEnum vehicleType, int days, int hours, boolean hasIncrement);

	public int calculateTotalHours(Date entryDate, Date departureDate);

	public BigDecimal calculatePayment(Date entryDate, Date departureDate, VehicleTypeEnum vehicleType,
			boolean hasIncrement);

	public ResponseDTO registerVehicleExit(VehicleModel vehicleModel);

}
