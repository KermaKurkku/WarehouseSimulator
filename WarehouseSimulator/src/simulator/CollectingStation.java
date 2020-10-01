package simulator;

import java.util.PriorityQueue;

import simulator.Trace.Level;

// TODO javadoc


public class CollectingStation {
    private Motor motor;

    private PriorityQueue<Order> orders;
    private EventType schedulingType;

    private Collector[] collectors;

    public CollectingStation(Motor motor, EventType schedulingType, int collectors)
    {
        this.motor = motor;
        this.orders = new PriorityQueue<Order>();
        this.schedulingType = schedulingType;
        this.collectors = new Collector[collectors];

        for (int i = 0; i < collectors; i++)
        {
            this.collectors[i] = new Collector(this.schedulingType);
        }

    }

    public void collectOrder()
    {
        double collectingTime = 0;
        for (Collector c : collectors)
        {
            if (!c.collecting())
            {
                Trace.out(Trace.Level.INFO, "Tilaus: "+this.orders.peek().getId()+" otetaan käsittelyyn");
                collectingTime = Motor.formatTime(Clock.getInstance().getTime()+this.orders.peek().getCollectionTime());
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
            Trace.out(Level.INFO, c.getName()+ " Order: "+c.getOrder()+" finish time: "+c.getFinishTime());
            if (c.getFinishTime() == Clock.getInstance().getTime() && c.collecting())
            {
                return c.stopCollecting();
            }
        }
        return null;
    }

    public void addOrder(Order order)
    {
        this.orders.add(order);
    }

    public int getOrderAmount()
    {
        return this.orders.size();
    }

    public EventType getType()
    {
        return this.schedulingType;
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

    

}
