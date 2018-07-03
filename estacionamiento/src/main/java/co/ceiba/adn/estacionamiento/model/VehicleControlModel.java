package co.ceiba.adn.estacionamiento.model;

import java.util.Date;

public class VehicleControlModel {

	private Long id;

	private VehicleModel vehicleModel;

	private Date entryDate;

	private Date departureDate;

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VehicleModel getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(VehicleModel vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public VehicleControlModel(VehicleModel vehicleModel, Date entryDate) {
		super();
		this.vehicleModel = vehicleModel;
		this.entryDate = entryDate;
	}
}
