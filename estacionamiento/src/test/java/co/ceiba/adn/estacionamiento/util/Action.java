package co.ceiba.adn.estacionamiento.util;

import co.ceiba.adn.estacionamiento.exception.ApplicationException;

public interface Action {

	public void execute() throws ApplicationException;
}
