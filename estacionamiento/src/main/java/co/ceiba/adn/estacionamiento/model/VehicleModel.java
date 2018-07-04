package co.ceiba.adn.estacionamiento.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class VehicleModel {

	@NotBlank
	@Size(min = 6, max = 6)
	private String plate;

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	protected VehicleModel(String plate) {
		super();
		this.plate = plate;
	}

	public VehicleModel() {
	}

}
