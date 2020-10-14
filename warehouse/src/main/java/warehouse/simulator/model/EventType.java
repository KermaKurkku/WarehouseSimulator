package warehouse.simulator.model;

/**
 * Different event types that can happen in the simulation.
 * @author Jere Salmensaari
 */
public enum EventType {
	/**
	 * Event for the ending of a collection.
	 */
    COLL,
    /**
     * Event for routing more orders.
     */
    ROUT,
    /**
     * Evemt for generating new orders.
     */
    ORDR
}
