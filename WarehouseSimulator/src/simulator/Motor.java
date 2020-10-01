package simulator;

import eduni.distributions.*;
import simulator.Trace.Level;

//TODO javadoc

public class Motor {
    private double simTime = 0;

    private int completedOrders = 0;
    private int lateOrders = 0;
    
    private CollectingStation[] stations = new CollectingStation[2];
    private Clock clock;

    private EventList eList;
    private OrderGenerator generator;
    private WarehouseRouter router;

    private Uniform orderAmountGenerator = new Uniform(3,15); // default values

    // TODO be able to set amount of colletors from outside
    public Motor()
    {
        stations[0] = new CollectingStation(this, EventType.COLL1, 2);
        stations[1] = new CollectingStation(this, EventType.COLL2, 3);

        clock = Clock.getInstance();
        router = WarehouseRouter.getInstance();
        router.setMotor(this);
        this.generator = new OrderGenerator(new Normal(0.1, 0.15), this);
        this.eList = new EventList();
        this.generator.initializeOrders(15);
        
    }


    // TODO run() go through B and C events and do the shits

    public void setSimulatorTime(double time)
    {
        this.simTime = formatTime(time);
    }

    public void run()
    {
        while(stillSimulating())
        {
            if (currentTime() == -1) break; // If EventList is empty
            clock.setTime(currentTime());
            runBEvents();
            runCEvents();
        }
        Trace.out(Level.WAR, "Simulointi on loppunut");
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
            case COLL1: o = stations[0].removeCompleted();
                        sendCompletedOrders(o);
                        break;
            case COLL2: o = stations[1].removeCompleted();
                        sendCompletedOrders(o);
                        break;
            case ORDR:  generator.createOrders((int)this.orderAmountGenerator.sample());
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

    /**
     *  This class sets numbers to be 2 decimal paces
     *  This looks cleaner and is easier to ceep track of
     * @param num Number to be formatted
     * @return  Formatted number
     */
    public static double formatTime(double num)
    {
        
        num = Double.parseDouble(String.format("%.2f", num));
        return num;
    }

    public boolean stillSimulating()
    {
        Trace.out(Trace.Level.INFO, "Kello on: " + clock.getTime());
        return clock.getTime() < simTime;
    }

    public void printEvents()
    {
        this.eList.printEvents();
    }

    public void setOrderGeneration(int min, int max)
    {
        this.orderAmountGenerator = new Uniform(min, max);
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

    public void reportResults()
    {
        System.out.println("---");
        Trace.results("Completed orders: ",this.completedOrders);
        Trace.results("Orders left uncompleted: ", (router.getTotalOrders() - this.completedOrders));
        Trace.results("Average time of orders completed orders: ", (Order.getSumTime() / (double)this.completedOrders));
        Trace.results("Late orders", this.lateOrders);
    }

}



