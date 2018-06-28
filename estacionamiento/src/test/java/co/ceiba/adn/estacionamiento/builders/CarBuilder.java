package co.ceiba.adn.estacionamiento.builders;

import co.ceiba.adn.estacionamiento.model.CarModel;

public class CarBuilder {

	private String plate;

	public CarBuilder() {
		this.plate = "ABC123";
	}

	public CarModel build() {
		return new CarModel(plate);
	}

	public CarBuilder withPlate(String plate) {
		this.plate = plate;
		return this;
	}

	public static CarBuilder aCar() {
		return new CarBuilder();
	}
}
