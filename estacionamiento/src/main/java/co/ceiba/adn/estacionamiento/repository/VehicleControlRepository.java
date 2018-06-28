package co.ceiba.adn.estacionamiento.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.ceiba.adn.estacionamiento.entity.VehicleControl;

@Repository
public interface VehicleControlRepository extends JpaRepository<VehicleControl, Long> {

	@Query("SELECT vc FROM VehicleControl vc LEFT JOIN vc.vehicle v WHERE TYPE(v) = ':type'")
	Optional<Long> countByDepartureDateIsNull(@Param("type") String type);
}
