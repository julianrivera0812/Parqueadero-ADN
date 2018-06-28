package co.ceiba.adn.estacionamiento.model;

import java.util.Date;

public class VehicleControlModel {

	private Long id;

	private VehicleModel vehicle;

	private Date entryDate;

	private Date departureDate;

	public VehicleControlModel() {
		// JPA
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VehicleModel getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleModel vehicle) {
		this.vehicle = vehicle;
	}

	public VehicleControlModel(VehicleModel vehicle, Date entryDate) {
		super();
		this.vehicle = vehicle;
		this.entryDate = entryDate;
	}
}
