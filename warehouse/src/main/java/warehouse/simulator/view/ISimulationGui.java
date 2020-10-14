package warehouse.simulator.view;

/**
 * Interface used to define what methods should be available to
 * classes that use a GUI.
 * @author Jere Salmensaari
 *
 */
public interface ISimulationGui {
	
	// Methods for controller input
	/**
	 * Returns the simulation time set for the motor in the GUI.
	 * @return simulationTime Simulation time for the motor.
	 */
	public double getTime();
	
	/**
	 * Returns the delay set for the motor in the GUI.
	 * @return delay Delay time for the motor.
	 */
	public long getDelay();
	
	// Methods for the controller to give results from the motor
	/**
	 * Sets current time in the GUI.
	 * @param time Current time in the simulation.
	 */
	public void setSimTime(double time);
	
	/**
	 * Sets next leave time field in the GUI.
	 * @param nextTime Next leave time from the motor.
	 */
	public void setNextLeaveTime(int nextTime);
	
	/**
	 * Sets the delay text in the GUI.
	 * @param delay delay time in the motor.
	 */
	public void setDelayText(double delay);
	
	/**
	 * Gets the visualization used in the GUI controller.
	 * @return visual Visualization used in the class.
	 */
	public Visualization getVisual();
}
