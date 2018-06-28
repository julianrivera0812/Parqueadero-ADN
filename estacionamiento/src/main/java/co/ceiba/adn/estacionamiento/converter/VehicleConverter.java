package co.ceiba.adn.estacionamiento.converter;

import org.springframework.stereotype.Component;

import co.ceiba.adn.estacionamiento.entity.Car;
import co.ceiba.adn.estacionamiento.entity.Motorcycle;
import co.ceiba.adn.estacionamiento.entity.Vehicle;
import co.ceiba.adn.estacionamiento.model.CarModel;
import co.ceiba.adn.estacionamiento.model.MotorcycleModel;
import co.ceiba.adn.estacionamiento.model.VehicleModel;

@Component
public class VehicleConverter {

	public Vehicle modelToEntity(VehicleModel vehicleModel) {

		Vehicle vehicle = null;

		if (vehicleModel instanceof MotorcycleModel) {
			vehicle = new Motorcycle(vehicleModel.getPlate(), ((MotorcycleModel) vehicleModel).getCylinderCapacity());
		}

		if (vehicleModel instanceof CarModel) {
			vehicle = new Car(vehicleModel.getPlate());
		}

		return vehicle;
	}
}
