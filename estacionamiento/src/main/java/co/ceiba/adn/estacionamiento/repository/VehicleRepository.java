package co.ceiba.adn.estacionamiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ceiba.adn.estacionamiento.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

}
