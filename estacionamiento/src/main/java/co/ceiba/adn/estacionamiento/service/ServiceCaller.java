package co.ceiba.adn.estacionamiento.service;

import co.ceiba.adn.estacionamiento.dto.ResponseDTO;
import co.ceiba.adn.estacionamiento.exception.ApplicationException;

public interface ServiceCaller {

	public ResponseDTO callService() throws ApplicationException;

}
