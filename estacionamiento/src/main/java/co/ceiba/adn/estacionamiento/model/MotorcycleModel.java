package co.ceiba.adn.estacionamiento.model;

import javax.validation.constraints.Min;

public class MotorcycleModel extends VehicleModel {

	@Min(0)
	private short cylinderCapacity;

	public MotorcycleModel(String plate, short cylinderCapacity) {
		super(plate);
		this.cylinderCapacity = cylinderCapacity;
	}

	public MotorcycleModel() {

	}

	public short getCylinderCapacity() {
		return cylinderCapacity;
	}

	public void setCylinderCapacity(short cylinderCapacity) {
		this.cylinderCapacity = cylinderCapacity;
	}

}
