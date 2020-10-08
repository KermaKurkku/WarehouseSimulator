package warehouse.simulator.model;
import java.util.List;

public interface IMotor {
	
	// the controllers uses this interface
	public void setSimulatorTime(double time);
	public void stopSimulation();
	public void setDelay(long time);
	public long getDelay();
	public void clearRouter();
	
	// methods for visualizing
	public int getCollectingStationCount();
	public int[] getStationOrdersCount();
	public int[] getStationCollectorCount();
	public List<int[]> getCollectingCollectors();
	public int getRouterOrdersCount();

	
}
