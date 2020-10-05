package warehouse.simulator.model;

import eduni.distributions.ContinuousGenerator;

/**
 * This class creates orders for the systems router
 * Used to initialize the orders
 * 
 */

 // TODO rest od javadoc

 // TODO Lisää satunnaisgeneroitu lisättävien tilausten määrä
public class OrderGenerator {
    private ContinuousGenerator generator;
    private WarehouseRouter router;
    private Motor motor;
    private CollectingStation[] stations;

    /**
     * Constructor for the Order generator
     * @param generator Number generator for generating order collect times
     */
    public OrderGenerator(ContinuousGenerator generator, Motor motor)
    {
        this.router = WarehouseRouter.getInstance();
        this.motor = motor;
        this.generator = generator;
    }

    /**
     * Creates a specified amount of orders
     */
    public void createOrders(double amount)
    {
        
        for (int i = 0; i < amount; i++)
        {
            
            router.addOrder(new Order(generator.sample(), generateLeaveTime(), generateStation()));
        }
        if (!(Clock.getInstance().getTime()  > (motor.getSimTime() - 1)))
        {
            motor.newEvent(new Event(EventType.ORDR, Clock.getInstance().getTime()+Math.abs(generator.sample()*3)));
        }
        
    }

    public void initializeOrders(int amount)
    {
        for (int i = 0; i < Math.abs(amount); i++)
        {
            router.addOrder(new Order(generator.sample(), generateLeaveTime(), generateStation()));
        }
        motor.newEvent(new Event(EventType.ROUT, Clock.getInstance().getTime()+Math.abs(generator.sample())));
        // This is horrible to read, change mby?
        motor.newEvent(new Event(EventType.ORDR, Clock.getInstance().getTime()+ 
        Math.abs(generator.sample()*2)));
    }

    /**
     * Returns an EventType for the order selected randomly
     * @return EvenetType for the order
     */
    private CollectingStation generateStation()
    {
        int range = (stations.length - 1) +1;
        int rand = (int)(Math.random()* range);

        return stations[rand];
    }

    /**
     * Generates an integer to set a leaving Time for the orders
     * @return int to set a leaving Time for orders
     */
    private int generateLeaveTime()
    {
        double rand = Math.random();
        
        if (rand > 0.75 && (Clock.getInstance().getTime() < 9))
        {
            return 0;
        } else if (rand > 0.5 && (Clock.getInstance().getTime() < 13))
        {
            return 1;
        } else if (rand > 0.25 && (Clock.getInstance().getTime() < 17))
        {
            return 2;
        } else 
        {
            return 3;
        }
        
    }
    
    public void setStations(CollectingStation[] stations)
    {
    	this.stations = stations;
    }


}
