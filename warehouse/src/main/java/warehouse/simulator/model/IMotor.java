package warehouse.simulator.model;
import java.util.List;

/**
 * Interfce used to define what methods should be available
 * for classes using the inheriting class.
 * @author Jere Salmensaari
 */
public interface IMotor {
	
	// the controllers uses this interface
	/**
	 * Sets the simulator's time.
	 * @param time Time to be set.
	 */
	public void setSimulatorTime(double time);
	
	/**
	 * Stops the simulation.
	 */
	public void stopSimulation();

	/**
	 * Sets the delay of the motor.
	 * @param time Delay to be set.
	 */
	public void setDelay(long time);
	
	/**
	 * Returns the delay of the motor.
	 * @return delay Delay time of the motor.
	 */
	public long getDelay();

	/**
	 * Clears the WarehouseRouter of orders.
	 */
	public void clearRouter();
	
	/**
	 * Returns the dates of saved results.
	 * @return dateData Array of names of saved results.
	 */
	public String[] getDateData();

	/**
	 * Returns the data of a single saved result.
	 * @param date Key for the saved result.
	 * @return OrderResults Results of the key.
	 */
	public OrderResults getResults(String date);
	
	// methods for visualizing
	/**
	 * Returns the amount of ColectingStations in the motor.
	 * @return stationCount Amount of stations in the motor.
	 */
	public int getCollectingStationCount();

	/**
	 * Returns an array containing the amount of orders
	 * each station has.
	 * @return Array of amount of orders in stations.
	 */
	public int[] getStationOrdersCount();

	/**
	 * Returns an array containing the amount of collectors
	 * each station has.
	 * @return Array of amount of collectors in stations.
	 */
	public int[] getStationCollectorCount();

	/**
	 * Returns an array containing the amount of collecting
	 * collectors in each station.
	 * @return Array of collecting collectors in the stations.
	 */
	public List<int[]> getCollectingCollectors();

	/**
	 * Returns the amount of orders in the router.
	 * @return Amount of orders in router.
	 */
	public int getRouterOrdersCount();

	/**
	 * Returns the next leave time.
	 * @return Next leave time of orders.
	 */
	public int getNextLeaveTime();

	
}
