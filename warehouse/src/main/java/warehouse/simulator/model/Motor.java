package warehouse.simulator.model;

import eduni.distributions.*;
import warehouse.simulator.controller.IController;
import warehouse.simulator.dao.IOrderDAO;
import warehouse.simulator.dao.OrderDataAccessObject;
import warehouse.simulator.model.Trace.Level;
import warehouse.simulator.util.NumberFormatter;
import warehouse.simulator.model.Order.SortType;

import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;

//TODO javadoc
/**
 * Motor class of the simulation. Responsible for excecuting 
 * all events that happen in the simulation. The motor
 * handles communication with all of the other model layer
 * classes and communicates to SimulationController.
 * @author Jere Salmensaari
 */
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

    private IOrderDAO orderDAO;

    /**
     * Nonparametric constructor for the Motor.
     * Mainly for testing purposes.
     */
    public Motor()
    {
        this.orderDAO = new OrderDataAccessObject();

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
        this.router.setRouteVariance(minRouteVariance, maxRouteVariance);
        this.router.setMotor(this);

        this.orderVariance = new Normal(medianOrderVariance, varianceOrderVariance, (long)(System.nanoTime()*Math.PI));
        this.generator = new OrderGenerator(this);
        this.generator.setGenerator(new Normal(this.medianOrderCollectVariance, this.varianceOrderCollectVariance));
        setCollectingStationCount();
        this.eList = new EventList();
       
    }
    
    /**
     * The main constructor for the motor. Creates all
     * the needed parts for the motor to work.
     * @param controller SimulationController of the simulation.
     */
    public Motor(IController controller)
    {
        this.orderDAO = new OrderDataAccessObject();

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

    /**
     * Loads the settings from the given JSON file.
     * If the given file cannot be read, reads the default file.
     * @param f JSON file to load settings from.
     */
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
    
    /**
     * Generates a given number of orders.
     * @param num Amount of orders to generate.
     */
    public void generateOrders(int num)
    {
    	this.generator.createOrders(num);
    }

    @Override
    /**
     * Sets the end time of the simulation.
     */
    public void setSimulatorTime(double time)
    {
        this.simTime = NumberFormatter.format(time);
    }
    
    @Override
    /**
     * Sets the delay of the motor.
     */
    public synchronized void setDelay(long delay)
    {
    	Trace.out(Level.WAR, "Delay: "+Math.abs(delay));
    	this.delay = Math.abs(delay);
    }
    
    @Override
    /**
     * Returns the delay of the motor.
     * @return delay Delay time of the motor.
     */
    public long getDelay()
    {
    	return this.delay;
    	
    }

    /**
     * Starts the simulation.
     */
    public void run()
    {
    	this.generator.initializeOrders(30);
        while(stillSimulating())
        {
            if (currentTime() == -1) break; // If EventList is empty
        	delay();
            clock.setTime(currentTime());
            this.controller.showTime(clock.getTime()); // Show current time in GUI
            this.controller.setNextLeaveTime();
            runBEvents();
            runCEvents();
        }
        Trace.out(Level.WAR, "Simulation over");
        reportResults();
       
    }

    /**
     * Calls the controller to draw orders.
     */
    public void visualize()
    {
        this.controller.visualizeOrders();
    }
    
    @Override
    /**
     * Stops the simulatio.
     */
    public void stopSimulation()
    {
        this.simulating = false;
    }

    /**
     * Checks if the time of the next event in the
     * event list matches the current time and
     * runs that event.
     */
    public void runBEvents()
    {
        while (eList.getNextTime() == clock.getTime())
        {
            runEvent(eList.removeEvent());
        }
    }

    /**
     * Checks if some of the CollectingStaions have
     * open collectors and starts collecting.
     * Updates the visualization.
     */
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

    /**
     * Runs a given event.
     * @param e Event to run.
     */
    public void runEvent(Event e)
    {
        switch (e.getType())
        {
            case ROUT:  router.routeOrders();
            			this.controller.visualizeOrders();
                        break;
            case COLL:  getCompletedOrders();
                        break;
            case ORDR:  generator.createOrders(this.minOrders+(int)this.orderVariance.sample());
            			break;
        }
    }

    /**
     * Adds a new event to the event list.
     * @param e New event.
     */
    public void newEvent(Event e)
    {
        eList.add(e);
    }

    /**
     * Returns the next time in the event list.
     * @return Next time in the event list.
     */
    public double currentTime()
    {
        return eList.getNextTime();
    }

    /**
     * Returns true if current time is smaller than the 
     * maximum simulation time.
     * @return True or false.
     */
    public boolean stillSimulating()
    {
        Trace.out(Trace.Level.INFO, "Kello on: " + clock.getTime());
        if (this.simulating && clock.getTime() < simTime)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Puts the motor to sleep for a given time.
     */
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
    /**
     * Clears the WarehouseRouter of orders
     */
    public void clearRouter()
    {
        this.router.empty();
    }

    /**
     * Sets a new generator for the amount of orders
     * that the OrderGenerator generates.
     * @param mean Mean value of the generator.
     * @param variance Variance of the generator.
     */
    public void setOrderGeneration(int mean, int variance)
    {
        this.orderVariance = new Normal(mean, variance);
    }
    
    /**
     * Sets the amount of CollectingStations.
     */
    private void setCollectingStationCount()
    {
    	this.stations = new CollectingStation[this.stationCount];
    	
        for (int i = 0; i < stations.length; i++)
        {
            stations[i] = new CollectingStation(this, EventType.COLL, this.collectorCount[i], i);
        }
        
        this.generator.setStations(this.stations);
    }

    /**
     * Gets all of the collected orders from all collectors
     * and sends them to the loading bay.
     */
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

    /**
     * Finishes the order that is given to the method.
     * @param ordr Completed order.
     */
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

    /**
     * Returns the simulations end time.
     * @return simTime Time when the simulation ends.
     */
    public double getSimTime()
    {
        return this.simTime;
    }

    /**
     * Saves the results of the current simulation to a 
     * OrderResults object and writes it into a database.
     */
    public void reportResults()
    {
        System.out.println("---");
        Trace.results("Completed orders: ",this.completedOrders);
        Trace.results("Orders left uncompleted: ", (router.getTotalOrders() - this.completedOrders));
        Trace.results("Average time of orders completed orders: ", (Order.getSumTime() / (double)this.completedOrders));
        Trace.results("Late orders", this.lateOrders);
        OrderResults result = new OrderResults();
        result.setCompleted(this.completedOrders);
        result.setUncompleted(router.getTotalOrders()-this.completedOrders);
        result.setAverage((double)Order.getSumTime()/(double)this.completedOrders);
        result.setLate(this.lateOrders);

        this.orderDAO.writeOrderResults(result);

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
    
    /**
     * Returns an array of CollectingStations in the motor.
     * return stations Array of CollectingStations.
     * @return stations Array of CollectingStations.
     */
    public CollectingStation[] getStations()
    {
    	return this.stations;
    }

    @Override
    /**
     * Returns an array of arrays containing the status of
     * Collectors collecting in each CollectingStation.
     * Collectors return a 1 if collecting and a 0 if not.
     * @return collecting Array of collecting collectors.
     */
    public List<int[]> getCollectingCollectors() {
        List<int[]> collecting = new ArrayList<>();
        for (CollectingStation c : this.stations)
        {
            collecting.add(c.getCollectingCollectors());
        }
        return collecting;
    }
    
    @Override
    /**
	 * Returns the dates of saved results.
	 * @return dateData Array of names of saved results.
	 */
    public String[] getDateData()
    {
    	return this.orderDAO.getKeys();
    }
    
    @Override
    /**
	 * Returns the data of a single saved result.
	 * @param date Key for the saved result.
	 * @return OrderResults Results of the key.
	 */
    public OrderResults getResults(String date)
    {
    	return this.orderDAO.readOrderResults(date);
    }
    
    @Override
    /**
	 * Returns the next leave time.
	 * @return Next leave time of orders.
	 */
    public int getNextLeaveTime()
    {
    	int[] leaveTimes = Order.getLeaveTimes();
    	double currTime = clock.getTime();
    	int returnable = 0;
    	for (int i : leaveTimes)
    	{
    		if (currTime <= i) 
    		{
    			returnable = i;
    			break;
    		}
    	}
    	
    	return returnable;
    }

}



