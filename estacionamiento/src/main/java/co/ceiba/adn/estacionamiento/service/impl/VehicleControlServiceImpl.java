package co.ceiba.adn.estacionamiento.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import co.ceiba.adn.estacionamiento.constant.RateConstants;
import co.ceiba.adn.estacionamiento.constant.VehicleLimitConstants;
import co.ceiba.adn.estacionamiento.converter.VehicleConverter;
import co.ceiba.adn.estacionamiento.dto.GetVehicleInParkingPaginatedDTO;
import co.ceiba.adn.estacionamiento.dto.RegisterExitOutDTO;
import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.entity.Motorcycle;
import co.ceiba.adn.estacionamiento.entity.Vehicle;
import co.ceiba.adn.estacionamiento.entity.Vehicle.VehicleTypeEnum;
import co.ceiba.adn.estacionamiento.entity.VehicleControl;
import co.ceiba.adn.estacionamiento.enumeration.ResponseCodeEnum;
import co.ceiba.adn.estacionamiento.exception.ApplicationException;
import co.ceiba.adn.estacionamiento.model.VehicleModel;
import co.ceiba.adn.estacionamiento.repository.VehicleControlRepository;
import co.ceiba.adn.estacionamiento.repository.VehicleRepository;
import co.ceiba.adn.estacionamiento.service.VehicleControlService;
import co.ceiba.adn.estacionamiento.util.DateValidator;

@Service
public class VehicleControlServiceImpl implements VehicleControlService {

	private static final String LETRA_A_NO_PERMITIDA = "A";

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

	public ResponseDTO registerVehicleEntry(VehicleModel vehicleModel) throws ApplicationException {

		Date currentDate = new Date();

		Vehicle vehicle = vehicleConverter.modelToEntity(vehicleModel);

		doEntryValidations(vehicleModel, currentDate, vehicle);

		saveRegisterEntryData(vehicleModel, currentDate, vehicle);

		return new ResponseDTO();
	}

	private void doEntryValidations(VehicleModel vehicleModel, Date currentDate, Vehicle vehicle)
			throws ApplicationException {

		if (vehicleControlRepository.existsByDepartureDateIsNullAndVehiclePlate(vehicle.getPlate())) {
			throw new ApplicationException(ResponseCodeEnum.DUPLICATED_ENTRY);
		}

		if (!hasSpaceForVehicle(vehicle)) {
			throw new ApplicationException(ResponseCodeEnum.WITHOUT_SPACE);
		}

		if (!isEnableDayByPlate(vehicleModel.getPlate(), currentDate)) {
			throw new ApplicationException(ResponseCodeEnum.NOT_ENTRY_BY_DAY);
		}
	}

	private void saveRegisterEntryData(VehicleModel vehicleModel, Date currentDate, Vehicle vehicle) {
		if (!vehicleRepository.existsById(vehicleModel.getPlate())) {
			vehicle = vehicleRepository.save(vehicle);
		}

		vehicleControlRepository.save(new VehicleControl(vehicle, currentDate));
	}

	public RegisterExitOutDTO registerVehicleExit(VehicleModel vehicleModel) throws ApplicationException {

		Date currentDate = new Date();

		RegisterExitOutDTO response = new RegisterExitOutDTO();

		VehicleControl vehicleControl = vehicleControlRepository
				.findOneByDepartureDateIsNullAndVehiclePlate(vehicleModel.getPlate());

		if (vehicleControl != null) {

			boolean hasIncrement = hasIncrement(vehicleControl.getVehicle());

			BigDecimal paymentValue = calculatePaymentByDates(vehicleControl.getEntryDate(), currentDate,
					vehicleControl.getVehicle().getTypeEnum(), hasIncrement);

			vehicleControl.setDepartureDate(currentDate);
			vehicleControl.setPaymentValue(paymentValue);
			vehicleControlRepository.save(vehicleControl);

			response.setPaymentValue(paymentValue);
		} else {
			throw new ApplicationException(ResponseCodeEnum.VEHICULE_WITHOUT_ENTRY);
		}

		return response;
	}

	public boolean isEnableDayByPlate(String plate, Date date) {

		boolean result = true;

		if (plate.startsWith(LETRA_A_NO_PERMITIDA) && !DateValidator.isMondayOrSunday(date)) {
			result = false;
		}

		return result;
	}

