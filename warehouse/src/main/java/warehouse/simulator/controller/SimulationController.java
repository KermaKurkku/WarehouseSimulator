package warehouse.simulator.controller;

import warehouse.simulator.view.ISimulationGui;
import warehouse.simulator.model.Clock;
import warehouse.simulator.model.IMotor;
import warehouse.simulator.model.Motor;
import warehouse.simulator.model.OrderResults;
import javafx.application.Platform;

/**
 * Controller for the MainApp, used to talk to the motor
 * and controls all the other controllers.
 * @author Jere Salmensaari
 */
public class SimulationController implements IController{
	private IMotor motor;
	private ISimulationGui gui;
	
	/**
	 * Constructor for the SimulationController.
	 * Creates a new IMotor.
	 */
	public SimulationController()
	{
		motor = new Motor(this);
	}
	
	@Override
	/**
	 * Sets the ISimulationGui of the controller.
	 */
	public void setGUI(ISimulationGui gui)
	{
		this.gui = gui;
	}
	
	@Override
	/**
	 * Handles starting the simulation.
	 */
	public void startSimulation()
	{
		Clock.getInstance().setTime(0);
		gui.getVisual().clear();
		motor = new Motor(this);
		motor.clearRouter();
		motor.setSimulatorTime(this.gui.getTime());
		motor.setDelay(this.gui.getDelay());
		gui.getVisual().drawRouter();
		gui.getVisual().drawCollectingStations(motor.getCollectingStationCount(), motor.getStationCollectorCount());
		((Thread)motor).start();
		
	}

	@Override
	/**
	 * Handles stopping the simulation
	 */
	public void stopSimulation()
	{
		motor.stopSimulation();
		
	}
	
	@Override
	/**
	 * Handles decreasing the delay of the motor.
	 */
	public void slower()
	{
		Platform.runLater(new Runnable() 
		{
			public void run()
			{
				motor.setDelay((long)(motor.getDelay()*1.10));
			}
		});

	}
	
	@Override
	/**
	 * Handles lengtening of the delay on the motor.
	 */
	public void faster()
	{
		Platform.runLater(new Runnable() 
		{
			public void run()
			{
				motor.setDelay((long)(motor.getDelay()*0.9));
			}
		});
	}

	@Override
	/**
	 * Sets the time in the ISimulationGui of the controller
	 * from the motor.
	 */
	public synchronized void showTime(double time) {
		Platform.runLater(new Runnable()
		{
			public void run()
			{
				gui.setSimTime(time);
			}
		});
		
		
	}

	@Override
	/**
	 * Returns the delay from the motor.
	 * @return delay Delay from the motor.
	 */
	public long getDelay()
	{
		return motor.getDelay();
	}

	@Override
	/**
	 * Draws orders from the motor on the ISimulationGui.
	 */
	public void visualizeOrders() {
		Platform.runLater(new Runnable()
		{
			public void run()
			{
				gui.getVisual().drawRouterOrders(motor.getRouterOrdersCount());
				gui.getVisual().drawOrders(motor.getStationOrdersCount());
				gui.getVisual().collectingCollectors(motor.getCollectingCollectors());
			}
		});
	}

	@Override
	/**
	 * Returns the dates of saved results.
	 * @return dateData Array of names of saved results.
	 */
	public String[] getDateData() {
		return motor.getDateData();
	}

	@Override
	/**
	 * Returns the data of a single saved result.
	 * @param date Key for the saved result.
	 * @return OrderResults Results of the key.
	 */
	public OrderResults getResults(String date) {
		return this.motor.getResults(date);
	}

	@Override
	/**
	 * Sets the next leave time on the ISimulatoGui
	 * from the motor.
	 */
	public void setNextLeaveTime() {
		this.gui.setNextLeaveTime(motor.getNextLeaveTime());
	}
	
	
}
