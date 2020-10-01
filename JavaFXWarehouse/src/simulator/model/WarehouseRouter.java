package simulator.model;

import eduni.distributions.*;
import simulator.model.Trace.Level;

import java.util.PriorityQueue;

// TODO javadoc

public class WarehouseRouter {
    private static WarehouseRouter INSTANCE;

    private Uniform generator = new Uniform(1, 6);  // Default values
    private Motor motor;
    private PriorityQueue<Order> orders;

    private int totalOrders = 0;

    private WarehouseRouter() 
    {
        this.orders = new PriorityQueue<Order>();
    }

    public static WarehouseRouter getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new WarehouseRouter();
        }
        return INSTANCE;
    }

    // Routes the orrders by type
    public void routeOrders(CollectingStation[] stations)
    {
        CollectingStation routTo;
        for (int i = 0; i < this.generator.sample(); i++)
        {
            if (isEmpty()){
                Trace.out(Level.ERR, "No orders in router!");
                break;
            } 
            
            
            routTo = findStation(orders.peek().getStation(), stations);
            if (routTo == null)
            {
                Trace.out(Level.ERR, "No collecting station found for type");
                orders.remove();
                continue;
            }
            // TODO add trace for adding order - here or in collectingStation
            routTo.addOrder(orders.poll());
        }
        motor.newEvent(new Event(EventType.ROUT, Clock.getInstance().getTime()+(
            generator.sample()*6/100 // Random numbers 
        )));
    }

    public void addOrder(Order order)
    {
        this.orders.add(order);
        this.totalOrders++;
    }

    public void setDistribution(int min, int max)
    {
        generator = new Uniform(min, max);
    }

    private CollectingStation findStation(CollectingStation dest, CollectingStation[] stations)
    {

        for (CollectingStation station : stations)
        {
            if (station == dest)
            {
                return station;
            }
        }
        return null;
    }

    public boolean isEmpty()
    {
        return this.orders.isEmpty();
    }

    public int getTotalOrders()
    {
        return this.totalOrders;
    }

    public void setMotor(Motor motor)
    {
        this.motor = motor;
    }

}
