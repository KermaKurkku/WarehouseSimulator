package warehouse.simulator.model;

import warehouse.simulator.model.Trace.Level;

/**
 * Class to represent a collector in the warehouse.
 * @author Jere Salmensaari
 */
public class Collector {
    private boolean collecting = false;
    private Order order = null;
    private String name; 
    private static int id = 1;
    private double finishTime = -1;
    private int collectedOrders = 0;
    private double activeTime = 0;

    /**
     * Constructor for the class.
     * @param type Event type the class schedules.
     */
    public Collector(EventType type) 
    {
        this.name = type+ ":"+ id;
        id++;
    };

    /**
     * Makes the collector start collecting.
     * @param ordr Order to be collected.
     * @param finishTime Time when the collector finishes.
     */
    public void startCollecting(Order ordr, double finishTime)
    {
        this.collecting = true;
        this.order = ordr;

        if (this.order == null)
        {
            Trace.out(Level.ERR, "Null order on collector.");
        }

        this.finishTime = finishTime;
        Trace.out(Level.INFO, "Collector "+ this.name+" started collecting.");
    }

    /**
     * Makes the collector stop collecting.
     * @return returnOrder Order that was collected.
     */
    public Order stopCollecting()
    {
        if (this.order == null) // Checks if no order is found
        {
            Trace.out(Level.ERR, "No order on collector: "+ this.name);
            return null;
        }
        this.collecting = false;
        Order returnOrder = this.order;
        this.activeTime += this.order.getCollectionTime();
        this.order = null;
        this.finishTime = -1;
        Trace.out(Level.INFO, "Collector "+this.name+" has finished collecting.");
        this.collectedOrders++;
        return returnOrder;
    }

    /**
     * Returns true if the collector is collecting.
     * @return true or false.
     */
    public boolean collecting()
    {
        return this.collecting;
    }

    /**
     * Returns the name of the collector.
     * @return name Name of the collector.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns the finishing time of the current order.
     * @return finishTime Finishing tie of the order
     * or -1 if no order is present.
     */
    public double getFinishTime()
    {
        return this.finishTime;
    }

    /**
     * Returns the current order on the collector.
     * @return order Current order on the collector 
     * or null.
     */
    public Order getOrder()
    {
        return this.order;
    }

    /**
     * Returns the amount of orders this collector
     * has collected.
     * @return collectedOrders Amount of orders collected.
     */
    public int getCollectedOrders()
    {
        return this.collectedOrders;
    }

    /**
     * Returns the percentage of time this collector 
     * has been active.
     * @return Active percentage.
     */
    public double getActivePercentage()
    {
        return this.activeTime/Clock.getInstance().getTime();
    }


}