	public long countVehicleInParkingByType(Class<? extends Vehicle> vehicleType) {
		return vehicleControlRepository.countByDepartureDateIsNull(vehicleType).orElse(0L);
	}

	public boolean hasSpaceForVehicle(Vehicle vehicle) throws ApplicationException {

		int maxValue = getMaxAmountVehicle(vehicle.getTypeEnum());

		return countVehicleInParkingByType(vehicle.getClass()) < maxValue;
	}

	public BigDecimal calculatePaymentByVehicleType(VehicleTypeEnum vehicleType, int days, int hours,
			boolean hasIncrement) throws ApplicationException {

		BigDecimal paymentValue = null;

		switch (vehicleType) {
		case CAR:
			paymentValue = calculateBasePayment(days, hours, RateConstants.CAR_DAY_VALUE, RateConstants.CAR_HOUR_VALUE);
			break;

		case MOTORCYCLE:
			paymentValue = calculateBasePayment(days, hours, RateConstants.MOTORCYCLE_DAY_VALUE,
					RateConstants.MOTORCYCLE_HOUR_VALUE);

			paymentValue = calculateIncrement(hasIncrement, paymentValue);
			break;

		default:
			throw new ApplicationException(ResponseCodeEnum.INVALID_VEHICLE_TYPE);
		}

		return paymentValue;
	}

	public int calculateTotalHours(Date entryDate, Date departureDate) {

		long timeMiliseconds = departureDate.getTime() - entryDate.getTime();

		double hours = timeMiliseconds / (3600000.0);
		return (int) Math.ceil(hours);
	}

	public BigDecimal calculatePaymentByDates(Date entryDate, Date departureDate, VehicleTypeEnum vehicleType,
			boolean hasIncrement) throws ApplicationException {

		int totalHours = calculateTotalHours(entryDate, departureDate);

		int days = totalHours / 24;
		int lessHours = totalHours % 24;
		if (lessHours >= 9) {
			days++;
			lessHours = 0;
		}

		return calculatePaymentByVehicleType(vehicleType, days, lessHours, hasIncrement);
	}

	private BigDecimal calculateIncrement(boolean hasIncrement, BigDecimal paymentValue) {
		if (hasIncrement) {
			paymentValue = paymentValue.add(RateConstants.INCREMENT_MOTORCYCLE_PAYMENT);
		}
		return paymentValue;
	}

	private int getMaxAmountVehicle(VehicleTypeEnum vehicleType) throws ApplicationException {
		int maxAmount = 0;

		switch (vehicleType) {
		case CAR:
			maxAmount = VehicleLimitConstants.MAX_AMOUNT_CAR;
			break;
		case MOTORCYCLE:
			maxAmount = VehicleLimitConstants.MAX_AMOUNT_MOTORCYCLE;
			break;
		default:
			throw new ApplicationException(ResponseCodeEnum.INVALID_VEHICLE_TYPE);
		}
		return maxAmount;
	}

	private BigDecimal calculateBasePayment(int days, int hours, BigDecimal dayValue, BigDecimal hourValue) {

		BigDecimal daysPayment = new BigDecimal(days).multiply(dayValue);

		BigDecimal hoursPayment = new BigDecimal(hours).multiply(hourValue);

		return daysPayment.add(hoursPayment);
	}

	private boolean hasIncrement(Vehicle vehicle) {
		return VehicleTypeEnum.MOTORCYCLE.equals(vehicle.getTypeEnum())
				&& ((Motorcycle) vehicle).getCylinderCapacity() > VehicleLimitConstants.LIMIT_CC_FOR_INCREMENT;
	}

	public GetVehicleInParkingPaginatedDTO getVehicleInParking(Integer page, Integer size) throws ApplicationException {

		GetVehicleInParkingPaginatedDTO response = new GetVehicleInParkingPaginatedDTO();

		if (page == null || size == null || page < 0 || size < 0) {
			throw new ApplicationException(ResponseCodeEnum.INVALID_PAGINATED_PARAMS);
		} else {
			response.setPage(vehicleControlRepository.findByDepartureDateIsNull(PageRequest.of(page, size)));
		}

		return response;
	}

}
