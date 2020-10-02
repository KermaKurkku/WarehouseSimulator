package simulator.controller;

import simulator.view.IGui;

public interface IController {
	
	// Interface for the UI
	public void startSimulation();
	public void faster();
	public void slower();
	public void setGUI(IGui gui);
	
	// Interface for the motor
	public void showEndTime();
	public void visualizeOrder();
}
