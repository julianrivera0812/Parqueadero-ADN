package co.ceiba.adn.estacionamiento.servicio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import co.ceiba.adn.estacionamiento.EstacionamientoApplication;
import co.ceiba.adn.estacionamiento.JsonUtil;
import co.ceiba.adn.estacionamiento.entity.Vehicle;
import co.ceiba.adn.estacionamiento.model.MotorcycleModel;
import co.ceiba.adn.estacionamiento.model.VehicleModel;
import co.ceiba.adn.estacionamiento.repository.VehicleControlRepository;
import co.ceiba.adn.estacionamiento.repository.VehicleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstacionamientoApplication.class, webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.yml")
public class VehicleControlServiceIT {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private VehicleControlRepository vehicleControlRepository;

	private VehicleModel modelTest;

	@Test
	public void registerMotorcycle_whenHasSpace_thenResponseCode0() throws Exception {
		// Arrange
		modelTest = new MotorcycleModel("BTP12D", (short) 200);

		// Act
		mvc.perform(post("/api/vehicle/registerMotorcycleEntry").contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(modelTest))).andDo(print()).andExpect(jsonPath("$.code", is(0)));

		// Assert
		List<Vehicle> found = vehicleRepository.findAll();
		assertThat(found).extracting(Vehicle::getPlate).contains("BTP12D");
	}

	@After
	public void resetDb() {
		vehicleControlRepository.deleteAll();
		vehicleRepository.deleteAll();
	}
}
