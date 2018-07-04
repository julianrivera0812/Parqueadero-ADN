package co.ceiba.adn.estacionamiento.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CAR")
public class Car extends Vehicle {

	public Car() {
		// JPA
	}

	public Car(String plate) {
		super.plate = plate;
	}

	@Override
	public VehicleTypeEnum getType() {
		return VehicleTypeEnum.CAR;
	}

}
