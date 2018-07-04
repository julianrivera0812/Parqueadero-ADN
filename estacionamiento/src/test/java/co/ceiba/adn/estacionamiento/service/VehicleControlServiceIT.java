package co.ceiba.adn.estacionamiento.service;

import static co.ceiba.adn.estacionamiento.builders.CarBuilder.aCar;
import static co.ceiba.adn.estacionamiento.builders.MotorcycleBuilder.aMotorcycle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import co.ceiba.adn.estacionamiento.EstacionamientoApplication;
import co.ceiba.adn.estacionamiento.JsonUtil;
import co.ceiba.adn.estacionamiento.entity.Car;
import co.ceiba.adn.estacionamiento.entity.Motorcycle;
import co.ceiba.adn.estacionamiento.entity.Vehicle;
import co.ceiba.adn.estacionamiento.entity.Vehicle.VehicleTypeEnum;
import co.ceiba.adn.estacionamiento.entity.VehicleControl;
import co.ceiba.adn.estacionamiento.model.VehicleModel;
import co.ceiba.adn.estacionamiento.repository.VehicleControlRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstacionamientoApplication.class, webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SqlGroup({ @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
		@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql") })
public class VehicleControlServiceIT {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private VehicleControlRepository vehicleControlRepository;

	@Test
	public void registerMotorcycle_whenHasSpace_thenResponseCode0() throws Exception {
		// Arrange
		VehicleModel modelTest = aMotorcycle().withPlate("BTP12D").withCylinderCapacity((short) 200).build();

		// Act
		ResultActions resultWS = mvc.perform(post("/api/vehicle/registerMotorcycleEntry")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(modelTest)));

		// Assert
		resultWS.andExpect(status().isOk()).andExpect(jsonPath("$.code", is(0)));
		List<VehicleControl> found = vehicleControlRepository
				.findByDepartureDateIsNullAndVehiclePlate(modelTest.getPlate());
		assertThat(found).hasSize(1);
	}

	@Test
	public void registerMotorcycle_whenNotSpace_thenResponseCode2() throws Exception {

		// Arrange
		VehicleModel modelTest = aMotorcycle().withPlate("BTP12D").withCylinderCapacity((short) 200).build();
		fillParkingSpace(VehicleTypeEnum.MOTORCYCLE);

		// Act
		ResultActions resultWS = mvc.perform(post("/api/vehicle/registerMotorcycleEntry")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(modelTest)));

		// Assert
		resultWS.andExpect(status().isOk()).andExpect(jsonPath("$.code", is(2)));
		List<VehicleControl> found = vehicleControlRepository
				.findByDepartureDateIsNullAndVehiclePlate(modelTest.getPlate());
		assertThat(found).hasSize(0);
	}

	@Test
	public void registerCar_whenHasSpace_thenResponseCode0() throws Exception {

		// Arrange
		VehicleModel modelTest = aCar().withPlate("IUT123").build();

		// Act
		ResultActions resultWS = mvc.perform(post("/api/vehicle/registerCarEntry")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(modelTest)));

		// Assert
		resultWS.andExpect(status().isOk()).andExpect(jsonPath("$.code", is(0)));
		List<VehicleControl> found = vehicleControlRepository
				.findByDepartureDateIsNullAndVehiclePlate(modelTest.getPlate());
		assertThat(found).hasSize(1);
	}

	@Test
	public void registerCar_whenNotSpace_thenResponseCode2() throws Exception {

		// Arrange
		VehicleModel modelTest = aCar().withPlate("IUT123").build();

		fillParkingSpace(VehicleTypeEnum.CAR);

		// Act
		ResultActions resultWS = mvc.perform(post("/api/vehicle/registerCarEntry")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(modelTest)));

		// Assert
		resultWS.andExpect(status().isOk()).andExpect(jsonPath("$.code", is(2)));
		List<VehicleControl> found = vehicleControlRepository
				.findByDepartureDateIsNullAndVehiclePlate(modelTest.getPlate());
		assertThat(found).hasSize(0);
	}

	private void fillParkingSpace(VehicleTypeEnum vehicleType) {

		Vehicle vehicle = null;

		switch (vehicleType) {
		case CAR:
			vehicle = new Car("CAR010");
			break;
		case MOTORCYCLE:
			vehicle = new Motorcycle("MOT10D", (short) 1000);
			break;
		default:
			throw new IllegalArgumentException("Tipo de vehiculo no valido");
		}

		vehicleControlRepository.save(new VehicleControl(vehicle, new Date()));
	}

	@Test
	public void registerMotorcycleExit_whenHasEntry_thenResponseCode0AndPaymentValue() throws Exception {

		// Arrange
		VehicleModel modelTest = aMotorcycle().withPlate("BTP12D").withCylinderCapacity((short) 650).build();
		List<BigDecimal> paymentValues = new ArrayList<>();
		paymentValues.add(null);
		paymentValues.add(new BigDecimal("2500.00"));

		// Act
		ResultActions resultEntry = mvc.perform(post("/api/vehicle/registerMotorcycleEntry")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(modelTest)));

		ResultActions resultExit = mvc.perform(post("/api/vehicle/registerVehicleExit")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(modelTest)));

		// Assert
		resultEntry.andExpect(status().isOk()).andExpect(jsonPath("$.code", is(0)));
		resultExit.andExpect(status().isOk()).andExpect(jsonPath("$.code", is(0)))
				.andExpect(jsonPath("$.paymentValue", is(2500)));

		List<VehicleControl> found = vehicleControlRepository.findAll();
		assertThat(found).extracting(VehicleControl::getPaymentValue).containsOnlyElementsOf(paymentValues);
	}

	@Test
	public void registerMotorcycleExit_whenNoSendPlate_thenBadRequestResponseCode3() throws Exception {

		// Arrange
		VehicleModel modelTest = aMotorcycle().withPlate(null).build();

		// Act
		ResultActions resultExit = mvc.perform(post("/api/vehicle/registerVehicleExit")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(modelTest)));

		// Assert
		resultExit.andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.code", is(3)));
	}

	@Test
	public void registerCarExit_whenHasEntry_thenResponseCode0AndPaymentValue() throws Exception {

		// Arrange
		VehicleModel modelTest = aCar().withPlate("IUT123").build();
		List<BigDecimal> paymentValues = new ArrayList<>();
		paymentValues.add(null);
		paymentValues.add(new BigDecimal("1000.00"));

		// Act
		ResultActions resultEntry = mvc.perform(post("/api/vehicle/registerCarEntry")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(modelTest)));

		ResultActions resultExit = mvc.perform(post("/api/vehicle/registerVehicleExit")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(modelTest)));

		// Assert
		resultEntry.andExpect(status().isOk()).andExpect(jsonPath("$.code", is(0)));
		resultExit.andExpect(status().isOk()).andExpect(jsonPath("$.code", is(0)))
				.andExpect(jsonPath("$.paymentValue", is(1000)));

		List<VehicleControl> found = vehicleControlRepository.findAll();
		assertThat(found).extracting(VehicleControl::getPaymentValue).containsOnlyElementsOf(paymentValues);
	}

	@Test
	public void registerCarExit_whenNoSendPlate_thenBadRequestResponseCode3() throws Exception {

		// Arrange
		VehicleModel modelTest = aCar().withPlate(null).build();

		// Act
		ResultActions resultExit = mvc.perform(post("/api/vehicle/registerVehicleExit")
				.contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(modelTest)));

		// Assert
		resultExit.andDo(print()).andExpect(status().isBadRequest()).andExpect(jsonPath("$.code", is(3)));
	}
}
