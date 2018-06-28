package co.ceiba.adn.estacionamiento.model;

public class MotorcycleModel extends VehicleModel {

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
