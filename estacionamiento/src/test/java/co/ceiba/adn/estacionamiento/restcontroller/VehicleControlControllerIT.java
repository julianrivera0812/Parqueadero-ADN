package co.ceiba.adn.estacionamiento.restcontroller;

import static co.ceiba.adn.estacionamiento.builders.CarBuilder.aCar;
import static co.ceiba.adn.estacionamiento.builders.MotorcycleBuilder.aMotorcycle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import co.ceiba.adn.estacionamiento.EstacionamientoApplication;
import co.ceiba.adn.estacionamiento.entity.Car;
import co.ceiba.adn.estacionamiento.entity.Motorcycle;
import co.ceiba.adn.estacionamiento.entity.Vehicle;
import co.ceiba.adn.estacionamiento.entity.Vehicle.VehicleTypeEnum;
import co.ceiba.adn.estacionamiento.entity.VehicleControl;
import co.ceiba.adn.estacionamiento.enumeration.ResponseCodeEnum;
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
public class VehicleControlControllerIT extends BaseITRestController {

	private static final String API_VEHICLE_GET_VEHICLE_IN_PARKING_PAGINATED = "/api/vehicle/getVehicleInParking";

	private static final String API_VEHICLE_REGISTER_VEHICLE_EXIT = "/api/vehicle/registerVehicleExit";

	private static final String API_VEHICLE_REGISTER_CAR_ENTRY = "/api/vehicle/registerCarEntry";

	private static final String API_VEHICLE_REGISTER_MOTORCYCLE_ENTRY = "/api/vehicle/registerMotorcycleEntry";

	@Autowired
	private MockMvc mvc;

	@Autowired
	private VehicleControlRepository vehicleControlRepository;

	@Test
	public void registerMotorcycle_whenHasSpace_thenResponseCode0() throws Exception {

		// Arrange
		VehicleModel modelTest = aMotorcycle().withPlate("BTP12D").withCylinderCapacity((short) 200).build();

		// Act
		ResultActions resultWS = consumePostWS(modelTest, API_VEHICLE_REGISTER_MOTORCYCLE_ENTRY, mvc);

		// Assert
		assertOk(resultWS);
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
		ResultActions resultWS = consumePostWS(modelTest, API_VEHICLE_REGISTER_MOTORCYCLE_ENTRY, mvc);

		// Assert
		resultWS.andExpect(status().isOk()).andExpect(jsonPath("$.code", is(ResponseCodeEnum.WITHOUT_SPACE.getCode())));
		List<VehicleControl> found = vehicleControlRepository
				.findByDepartureDateIsNullAndVehiclePlate(modelTest.getPlate());
		assertThat(found).hasSize(0);
	}

	@Test
	public void registerCar_whenHasSpace_thenResponseCode0() throws Exception {

		// Arrange
		VehicleModel modelTest = aCar().withPlate("IUT123").build();

		// Act
		ResultActions resultWS = consumePostWS(modelTest, API_VEHICLE_REGISTER_CAR_ENTRY, mvc);

		// Assert
		assertOk(resultWS);
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
		ResultActions resultWS = consumePostWS(modelTest, API_VEHICLE_REGISTER_CAR_ENTRY, mvc);

		// Assert
		resultWS.andExpect(status().isOk()).andExpect(jsonPath("$.code", is(ResponseCodeEnum.WITHOUT_SPACE.getCode())));
		List<VehicleControl> found = vehicleControlRepository
				.findByDepartureDateIsNullAndVehiclePlate(modelTest.getPlate());
		assertThat(found).hasSize(0);
	}

	@Test
	public void registerCarEntry_whenExistsEntry_thenBadRequestResponseCode3() throws Exception {

		// Arrange
		CarModel modelTest = aCar().withPlate("CAR001").build();

		postWSBadRequestIT(modelTest, API_VEHICLE_REGISTER_CAR_ENTRY, mvc);
	}
	
