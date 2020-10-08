package warehouse.simulator.model;

import eduni.distributions.*;
import warehouse.simulator.controller.IController;
import warehouse.simulator.model.Trace.Level;
import warehouse.simulator.util.NumberFormatter;
import warehouse.simulator.model.Order.SortType;

import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;

//TODO javadoc

public class Motor extends Thread implements IMotor{
    private double simTime = 0;
    private boolean simulating = true;
    private long delay = 0; // Default values
    
    private IController controller;
    

    private int completedOrders = 0;
    private int lateOrders = 0;
    
    private CollectingStation[] stations;
    private Clock clock;

    private EventList eList;
    private OrderGenerator generator;
    private WarehouseRouter router;

    // Default values
    private int stationCount = 3;
    private int[] collectorCount;
    private int minOrders = 15;	
    private double minRouteVariance = 10;
    private double maxRouteVariance = 25;
    private double medianOrderCollectVariance = 0.1;
    private double varianceOrderCollectVariance = 0.15;
    private double medianOrderVariance = 3;
    private double varianceOrderVariance = 4;
    private Normal orderVariance;


    // TODO be able to set amount of colletors from outside
    public Motor()
    {
        this.completedOrders = 0;
        this.lateOrders = 0;
        File f = new File(System.getProperty("user.dir")+"/src/main/resources/options/settings.json");
        if (f.exists())
        {
            loadSettings(f);
        } else
        {
            loadSettings(new File(System.getProperty("user.dir")+"/src/main/resources/options/default.json"));
        }
    	this.clock = Clock.getInstance();
        this.router = WarehouseRouter.getInstance();
        this.router.setMotor(this);
        this.generator = new OrderGenerator(this);
        this.generator.setGenerator(new Normal(this.medianOrderCollectVariance, this.varianceOrderCollectVariance));
        this.eList = new EventList();
    }
    
    public Motor(IController controller)
    {
        this.completedOrders = 0;
        this.lateOrders = 0;

    	this.controller = controller;
        File f = new File(System.getProperty("user.dir")+"/src/main/resources/options/settings.json");
        if (f.exists())
        {
            loadSettings(f);
        } else 
        {
            loadSettings(new File(System.getProperty("user.dir")+"/src/main/resources/options/default.json"));
        }
        this.clock = Clock.getInstance();
        this.router = WarehouseRouter.getInstance();
        this.router.setRouteVariance(minRouteVariance, maxRouteVariance);
        this.router.setMotor(this);

        this.orderVariance = new Normal(medianOrderVariance, varianceOrderVariance, (long)(System.nanoTime()*Math.PI));
        this.generator = new OrderGenerator(this);
        this.generator.setGenerator(new Normal(this.medianOrderCollectVariance, this.varianceOrderCollectVariance));
        setCollectingStationCount();
        this.eList = new EventList();
       
    }

    public void loadSettings(File f)
    {
        Boolean failed = false;
        JSONParser parser = new JSONParser();
        try (Reader reader = new FileReader(f))
        {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            
            this.stationCount = Integer.parseInt((String)jsonObject.get("stationCount"));

            this.collectorCount = new int[this.stationCount];
            JSONArray coll = (JSONArray) jsonObject.get("collectors");
            for (int i = 0; i < this.collectorCount.length;i++)
            {
                this.collectorCount[i] = Integer.parseInt((String)coll.toArray()[i]);
            }

            switch ((String)jsonObject.get("sortType"))
            {
                case "FIFO":    Order.setSortType(SortType.FIFO);
                                break;
                case "SIZE":    Order.setSortType(SortType.SIZE);
                                break;
                case "TIME":    Order.setSortType(SortType.TIME);
                                break;
            }
            this.minOrders = Integer.parseInt((String)jsonObject.get("minimumOrders"));
            this.minRouteVariance = Double.parseDouble((String)jsonObject.get("minRouteVariance"));
            this.maxRouteVariance = Double.parseDouble((String)jsonObject.get("maxRouteVariance"));
            this.medianOrderCollectVariance = Double.parseDouble((String)jsonObject.get("medianOrderCollectVariance"));
            this.varianceOrderCollectVariance = Double.parseDouble((String)jsonObject.get("varianceOrderCollectVariance"));
            this.medianOrderVariance = Double.parseDouble((String)jsonObject.get("medianOrderVariance"));
            this.varianceOrderVariance = Double.parseDouble((String)jsonObject.get("varianceOrderVariance"));
            

        } catch (ParseException e)
        {
            e.printStackTrace();
            failed = true;
           
        } catch (IOException e)
        {
            e.printStackTrace();
            failed = true;
        }
        if (failed)
        {
            Trace.out(Level.ERR, "Could not load settings, loading defaults");
            loadSettings(new File(System.getProperty("user.dir")+"/src/main/resources/options/default.json"));
        }
    }
    
