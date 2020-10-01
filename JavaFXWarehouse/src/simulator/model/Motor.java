package simulator.model;

import eduni.distributions.*;
import simulator.controller.IController;
import simulator.model.Trace.Level;
import simulator.util.NumberFormatter;

//TODO javadoc

public class Motor extends Thread implements IMotor{
    private double simTime = 0;
    private long delay;
    
    private IController controller;
    
    private int amountOfStations = 5;

    private int completedOrders = 0;
    private int lateOrders = 0;
    
    private CollectingStation[] stations;
    private Clock clock;

    private EventList eList;
    private OrderGenerator generator;
    private WarehouseRouter router;

    private int minOrders = 15;
    private Normal orderVariance = new Normal(3,4, (long)(System.nanoTime()*Math.PI)); // default values

    // TODO be able to set amount of colletors from outside
    public Motor(IController controller)
    {
    	this.controller = controller;
    	
        stations = new CollectingStation[amountOfStations];
        int collectors = 2;
        for (int i = 0; i < stations.length; i++)
        {
            stations[i] = new CollectingStation(this, EventType.COLL, collectors);
            collectors++;
        }
        

        clock = Clock.getInstance();
        router = WarehouseRouter.getInstance();
        router.setMotor(this);
        this.generator = new OrderGenerator(new Normal(0.1, 0.15), this, stations);
        this.eList = new EventList();
        this.generator.initializeOrders(15);
        
    }

    @Override
    public void setSimulatorTime(double time)
    {
        this.simTime = NumberFormatter.format(time);
    }
    
    @Override
    public void setDelay(long delay)
    {
    	this.delay = delay;
    }
    
    @Override
    public long getDelay()
    {
    	return this.delay;
    }

    public void run()
    {
        while(stillSimulating())
        {
            if (currentTime() == -1) break; // If EventList is empty
        	delay();
            clock.setTime(currentTime());
            runBEvents();
            runCEvents();
        }
        Trace.out(Level.WAR, "Simulation over");
        reportResults();
    }

    public void runBEvents()
    {
        while (eList.getNextTime() == clock.getTime())
        {
            runEvent(eList.removeEvent());
        }
    }

    public void runCEvents()
    {
        for (CollectingStation s : stations)
        {
            if (s.openCollectors() && s.hasOrders())
            {
                s.collectOrder();
            }
        }
    }

    public void runEvent(Event e)
    {
        Order o;
        switch (e.getType())
        {
            case ROUT:  router.routeOrders(stations);
                        break;
            case COLL:  getCompletedOrders();
                        break;
            case ORDR:  generator.createOrders(this.minOrders+(int)this.orderVariance.sample());
        }
    }

    public void newEvent(Event e)
    {
        eList.add(e);
    }

    public double currentTime()
    {
        return eList.getNextTime();
    }

    

    public boolean stillSimulating()
    {
        Trace.out(Trace.Level.INFO, "Kello on: " + clock.getTime());
        return clock.getTime() < simTime;
    }
    
    private void delay()
    {
    	Trace.out(Level.INFO, "Delay: "+ this.delay);
    	try
    	{
    		sleep(this.delay);
    	} catch (InterruptedException e)
    	{
    		e.printStackTrace();
    	}
    }

    private void printEvents()
    {
        this.eList.printEvents();
    }

    public void setOrderGeneration(int mean, int variance)
    {
        this.orderVariance = new Normal(mean, variance);
    }

    private void getCompletedOrders()
    {
        Order o;
        for (CollectingStation s : stations)
        {
            while (s.hasCompleted())    // Remove all completed orders
            {
                o = s.removeCompleted();
                sendCompletedOrders(o);
            }
            
        }
    }

    private void sendCompletedOrders(Order ordr) // check if order is null and move it forwards
    {
        if (ordr == null)
        {
            Trace.out(Level.ERR, "Null order was submitted!");
            return;
        }
        ordr.setExitTime(clock.getTime());
        if (clock.getTime() > ordr.getTargetLeaveTime())
        {
            this.lateOrders++;
        }
        this.completedOrders++;
        //ordr.report();

    }

    public double getSimTime()
    {
        return this.simTime;
    }

    public void reportResults()
    {
        System.out.println("---");
        Trace.results("Completed orders: ",this.completedOrders);
        Trace.results("Orders left uncompleted: ", (router.getTotalOrders() - this.completedOrders));
        Trace.results("Average time of orders completed orders: ", (Order.getSumTime() / (double)this.completedOrders));
        Trace.results("Late orders", this.lateOrders);
    }

}



