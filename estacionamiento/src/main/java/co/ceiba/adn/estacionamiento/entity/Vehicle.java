package co.ceiba.adn.estacionamiento.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {

	@Id
	@Column(name = "plate", length = 6)
	protected String plate;

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public enum VehicleTypeEnum {
		CAR, MOTORCYCLE, VEHICLE;
	}

	public abstract VehicleTypeEnum getType();
}
