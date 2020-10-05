package warehouse.simulator.view;

public interface IGui {
	
	// Methods for controller input
	public double getTime();
	public long getDelay();
	public void setDelayText(double speed);
	
	// Methods for the controller to give results from the motor
	public void setSimTime(double time);
	public Visualization getVisual();
}
