package co.ceiba.adn.estacionamiento.constant;

import java.math.BigDecimal;

public final class RateConstants {

	public static final BigDecimal INCREMENT_MOTORCYCLE_PAYMENT = new BigDecimal(2000);

	public static final BigDecimal MOTORCYCLE_DAY_VALUE = new BigDecimal(4000);

	public static final BigDecimal MOTORCYCLE_HOUR_VALUE = new BigDecimal(500);

	public static final BigDecimal CAR_DAY_VALUE = new BigDecimal(8000);

	public static final BigDecimal CAR_HOUR_VALUE = new BigDecimal(1000);

	private RateConstants() {
		// se deja vacio a proposito
	}
}
