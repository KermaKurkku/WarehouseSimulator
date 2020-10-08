package warehouse.simulator.controller;

import warehouse.simulator.view.IGui;

public interface IController {
	
	// Interface for the UI
	public void startSimulation();
	public void stopSimulation();
	public void faster();
	public void slower();
	public void setGUI(IGui gui);
	
	// Interface for the motor
	public void showTime(double time);
	public void visualizeOrders();
}
