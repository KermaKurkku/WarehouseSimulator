package simulator.model;

import java.util.PriorityQueue;

import simulator.model.Trace.Level;
import simulator.util.NumberFormatter;

// TODO javadoc


public class CollectingStation {
    private Motor motor;

    private int id;
    private static int i = 1;

    private PriorityQueue<Order> orders;
    private EventType schedulingType;

    private Collector[] collectors;

    public CollectingStation(Motor motor, EventType schedulingType, int collectors)
    {
        this.motor = motor;
        this.id = i++;
        this.orders = new PriorityQueue<Order>();
        this.schedulingType = schedulingType;
        setCollectorCount(collectors);

    }

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

    public void addOrder(Order order)
    {
        this.orders.add(order);
    }

    public int getOrderCount()
    {
        return this.orders.size();
    }
    
    public int getCollectorCount()
    {
    	return this.collectors.length;
    }

    public EventType getType()
    {
        return this.schedulingType;
    }
    
    public void setCollectorCount(int count)
    {
    	this.collectors = new Collector[count];

        for (int i = 0; i < count; i++)
        {
            this.collectors[i] = new Collector(this.schedulingType);
        }
    }

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

    public boolean hasOrders()
    {
        return this.orders.size() != 0;
    }

    public void printOrders()
    {
        for (Order order : this.orders)
        {
            System.out.println(order);
        }
    }

    @Override
    public String toString()
    {
        return "Station"+this.id;
    }

}