	@Test
	public void registerMotorcycleEntry_whenExistsEntry_thenBadRequestResponseCode3() throws Exception {

		// Arrange
		CarModel modelTest = aCar().withPlate("MOT01D").build();

		postWSBadRequestIT(modelTest, API_VEHICLE_REGISTER_CAR_ENTRY, mvc);
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
		ResultActions resultEntry = consumePostWS(modelTest, API_VEHICLE_REGISTER_MOTORCYCLE_ENTRY, mvc);
		ResultActions resultExit = consumePostWS(modelTest, API_VEHICLE_REGISTER_VEHICLE_EXIT, mvc);

		// Assert
		assertOk(resultEntry);
		assertOk(resultExit);
		resultExit.andExpect(jsonPath("$.paymentValue", is(2500)));

		List<VehicleControl> found = vehicleControlRepository.findAll();
		assertThat(found).extracting(VehicleControl::getPaymentValue).containsOnlyElementsOf(paymentValues);
	}

	@Test
	public void registerMotorcycleExit_whenNoSendPlate_thenBadRequestResponseCode3() throws Exception {

		// Arrange
		MotorcycleModel modelTest = aMotorcycle().withPlate(null).build();

		postWSBadRequestIT(modelTest, API_VEHICLE_REGISTER_VEHICLE_EXIT, mvc);
	}

	@Test
	public void registerCarExit_whenHasEntry_thenResponseCode0AndPaymentValue() throws Exception {

		// Arrange
		VehicleModel modelTest = aCar().withPlate("IUT123").build();
		List<BigDecimal> paymentValues = new ArrayList<>();
		paymentValues.add(null);
		paymentValues.add(new BigDecimal("1000.00"));

		// Act
		ResultActions resultEntry = consumePostWS(modelTest, API_VEHICLE_REGISTER_CAR_ENTRY, mvc);
		ResultActions resultExit = consumePostWS(modelTest, API_VEHICLE_REGISTER_VEHICLE_EXIT, mvc);

		// Assert
		assertOk(resultEntry);
		assertOk(resultExit);
		resultExit.andExpect(jsonPath("$.paymentValue", is(1000)));

		List<VehicleControl> found = vehicleControlRepository.findAll();
		assertThat(found).extracting(VehicleControl::getPaymentValue).containsOnlyElementsOf(paymentValues);
	}

	@Test
	public void registerCarExit_whenNoSendPlate_thenBadRequestResponseCode3() throws Exception {

		// Arrange
		CarModel modelTest = aCar().withPlate(null).build();

		postWSBadRequestIT(modelTest, API_VEHICLE_REGISTER_VEHICLE_EXIT, mvc);
	}

	@Test
	public void registerCarExit_whenNotHaveEntry_thenBadRequestResponseCode3() throws Exception {

		// Arrange
		CarModel modelTest = aCar().withPlate("SIN123").build();

		postWSBadRequestIT(modelTest, API_VEHICLE_REGISTER_VEHICLE_EXIT, mvc);
	}

	@Test
	public void getVehicleInParking_whenNoSendPage_thenBadRequestResponseCode3() throws Exception {

		// Arrange
		String queryParams = "size=10";

		// Act
		ResultActions resultGet = consumeGetWS(API_VEHICLE_GET_VEHICLE_IN_PARKING_PAGINATED, queryParams, mvc);

		// Assert
		assertBadRequest(resultGet);
	}

	@Test
	public void getVehicleInParking_whenHasResult_thenResponseCode0() throws Exception {

		// Arrange
		String queryParams = "page=0&size=10";

		// Act
		ResultActions resultGet = consumeGetWS(API_VEHICLE_GET_VEHICLE_IN_PARKING_PAGINATED, queryParams, mvc);

		// Assert
		assertOk(resultGet);
		resultGet.andExpect(jsonPath("$.page.totalElements", is(28)))
				.andExpect(jsonPath("$.page.numberOfElements", is(10))).andExpect(jsonPath("$.page.totalPages", is(3)));
	}

}
