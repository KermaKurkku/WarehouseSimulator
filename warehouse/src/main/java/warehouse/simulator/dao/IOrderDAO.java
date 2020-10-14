package warehouse.simulator.dao;


import warehouse.simulator.model.OrderResults;

/**
 * Interface used to define which methods should be available
 * for classes using the inheiritng class.
 * @author Jere Salmensaari
 */
public interface IOrderDAO {
    
    /**
     * Writes the results in of the given OrderResults object
     * in a database.
     * @param results OrderResults to be written.
     * @return True or false.
     */
    public boolean writeOrderResults(OrderResults results);

    /**
     * Reads the results of a given key.
     * @param date Key of the results that are wanted.
     * @return OrderResults object containing the results.
     */
    public OrderResults readOrderResults(String date);

    /**
     * Returns all keys in the database.
     * @return Array of keys.
     */
    public String[] getKeys();
    
    
}
