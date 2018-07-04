package co.ceiba.adn.estacionamiento.dto;

import java.util.Date;

public class VehicleControlDTO {

	private String plate;

	private String vehicleType;

	private Date entryDate;

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public VehicleControlDTO(String plate, String vehicleType, Date entryDate) {
		super();
		this.plate = plate;
		this.vehicleType = vehicleType;
		this.entryDate = entryDate;
	}

}
