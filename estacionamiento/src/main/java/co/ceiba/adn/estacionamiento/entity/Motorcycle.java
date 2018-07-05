package co.ceiba.adn.estacionamiento.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MOTORCYCLE")
public class Motorcycle extends Vehicle {

	@Column(name = "cylinder_capacity")
	private short cylinderCapacity;

	public Motorcycle() {
		// JPA
	}

	public Motorcycle(String plate, short cylinderCapacity) {
		super.plate = plate;
		this.cylinderCapacity = cylinderCapacity;
	}

	public short getCylinderCapacity() {
		return cylinderCapacity;
	}

	public void setCylinderCapacity(short cylinderCapacity) {
		this.cylinderCapacity = cylinderCapacity;
	}

	@Override
	public VehicleTypeEnum getTypeEnum() {
		return VehicleTypeEnum.MOTORCYCLE;
	}

}
