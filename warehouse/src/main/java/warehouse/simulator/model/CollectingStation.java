package warehouse.simulator.model;

import java.util.PriorityQueue;

import warehouse.simulator.model.Trace.Level;
import warehouse.simulator.util.NumberFormatter;


/**
 * Class representing an order collecting station in a warehouse.
 * @author Jere Salmensaari
 */
public class CollectingStation {
    private Motor motor;

    private int id;

    private PriorityQueue<Order> orders;
    private EventType schedulingType;

    private Collector[] collectors;

    /**
     * Constructor for the ColletingStaion.
     * @param motor Motor of the simulation.
     * @param schedulingType Event type to schedule.
     * @param collectors Amount of collectors in the station.
     * @param id id of the station.
     */
    public CollectingStation(Motor motor, EventType schedulingType, int collectors, int id)
    {
        this.motor = motor;
        this.id = id;
        this.orders = new PriorityQueue<Order>();
        this.schedulingType = schedulingType;
        setCollectorCount(collectors);

    }

    /**
     * Goes through the list of collectors and checks if they are collecting,
     * if not gives the collector the order to collect and starts collecting.
     * Creates an event for the ending of the collection.
     */
    public void collectOrder()
    {
        double collectingTime = 0;
        for (Collector c : collectors)
        {
            if (!c.collecting())
            {
                Trace.out(Trace.Level.INFO, "Order: "+this.orders.peek().getId()+" started collecting");
                collectingTime = NumberFormatter.format(Clock.getInstance().getTime()+this.orders.peek().getCollectionTime());
                c.startCollecting(this.orders.remove(), collectingTime);
                motor.newEvent(new Event(this.schedulingType, collectingTime));
                break;
            }
        }
        
    }

    /**
     * Goes through the list of collectors and checks if collector is colleting
     * and if it's finish time is the same as the current time.
     * @return collected Order.
     */
    public Order removeCompleted()
    {        
        for (Collector c : collectors)
        {
            
            if (c.getFinishTime() == Clock.getInstance().getTime() && c.collecting())
            {
                Trace.out(Level.INFO, c.getName()+ " Order: "+c.getOrder()+" finish time: "+c.getFinishTime());
                return c.stopCollecting();
            }
        }
        return null;
    }

    /**
     * Adds an order to the order list in the station.
     * @param order Order to be added.
     */
    public void addOrder(Order order)
    {
        this.orders.add(order);
    }

    /**
     * Returns the amount of orders.
     * @return Amount of orders.
     */
    public int getOrderCount()
    {
        return this.orders.size();
    }
    
    /**
     * Returns the amount of collectors in the station.
     * @return Amount of collectors in the staton.
     */
    public int getCollectorCount()
    {
    	return this.collectors.length;
    }

    /**
     * Returns the Event Type that the class schedules.
     * @return Event type to be scheduled.
     */
    public EventType getType()
    {
        return this.schedulingType;
    }
    
    /**
     * Creates the specified amount of collectors.
     * @param count Amount of collectors to be created.
     */
    public void setCollectorCount(int count)
    {
    	this.collectors = new Collector[count];

        for (int i = 0; i < count; i++)
        {
            this.collectors[i] = new Collector(this.schedulingType);
        }
    }

    /**
     * Returns if the station has collectors that are not collecting.
     * @return True if open collectors exist, otherwise false.
     */
    public boolean openCollectors()
    {
        int collecting = 0;
        for (Collector c : collectors)
        {
            if (c.collecting())
            {
                collecting++;
            }
        }
        if (collecting == this.collectors.length)
        {
            return false;
        }
        return true;
    }

    /**
     * Returns an array of collectors that are collecting. 
     * The array is as long as there are collectors and 
     * a 1 or a 0 will be placed into the array depending on
     * if the collector is collecting or not.
     * @return Arrau of collecting collectors.
     */
    public int[] getCollectingCollectors()
    {
        int[] collecting = new int[this.collectors.length];
        for (int i = 0; i < this.collectors.length; i++)
        {
            if (this.collectors[i].collecting())
            {
                collecting[i] = 1; 
            } else
            {
                collecting[i] = 0;
            }
        }
        return collecting;
    }

    /**
     * Returns true if there are collectors who's
     * complete time is the same as current time.
     * @return True or false.
     */
    public boolean hasCompleted()
    {
        for (Collector c : this.collectors)
        {
            if (c.getFinishTime() == Clock.getInstance().getTime())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if station has orders.
     * @return True or false.
     */
    public boolean hasOrders()
    {
        return this.orders.size() != 0;
    }
    
    @Override
    /**
     * toString of the class.
     */
    public String toString()
    {
        return "Station"+this.id;
    }

}
