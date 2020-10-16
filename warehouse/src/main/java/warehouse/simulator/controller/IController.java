package warehouse.simulator.controller;

import warehouse.simulator.model.OrderResults;
import warehouse.simulator.view.ISimulationGui;

/**
 * Interfce used to define which methods should be available
 * for classes using the inheriting class.
 * @author Jere Salmensaari
 */
public interface IController {
	
	// Interface for the UI
	/**
	 * Handles starting the simulation.
	 */
	public void startSimulation();
	
	/**
	 * Handles stopping the simulation.
	 */
	public void stopSimulation();

	/**
	 * Handles decrasing the delay of the motor.
	 */
	public void faster();

	/**
	 * Handles lengthening the delay of the motor.
	 */
	public void slower();

	/**
	 * Returns the amount of delay in the motor.
	 * @return delay Amount of delay in the motor.
	 */
	public long getDelay();

	/**
	 * Set the ISimulationGui of the controller.
	 * @param gui ISimulationGui to be set.
	 */
	public void setGUI(ISimulationGui gui);

	/**
	 * Gets the dates of the saved results.
	 * @return dateData Array of names of saved results.
	 */
	public String[] getDateData();

	/**
	 * Gets the data of a single saved result.
	 * @param date Key for the saved result.
	 * @return OrderResults Results of the key.
	 */
	public OrderResults getResults(String date);

	// Interface for the motor
	/**
	 * Sets the next leave time on the ISimulationGui 
	 * from the motor.
	 */
	public void setNextLeaveTime();
	
	/**
	 * Sets the time in the ISimulationGui of the controller
	 * from the motor.
	 * @param time Time to be set.
	 */
	public void showTime(double time);

	/**
	 * Draws the orders on the ISimulationgui.
	 */
	public void visualizeOrders();
}
