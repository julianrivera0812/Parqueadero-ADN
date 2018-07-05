package co.ceiba.adn.estacionamiento.dto;

import org.springframework.data.domain.Page;

public class GetVehicleInParkingPaginatedDTO extends ResponseDTO {

	private Page<VehicleControlDTO> page;

	public Page<VehicleControlDTO> getPage() {
		return page;
	}

	public void setPage(Page<VehicleControlDTO> page) {
		this.page = page;
	}

}