    // For testing purposes
    public void generateOrders(int num)
    {
    	this.generator.createOrders(num);
    }

    @Override
    public void setSimulatorTime(double time)
    {
        this.simTime = NumberFormatter.format(time);
        newEvent(new Event(EventType.END, this.simTime));
    }
    
    @Override
    public synchronized void setDelay(long delay)
    {
    	Trace.out(Level.WAR, "Delay: "+delay);
    	this.delay = delay;
    }
    
    @Override
    public long getDelay()
    {
    	return this.delay;
    	
    }

    public void run()
    {
    	this.generator.initializeOrders(30);
        while(stillSimulating())
        {
            if (currentTime() == -1) break; // If EventList is empty
        	delay();
            clock.setTime(currentTime());
            this.controller.showTime(clock.getTime()); // Show current time in GUI
            runBEvents();
            runCEvents();
        }
        Trace.out(Level.WAR, "Simulation over");
        reportResults();
       
    }

    public void visualize()
    {
        this.controller.visualizeOrders();
    }
    
    @Override
    public void stopSimulation()
    {
        this.simulating = false;
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
        visualize();
    }

    public void runEvent(Event e)
    {
        switch (e.getType())
        {
            case ROUT:  router.routeOrders(stations);
            			this.controller.visualizeOrders();
                        break;
            case COLL:  getCompletedOrders();
                        break;
            case ORDR:  generator.createOrders(this.minOrders+(int)this.orderVariance.sample());
            			break;
            
            case END:	stopSimulation();
            			break;
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
        if (this.simulating && clock.getTime() < simTime)
        {
            return true;
        }
        return false;
    }
    
    public void delay()
    {
    	try
    	{
    		sleep(this.delay);
    	} catch (InterruptedException e)
    	{
    		e.printStackTrace();
    	}
    }

    @Override
    public void clearRouter()
    {
        this.router.empty();
    }

    public void setOrderGeneration(int mean, int variance)
    {
        this.orderVariance = new Normal(mean, variance);
    }
    
    private void setCollectingStationCount()
    {
    	this.stations = new CollectingStation[this.stationCount];
    	
        for (int i = 0; i < stations.length; i++)
        {
            stations[i] = new CollectingStation(this, EventType.COLL, this.collectorCount[i], i);
        }
        
        this.generator.setStations(this.stations);
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
    
    
    // For visualization
    /**
     * Returns the amount of CollectingStaitons
     * @return int amount of orders
     */
	@Override
	public int getCollectingStationCount() {
		return this.stations.length;
	}

	/**
	 * Returns the amount of orders in each CollectingStationg
	 * @return int[] amount of orders in each station as list
	 */
	@Override
	public int[] getStationOrdersCount() {
		int[] orders = new int[this.stations.length];
		for (int i = 0; i < this.stations.length; i++)
		{
			orders[i] = this.stations[i].getOrderCount();
		}
		
		return orders;
	}

	/**
	 * Returns the amount of collectors in each CollectingStaiton
	 * @return int[] amount of collectors in each station as list
	 */
	@Override
	public int[] getStationCollectorCount() {
		int[] collectors = new int[this.stations.length];
		for (int i = 0; i < this.stations.length;i++)
		{
			collectors[i] = this.stations[i].getCollectorCount();
		}
		
		return collectors;
	}

	/**
	 * Returns the amount of orders in the routers queue
	 * @return int amount of orders in routers queue
	 */
	@Override
	public int getRouterOrdersCount() {
		return router.getOrderCount();
	}
    
    public CollectingStation[] getStations()
    {
    	return this.stations;
    }

    @Override
    public List<int[]> getCollectingCollectors() {
        List<int[]> collecting = new ArrayList<>();
        for (CollectingStation c : this.stations)
        {
            collecting.add(c.getCollectingCollectors());
        }
        return collecting;
    }

}



