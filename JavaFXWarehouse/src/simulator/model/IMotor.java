package simulator.model;

public interface IMotor {
	
	// the controllers uses this interface
	public void setSimulatorTime(double time);
	public void setDelay(long time);
	public long getDelay();
	
}
