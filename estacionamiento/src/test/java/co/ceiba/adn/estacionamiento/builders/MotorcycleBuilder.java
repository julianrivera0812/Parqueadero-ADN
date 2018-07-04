package co.ceiba.adn.estacionamiento.builders;

import co.ceiba.adn.estacionamiento.model.MotorcycleModel;

public class MotorcycleBuilder {

	private String plate;

	private short cylinderCapacity;

	public MotorcycleBuilder() {
		this.plate = "ATP99D";
		this.cylinderCapacity = 500;
	}

	public MotorcycleBuilder withPlate(String plate) {
		this.plate = plate;
		return this;
	}

	public MotorcycleBuilder withCylinderCapacity(short cylinderCapacity) {
		this.cylinderCapacity = cylinderCapacity;
		return this;
	}

	public MotorcycleModel build() {
		return new MotorcycleModel(plate, cylinderCapacity);
	}

	public static MotorcycleBuilder aMotorcycle() {
		return new MotorcycleBuilder();
	}
}
