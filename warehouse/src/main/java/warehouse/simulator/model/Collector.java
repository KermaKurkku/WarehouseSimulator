package warehouse.simulator.model;

import warehouse.simulator.model.Trace.Level;

// TODO javaDoc
public class Collector {
    private boolean collecting = false;
    private Order order = null;
    private String name; 
    private static int id = 1;
    private double finishTime = -1;
    private int collectedOrders = 0;
    private double activeTime = 0;


    public Collector(EventType type) 
    {
        this.name = type+ ":"+ id;
        id++;
    };

    /**
     * Makes the collector start collecting
     * @param ordr Order to be collected
     * @param finishTime Time when the collector finishes
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
     * Makes the collector stop collecting
     * @return Order that was collected
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
     * Returns if the collector is collecting
     * @return true if collecting otherwise false
     */
    public boolean collecting()
    {
        return this.collecting;
    }

    public String getName()
    {
        return this.name;
    }

    public double getFinishTime()
    {
        return this.finishTime;
    }

    public Order getOrder()
    {
        return this.order;
    }

    public int getCollectedOrders()
    {
        return this.collectedOrders;
    }

    public double getActivePercentage()
    {
        return this.activeTime/Clock.getInstance().getTime();
    }


}
