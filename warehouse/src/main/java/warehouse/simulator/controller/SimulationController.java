package warehouse.simulator.controller;

import warehouse.simulator.view.IGui;
import warehouse.simulator.model.Clock;
import warehouse.simulator.model.IMotor;
import warehouse.simulator.model.Motor;

import javafx.application.Platform;
import javafx.scene.control.Alert;


public class SimulationController implements IController{
	private IMotor motor;
	private IGui gui;
	
	public SimulationController()
	{

	}
	
	@Override
	public void setGUI(IGui gui)
	{
		this.gui = gui;
	}
	
	@Override
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
	public void stopSimulation()
	{
		motor.stopSimulation();
		
	}
	
	@Override
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
	public void visualizeOrders() {
		Platform.runLater(new Runnable()
		{
			public void run()
			{
				gui.getVisual().drawOrders(motor.getStationOrdersCount());
				gui.getVisual().drawRouterOrders(motor.getRouterOrdersCount());
				gui.getVisual().collectingCollectors(motor.getCollectingCollectors());
			}
		});
	}
	
	
}
