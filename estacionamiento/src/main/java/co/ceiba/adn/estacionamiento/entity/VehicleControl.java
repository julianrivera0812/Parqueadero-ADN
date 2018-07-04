package co.ceiba.adn.estacionamiento.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstructorResult;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.ceiba.adn.estacionamiento.dto.VehicleControlDTO;

@Entity
@Table(name = "vehicle_control")
@NamedNativeQuery(name = "VehicleControl.findNativeByDepartureDateIsNull", resultClass = VehicleControlDTO.class, resultSetMapping = "VehicleControl.findNativeByDepartureDateIsNull", query = "SELECT v.plate, v.type, vc.entry_date FROM vehicle_control vc LEFT JOIN vehicle v ON v.plate=vc.plate WHERE vc.departure_date IS NULL")
@SqlResultSetMapping(name = "VehicleControl.findNativeByDepartureDateIsNull", classes = {
		@ConstructorResult(targetClass = VehicleControlDTO.class, columns = {
				@ColumnResult(name = "plate", type = String.class), @ColumnResult(name = "type", type = String.class),
				@ColumnResult(name = "entry_date", type = Date.class) }) })
public class VehicleControl {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "plate", nullable = false)
	private Vehicle vehicle;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entry_date", nullable = false)
	private Date entryDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "departure_date")
	private Date departureDate;

	@Column(name = "payment_value")
	private BigDecimal paymentValue;

	public VehicleControl() {
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

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public VehicleControl(Vehicle vehicle, Date entryDate) {
		super();
		this.vehicle = vehicle;
		this.entryDate = entryDate;
	}

	public BigDecimal getPaymentValue() {
		return paymentValue;
	}

	public void setPaymentValue(BigDecimal paymentValue) {
		this.paymentValue = paymentValue;
	}
}
