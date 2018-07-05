package co.ceiba.adn.estacionamiento.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.ceiba.adn.estacionamiento.dto.VehicleControlDTO;
import co.ceiba.adn.estacionamiento.entity.Vehicle;
import co.ceiba.adn.estacionamiento.entity.VehicleControl;

@Repository
public interface VehicleControlRepository extends JpaRepository<VehicleControl, Long> {

	@Query("SELECT COUNT(vc) FROM VehicleControl vc LEFT JOIN vc.vehicle v WHERE TYPE(v) = :type AND vc.departureDate IS NULL")
	Optional<Long> countByDepartureDateIsNull(@Param("type") Class<? extends Vehicle> type);

	List<VehicleControl> findByDepartureDateIsNullAndVehiclePlate(String plate);

	@Query("FROM VehicleControl vc LEFT JOIN vc.vehicle v WHERE vc.departureDate IS NULL AND v.plate = :plate")
	VehicleControl findOneByDepartureDateIsNullAndVehiclePlate(@Param("plate") String plate);

	@Query("SELECT NEW co.ceiba.adn.estacionamiento.dto.VehicleControlDTO(v.plate, v.type, vc.entryDate) FROM VehicleControl vc LEFT JOIN vc.vehicle v WHERE vc.departureDate IS NULL")
	Page<VehicleControlDTO> findByDepartureDateIsNull(Pageable pageable);

	boolean existsByDepartureDateIsNullAndVehiclePlate(String plate);
}
