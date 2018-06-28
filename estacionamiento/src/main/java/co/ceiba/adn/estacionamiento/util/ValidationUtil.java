package co.ceiba.adn.estacionamiento.util;

import java.util.Calendar;
import java.util.Date;

public final class ValidationUtil {

	private ValidationUtil() {
	}

	public static final boolean isMondayOrSunday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
				|| calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY);
	}

}
