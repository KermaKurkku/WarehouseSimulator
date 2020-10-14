package warehouse.simulator.model;

import eduni.distributions.*;
import warehouse.simulator.model.Trace.Level;

import java.util.PriorityQueue;

/**
 * Singleton used to share orders to all
 * CollectingStations in the motor.
 * @author Jere Salmensaari
 */
public class WarehouseRouter {
    private static WarehouseRouter INSTANCE;

    private Uniform generator = new Uniform(10, 25);  // Default values
    private Motor motor;
    private PriorityQueue<Order> orders;

    private int totalOrders = 0;

    /**
     * Private constructor for the WarehouseRouter.
     */
    private WarehouseRouter() 
    {
        this.orders = new PriorityQueue<Order>();
    }

    /**
     * Returns the instance of the WarehouseRouter.
     * @return INSTANCE Instance of the router.
     */
    public static WarehouseRouter getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new WarehouseRouter();
        }
        return INSTANCE;
    }

    /**
     * Sets the routing variance for the rouer.
     * @param min Minimum amount of routed orders.
     * @param max Maximum amount of routed orders.
     */
    public void setRouteVariance(double min, double max)
    {
        this.generator = new Uniform(min,max);
    }

    // Routes the orders by type
    /**
     * Routes orders to given CollectingStations depending
     * on the station in the order to be routed.
     */
    public void routeOrders()
    {
       
        CollectingStation[] stations = motor.getStations();
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
        motor.newEvent(new Event(EventType.ROUT, Clock.getInstance().getTime()+(
            generator.sample()*4/100 // Random numbers 
        )));
    }
    
    /**
     * Route a single order to the CollectingStations.
     * Mostly for testing purposes.
     * @param stations Routable CollectingStations.
     */
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

    /**
     * Adds an order to the WarehouseRouter.
     * @param order Order to be added.
     */
    public void addOrder(Order order)
    {
        this.orders.add(order);
        this.totalOrders++;
    }

    /**
     * Finds the matching CollectingStation to the station in the 
     * order to be routed.
     * @param dest CollectingStation in the order.
     * @param stations List of Stations given to the router.
     * @return station Correct station in the given list.
     */
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

    /**
     * Returns true if the router is empty.
     * @return True or false.
     */
    public boolean isEmpty()
    {
        return this.orders.isEmpty();
    }
    
    /**
     * Returns the amount of orders.
     * @return Amount of orders in the router.
     */
    public int getOrderCount()
    {
    	return this.orders.size();
    }

    /**
     * Returns the total amount of orders that have
     * gone through the WarehouseRouter.
     * @return totalOrders Total amount of orders.
     */
    public int getTotalOrders()
    {
        return this.totalOrders;
    }

    /**
     * Sets the motor reference of the WarehouseRouter.
     * @param motor Motor to be set.
     */
    public void setMotor(Motor motor)
    {
        this.motor = motor;
    }
    
    /**
     * Empties the PriorityQueue of the WarehouseRouter.
     */
    public void empty()
    {
        this.orders = new PriorityQueue<Order>();
        this.totalOrders = 0;
    }

}
