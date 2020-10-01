package simulator.controller;

public interface IController {
	
	// Interface for the UI
	public void startSimulation();
	public void faster();
	public void slower();
	
	// Interface for the motor
	public void showEndTime();
	public void visualizeOrder();
}
