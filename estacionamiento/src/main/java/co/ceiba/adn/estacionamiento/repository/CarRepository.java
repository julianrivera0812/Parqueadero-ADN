package co.ceiba.adn.estacionamiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.ceiba.adn.estacionamiento.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {

	
}
