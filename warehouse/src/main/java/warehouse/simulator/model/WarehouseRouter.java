package warehouse.simulator.model;

import eduni.distributions.*;
import warehouse.simulator.model.Trace.Level;

import java.util.PriorityQueue;

// TODO javadoc

public class WarehouseRouter {
    private static WarehouseRouter INSTANCE;

    private Uniform generator = new Uniform(10, 25);  // Default values
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

    public void setRouteVariance(double min, double max)
    {
        this.generator = new Uniform(min,max);
    }

    // Routes the orders by type
    public void routeOrders(CollectingStation[] stations)
    {
       
        
        if (Clock.getInstance().getTime() > (motor.getSimTime() - 1))
        {
        	for (int i = 0; i < this.orders.size(); i++)
        	{
                routeOrder(stations);
                motor.visualize();
                motor.delay();
            }
            return;
        }
        
        for (int i = 0; i < this.generator.sample(); i++)
        {
            if (isEmpty()){
                Trace.out(Level.ERR, "No orders in router!");
                break;
            } 
            routeOrder(stations); 
            motor.visualize();
            motor.delay();

            
        }
        // TODO be able to change the generation of new event
        motor.newEvent(new Event(EventType.ROUT, Clock.getInstance().getTime()+(
            generator.sample()*4/100 // Random numbers 
        )));
    }
    
    // Mostly for testing purposes
    private void routeOrder(CollectingStation[] stations)
    {
    	CollectingStation routTo = findStation(orders.peek().getStation(), stations);
        if (routTo == null)
        {
            orders.remove();
            return;
        }
        // TODO add trace for adding order - here or in collectingStation
        routTo.addOrder(orders.poll());
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
        Trace.out(Level.ERR, "No collecting station found for type: "+dest);
        return null;
    }

    public boolean isEmpty()
    {
        return this.orders.isEmpty();
    }
    
    public int getOrderCount()
    {
    	return this.orders.size();
    }

    public int getTotalOrders()
    {
        return this.totalOrders;
    }

    public void setMotor(Motor motor)
    {
        this.motor = motor;
    }
    
    public void empty()
    {
        this.orders = new PriorityQueue<Order>();
        this.totalOrders = 0;
    }

}
