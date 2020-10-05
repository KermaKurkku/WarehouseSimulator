package warehouse.simulator.model;

public interface IMotor {
	
	// the controllers uses this interface
	public void setSimulatorTime(double time);
	public void setDelay(long time);
	public long getDelay();
	
	// methods for visualizing
	public int getCollectingStationCount();
	public int[] getStationOrdersCount();
	public int[] getStationCollectorCount();
	public int getRouterOrdersCount();
	
}
