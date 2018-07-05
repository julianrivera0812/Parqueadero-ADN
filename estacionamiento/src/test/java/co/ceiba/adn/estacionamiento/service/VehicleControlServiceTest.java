package co.ceiba.adn.estacionamiento.service;

import static co.ceiba.adn.estacionamiento.builders.CarBuilder.aCar;
import static co.ceiba.adn.estacionamiento.builders.MotorcycleBuilder.aMotorcycle;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import co.ceiba.adn.estacionamiento.converter.VehicleConverter;
import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.entity.Car;
import co.ceiba.adn.estacionamiento.entity.Motorcycle;
import co.ceiba.adn.estacionamiento.entity.Vehicle.VehicleTypeEnum;
import co.ceiba.adn.estacionamiento.entity.VehicleControl;
import co.ceiba.adn.estacionamiento.enumeration.ResponseCodeEnum;
import co.ceiba.adn.estacionamiento.model.CarModel;
import co.ceiba.adn.estacionamiento.model.MotorcycleModel;
import co.ceiba.adn.estacionamiento.model.VehicleModel;
import co.ceiba.adn.estacionamiento.repository.VehicleControlRepository;
import co.ceiba.adn.estacionamiento.repository.VehicleRepository;
import co.ceiba.adn.estacionamiento.service.impl.VehicleControlServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleControlServiceTest {

	@Mock
	private VehicleRepository vehicleRepository;

	@Mock
	private VehicleControlRepository vehicleControlRepository;

	@Mock
	private VehicleConverter vehicleConverter;

	@InjectMocks
	private VehicleControlService vehicleControlService = new VehicleControlServiceImpl(vehicleRepository,
			vehicleControlRepository, vehicleConverter);

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");

	@Test
	public void isEnabledDayPlateStartsA() {

		// Arrange
		String plate = "ASD123";
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, Calendar.JUNE, 25);

		// Act
		boolean result = vehicleControlService.isEnableDayByPlate(plate, calendar.getTime());

		// Assert
		Assert.assertTrue(result);
	}

	@Test
	public void isEnabledDayPlateNotStartsA() {

		// Arrange
		String plate = "BSD123";
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, Calendar.JUNE, 25);

		// Act
		boolean result = vehicleControlService.isEnableDayByPlate(plate, calendar.getTime());

		// Assert
		Assert.assertTrue(result);
	}

	@Test
	public void isDisabledDayPlateStartsA() {

		// Arrange
		String plate = "ASD123";
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, Calendar.JUNE, 27);

		// Act
		boolean result = vehicleControlService.isEnableDayByPlate(plate, calendar.getTime());

		// Assert
		Assert.assertFalse(result);
	}

	@Test
	public void withoutSpaceForMotorcycle() {

		// Arrange
		MotorcycleModel motorcycleModel = aMotorcycle().withPlate("BTP88D").build();

		when(vehicleRepository.save(any()))
				.thenReturn(new Motorcycle(motorcycleModel.getPlate(), motorcycleModel.getCylinderCapacity()));
		when(vehicleRepository.existsById(motorcycleModel.getPlate())).thenReturn(true);

		when(vehicleControlRepository.save(any())).thenReturn(new VehicleControl());
		when(vehicleControlRepository.countByDepartureDateIsNull(Motorcycle.class)).thenReturn(Optional.of(11L));

		when(vehicleConverter.modelToEntity(any())).thenCallRealMethod();

		// Act
		ResponseDTO response = vehicleControlService.registerVehicleEntry(motorcycleModel);

		// Assert
		Assert.assertEquals(ResponseCodeEnum.WITHOUT_SPACE.getCode(), response.getCode());
	}

	@Test
	public void withoutSpaceForCar() {

		// Arrange
		CarModel carModel = aCar().withPlate("BTP88D").build();

		when(vehicleRepository.save(any())).thenReturn(new Car(carModel.getPlate()));
		when(vehicleRepository.existsById(carModel.getPlate())).thenReturn(true);

		when(vehicleControlRepository.save(any())).thenReturn(new VehicleControl());
		when(vehicleControlRepository.countByDepartureDateIsNull(Car.class)).thenReturn(Optional.of(21L));

		when(vehicleConverter.modelToEntity(any())).thenCallRealMethod();

		// Act
		ResponseDTO response = vehicleControlService.registerVehicleEntry(carModel);

		// Assert
		Assert.assertEquals(ResponseCodeEnum.WITHOUT_SPACE.getCode(), response.getCode());
	}

	@Test
	public void vehicleEntryPlateNoStartWithA() {

		// Arrange
		MotorcycleModel motorcycleModel = aMotorcycle().withPlate("BTP88D").build();

		when(vehicleRepository.save(any()))
				.thenReturn(new Motorcycle(motorcycleModel.getPlate(), motorcycleModel.getCylinderCapacity()));
		when(vehicleRepository.existsById(motorcycleModel.getPlate())).thenReturn(true);

		when(vehicleControlRepository.save(any())).thenReturn(new VehicleControl());
		when(vehicleControlRepository.countByDepartureDateIsNull(Motorcycle.class)).thenReturn(Optional.of(0L));

		when(vehicleConverter.modelToEntity(any())).thenCallRealMethod();

		// Act
		ResponseDTO response = vehicleControlService.registerVehicleEntry(motorcycleModel);

		// Assert
		Assert.assertEquals(ResponseCodeEnum.SUCCESS.getCode(), response.getCode());
	}

	@Test
	public void calculatePaymentCarBy8Hours() {

		// Arrange
		int hours = 8;

		// Act
		BigDecimal paymentValue = vehicleControlService.calculatePaymentByVehicleType(VehicleTypeEnum.CAR, 0, hours,
				false);

		// Assert
		Assert.assertEquals(new BigDecimal(8000), paymentValue);
	}

	@Test
	public void calculatePaymentCarBy5Hours() {

		// Arrange
		int hours = 5;

		// Act
		BigDecimal paymentValue = vehicleControlService.calculatePaymentByVehicleType(VehicleTypeEnum.CAR, 0, hours,
				false);

		// Assert
		Assert.assertEquals(new BigDecimal(5000), paymentValue);
	}

	@Test
	public void calculatePaymentCarBy1DayAnd3Hours() {

		// Arrange
		int hours = 3;
		int days = 1;

		// Act
		BigDecimal paymentValue = vehicleControlService.calculatePaymentByVehicleType(VehicleTypeEnum.CAR, days, hours,
				false);

		// Assert
		Assert.assertEquals(new BigDecimal(11000), paymentValue);
	}

	@Test
	public void calculatePaymentCarBy2DayAnd7Hours() {

		// Arrange
		int hours = 7;
		int days = 2;

		// Act
		BigDecimal paymentValue = vehicleControlService.calculatePaymentByVehicleType(VehicleTypeEnum.CAR, days, hours,
				false);

		// Assert
		Assert.assertEquals(new BigDecimal(23000), paymentValue);
	}

	@Test
	public void calculatePaymentMotorcycleBy8Hours() {

		// Arrange
		int hours = 8;

		// Act
		BigDecimal paymentValue = vehicleControlService.calculatePaymentByVehicleType(VehicleTypeEnum.MOTORCYCLE, 0,
				hours, false);

		// Assert
		Assert.assertEquals(new BigDecimal(4000), paymentValue);
	}

	@Test
	public void calculatePaymentMotorcycleBy1DayAnd3Hours() {

		// Arrange
		int hours = 3;
		int days = 1;

		// Act
		BigDecimal paymentValue = vehicleControlService.calculatePaymentByVehicleType(VehicleTypeEnum.MOTORCYCLE, days,
				hours, false);

		// Assert
		Assert.assertEquals(new BigDecimal(5500), paymentValue);
	}

	@Test
	public void calculatePaymentMotorcycle650CCBy1DayAnd3Hours() {

		// Arrange
		int hours = 3;
		int days = 1;

		// Act
		BigDecimal paymentValue = vehicleControlService.calculatePaymentByVehicleType(VehicleTypeEnum.MOTORCYCLE, days,
				hours, true);

		// Assert
		Assert.assertEquals(new BigDecimal(7500), paymentValue);
	}

	@Test
	public void calculateHoursLessThan9() throws ParseException {

		// Arrange
		Date entryDate = dateFormat.parse("2016-02-14 10:00:00");
		Date departureDate = dateFormat.parse("2016-02-14 17:59:59");

		// Act
		int totalHours = vehicleControlService.calculateTotalHours(entryDate, departureDate);

		// Assert
		Assert.assertEquals(8, totalHours);
	}

	@Test
	public void calculateHoursMoreThanOneDay() throws ParseException {

		// Arrange
		Date entryDate = dateFormat.parse("2016-02-14 10:00:00");
		Date departureDate = dateFormat.parse("2016-02-15 18:00:00");

		// Act
		int totalHours = vehicleControlService.calculateTotalHours(entryDate, departureDate);

		// Assert
		Assert.assertEquals(32, totalHours);
	}

	@Test
	public void calculatePaymentCar1Day3Hours() throws ParseException {

		// Arrange
		Date entryDate = dateFormat.parse("2016-02-14 10:00:00");
		Date departureDate = dateFormat.parse("2016-02-15 13:00:00");

		// Act
		BigDecimal paymentValue = vehicleControlService.calculatePaymentByDates(entryDate, departureDate,
				VehicleTypeEnum.CAR, false);

		// Assert
		Assert.assertEquals(new BigDecimal(11000), paymentValue);
	}

	@Test
	public void calculatePaymentMotorcycle650cc10Hours() throws ParseException {

		// Arrange
		Date entryDate = dateFormat.parse("2018-07-03 07:00:00");
		Date departureDate = dateFormat.parse("2018-07-03 16:45:35");

		// Act
		BigDecimal paymentValue = vehicleControlService.calculatePaymentByDates(entryDate, departureDate,
				VehicleTypeEnum.MOTORCYCLE, true);
		// Assert
		Assert.assertEquals(new BigDecimal(6000), paymentValue);
	}

	@Test
	public void registerVehicleExitNotFound() {

		// Arrange
		VehicleModel vehicleModel = aMotorcycle().build();
		when(vehicleControlRepository.findOneByDepartureDateIsNullAndVehiclePlate(anyString())).thenReturn(null);

		try {
			// Act
			vehicleControlService.registerVehicleExit(vehicleModel);
			fail();

		} catch (Exception e) {
			// Assert
			Assert.assertEquals("No existe registro de ingreso del vehiculo", e.getMessage());
		}

	}

	@Test
	public void registerVehicleEntryIfExists() {

		// Arrange
		VehicleModel vehicleModel = aMotorcycle().build();

		when(vehicleConverter.modelToEntity(any())).thenCallRealMethod();
		when(vehicleControlRepository.existsByDepartureDateIsNullAndVehiclePlate(vehicleModel.getPlate()))
				.thenReturn(true);

		try {
			// Act
			vehicleControlService.registerVehicleEntry(vehicleModel);
			fail();

		} catch (Exception e) {
			// Assert
			Assert.assertEquals("Ya existe registro de ingreso del vehiculo", e.getMessage());
		}

	}

}
