package co.ceiba.adn.estacionamiento.dto;

import java.math.BigDecimal;

public class RegisterExitOutDTO extends ResponseDTO {

	private BigDecimal paymentValue;

	public BigDecimal getPaymentValue() {
		return paymentValue;
	}

	public void setPaymentValue(BigDecimal paymentValue) {
		this.paymentValue = paymentValue;
	}

}
