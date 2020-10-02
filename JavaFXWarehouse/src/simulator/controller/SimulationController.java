package simulator.controller;

import simulator.view.IGui;
import simulator.model.IMotor;
import simulator.model.Motor;

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
		motor = new Motor(this);
		motor.setSimulatorTime(this.gui.getTime());
		motor.setDelay(this.gui.getDelay());
		gui.getVisual();
		((Thread)motor).start();
	}
	
	@Override
	public void slower()
	{
		motor.setDelay((long)(motor.getDelay()*1.10));
	}
	
	@Override
	public void faster()
	{
		motor.setDelay((long)(motor.getDelay()*0.9));
	}

	@Override
	public void showEndTime() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visualizeOrder() {
		// TODO Auto-generated method stub
		
	}
	
	
}
