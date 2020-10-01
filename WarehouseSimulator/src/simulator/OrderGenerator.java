package simulator;

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
    public void createOrders(int amount)
    {
        EventType type;
        for (int i = 0; i < amount; i++)
        {
            type = generateType();
            router.addOrder(new Order(generator.sample(), generateLeaveTime(), type));
        }
        motor.newEvent(new Event(EventType.ORDR, Clock.getInstance().getTime()+generator.sample()*3));
    }

    public void initializeOrders(int amount)
    {
        EventType type;
        for (int i = 0; i < amount; i++)
        {
            type = generateType();
            router.addOrder(new Order(generator.sample(), generateLeaveTime(), type));
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
    private EventType generateType()
    {
        double rand = Math.random();

        if (rand > 0.5)
        {
            return EventType.TOC1;
        } else{
            return EventType.TOC2;
        }
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


}
