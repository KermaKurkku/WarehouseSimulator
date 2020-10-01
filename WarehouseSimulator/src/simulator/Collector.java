package simulator;

import simulator.Trace.Level;

// TODO javaaDoc
public class Collector {
    private boolean collecting = false;
    private Order order = null;
    private String name;
    private static int id = 0;
    private double finishTime = -1;
    private int collectedOrders = 0;


    public Collector(EventType type) 
    {
        this.name = type+ ":"+ id;
        id++;
    };

    public void startCollecting(Order ordr, double finishTime)
    {
        this.collecting = true;
        this.order = ordr;

        if (this.order == null) // TODO remove
        {
            Trace.out(Level.ERR, "Null order on collector.");
        }

        this.finishTime = finishTime;
        Trace.out(Level.INFO, "Collector "+ this.name+" started collecting.");
    }

    public Order stopCollecting()
    {
        if (this.order == null) // Checks if no order is found
        {
            Trace.out(Level.ERR, "No order on collector: "+ this.name);
            return null;
        }
        this.collecting = false;
        Order returnOrder = this.order;
        this.order = null;
        this.finishTime = -1;
        Trace.out(Level.INFO, "Collector "+this.name+" has finished collecting.");
        this.collectedOrders++;
        return returnOrder;
    }

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


}
