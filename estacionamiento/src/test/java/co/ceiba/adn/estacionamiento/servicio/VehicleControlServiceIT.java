package co.ceiba.adn.estacionamiento.servicio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import co.ceiba.adn.estacionamiento.EstacionamientoApplication;
import co.ceiba.adn.estacionamiento.JsonUtil;
import co.ceiba.adn.estacionamiento.entity.Car;
import co.ceiba.adn.estacionamiento.entity.Motorcycle;
import co.ceiba.adn.estacionamiento.entity.Vehicle;
import co.ceiba.adn.estacionamiento.entity.Vehicle.VehicleTypeEnum;
import co.ceiba.adn.estacionamiento.entity.VehicleControl;
import co.ceiba.adn.estacionamiento.model.CarModel;
import co.ceiba.adn.estacionamiento.model.MotorcycleModel;
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
		VehicleModel modelTest = new MotorcycleModel("BTP12D", (short) 200);

		// Act
		mvc.perform(post("/api/vehicle/registerMotorcycleEntry").contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(modelTest))).andDo(print()).andExpect(jsonPath("$.code", is(0)));

		// Assert
		List<VehicleControl> found = vehicleControlRepository.findByDepartureDateIsNullAndVehiclePlate("BTP12D");
		assertThat(found).hasSize(1);
	}

	@Test
	public void registerMotorcycle_whenNotSpace_thenResponseCode2() throws Exception {
		// Arrange
		VehicleModel modelTest = new MotorcycleModel("BTP12D", (short) 200);
		fillParkingSpace(VehicleTypeEnum.MOTORCYCLE);

		// Act
		mvc.perform(post("/api/vehicle/registerMotorcycleEntry").contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(modelTest))).andExpect(status().isOk()).andExpect(jsonPath("$.code", is(2)));

		// Assert
		List<VehicleControl> found = vehicleControlRepository.findByDepartureDateIsNullAndVehiclePlate("BTP12D");
		assertThat(found).hasSize(0);
	}

	@Test
	public void registerCar_whenHasSpace_thenResponseCode0() throws Exception {
		// Arrange
		VehicleModel modelTest = new CarModel("IUT123");

		// Act
		mvc.perform(post("/api/vehicle/registerCarEntry").contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(modelTest))).andDo(print()).andExpect(jsonPath("$.code", is(0)));

		// Assert
		List<VehicleControl> found = vehicleControlRepository.findByDepartureDateIsNullAndVehiclePlate("IUT123");
		assertThat(found).hasSize(1);
	}

	@Test
	public void registerCar_whenNotSpace_thenResponseCode2() throws Exception {
		// Arrange
		VehicleModel modelTest = new CarModel("IUT123");

		fillParkingSpace(VehicleTypeEnum.CAR);

		// Act
		mvc.perform(post("/api/vehicle/registerCarEntry").contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(modelTest))).andExpect(status().isOk()).andExpect(jsonPath("$.code", is(2)));

		// Assert
		List<VehicleControl> found = vehicleControlRepository.findByDepartureDateIsNullAndVehiclePlate("IUT123");
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
}
