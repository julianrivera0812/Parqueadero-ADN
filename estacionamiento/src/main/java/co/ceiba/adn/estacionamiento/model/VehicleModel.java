package co.ceiba.adn.estacionamiento.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class VehicleModel {

	protected String plate;

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public VehicleModel(@NotNull @NotEmpty String plate) {
		super();
		this.plate = plate;
	}

	public VehicleModel() {
	}

}
