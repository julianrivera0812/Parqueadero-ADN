package co.ceiba.adn.estacionamiento.restcontroller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import co.ceiba.adn.estacionamiento.enumeration.ResponseCodeEnum;
import co.ceiba.adn.estacionamiento.model.VehicleModel;
import co.ceiba.adn.estacionamiento.util.JsonUtil;

public abstract class BaseITRestController {

	protected ResultActions consumePostWS(VehicleModel modelTest, String wsURI, MockMvc mockMvc) throws Exception {

		return mockMvc.perform(post(wsURI).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(modelTest)));
	}

	protected void postWSBadRequestIT(VehicleModel modelTest, String wsURI, MockMvc mockMvc) throws Exception {

		// Act
		ResultActions resultActions = consumePostWS(modelTest, wsURI, mockMvc);

		// Assert
		assertBadRequest(resultActions);
	}

	protected ResultActions consumeGetWS(String wsURI, String queryParams, MockMvc mockMvc) throws Exception {

		StringBuilder wsUriAndParams = new StringBuilder(wsURI);
		wsUriAndParams.append("?");
		wsUriAndParams.append(queryParams);

		return mockMvc.perform(get(wsUriAndParams.toString()).contentType(MediaType.APPLICATION_JSON));
	}

	protected void assertOk(ResultActions resultActions) throws Exception {

		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.code", is(ResponseCodeEnum.SUCCESS.getCode())));
	}

	protected void assertBadRequest(ResultActions resultActions) throws Exception {

		resultActions.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code", is(ResponseCodeEnum.BAD_REQUEST.getCode())));
	}

}