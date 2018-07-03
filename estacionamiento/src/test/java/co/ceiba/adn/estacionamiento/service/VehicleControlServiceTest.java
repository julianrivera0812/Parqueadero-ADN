package co.ceiba.adn.estacionamiento.service;

import static co.ceiba.adn.estacionamiento.builders.CarBuilder.aCar;
import static co.ceiba.adn.estacionamiento.builders.MotorcycleBuilder.aMotorcycle;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Calendar;
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
import co.ceiba.adn.estacionamiento.entity.VehicleControl;
import co.ceiba.adn.estacionamiento.model.CarModel;
import co.ceiba.adn.estacionamiento.model.MotorcycleModel;
import co.ceiba.adn.estacionamiento.repository.VehicleControlRepository;
import co.ceiba.adn.estacionamiento.repository.VehicleRepository;
import co.ceiba.adn.estacionamiento.service.VehicleControlService;
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
		Assert.assertEquals(2, response.getCode());
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
		Assert.assertEquals(2, response.getCode());
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
		Assert.assertEquals(0, response.getCode());
	}
}
