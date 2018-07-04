package co.ceiba.adn.estacionamiento.dto;

import java.util.List;

public class GetVehicleInParkingOutDTO extends ResponseDTO {

	private List<VehicleControlDTO> vehicleList;

	public List<VehicleControlDTO> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(List<VehicleControlDTO> vehicleList) {
		this.vehicleList = vehicleList;
	}

	public GetVehicleInParkingOutDTO() {

	}

	public GetVehicleInParkingOutDTO(List<VehicleControlDTO> vehicleList) {
		super();
		this.vehicleList = vehicleList;
	}

}
