package simulator;

/**
 * This class is used to represent a singular order the warehouse gets
 */

// TODO rest of javadoc

// TODO add departure time for orders

public class Order implements Comparable<Order>{
    private double arrivalTime;
    private double exitTime;
    private double targetLeaveTime;

    private final int[] leaveTimes = {
        10,
        14,
        18,
        22
    };

    private int id;
    private static int i = 1;
    private static double sum = 0;
    private double collectTime;
    private CollectingStation collectingStation;

    public enum SortType {
        SIZE,
        FIFO,
        TIME,
    }
    private static SortType sortType;

    /**
     * Used for setting the sorting method of orders
     * @param type Sorting type
     */
    public static void setSortType(SortType type)
    {
        sortType = type;
    }
    
    public Order(double collectTime, int leaveTime, CollectingStation station)
    {
        this.id = i++;
        this.collectTime = Motor.formatTime(Math.abs(collectTime));
        this.targetLeaveTime = leaveTimes[leaveTime];
        this.collectingStation = station;
        this.arrivalTime = Clock.getInstance().getTime();

        arrivalTime = Clock.getInstance().getTime();
        Trace.out(Trace.Level.INFO, "Uusi tilaus:"+id+":"+this.arrivalTime+":"+this.collectTime+":"+this.collectingStation);
    }

    public int getId()
    {
        return this.id;
    }

    public double getExitTime()
    {
        return this.exitTime;
    }

    public void setExitTime(double exitTime)
    {
        this.exitTime = Motor.formatTime(exitTime);
        Order.sum += (this.exitTime-this.arrivalTime);
    }

    public double getArrivalTime()
    {
        return this.arrivalTime;
    }

    public void setArrivalTime(double arrTime)
    {
        this.arrivalTime = Motor.formatTime(arrTime);
    }

    public double getCollectionTime()
    {
        return this.collectTime;
    }

    public double getTargetLeaveTime()
    {
        return this.targetLeaveTime;
    }

    public CollectingStation getStation()
    {
        return this.collectingStation;
    }

    public void report()
    {
        Trace.out(Trace.Level.INFO, "Tilaus: "+this.id+" saapui: "+this.arrivalTime);
        Trace.out(Trace.Level.INFO, "Tilaus: "+ this.id+" poistui: "+this.exitTime);
        Trace.out(Trace.Level.INFO, "Tilaus: "+this.id+" viipyi: "+(this.exitTime-this.arrivalTime));
        double avrg = (double)(Order.sum/i);
        Trace.out(Trace.Level.WAR, "Tilausten läpimenoaikojen keskiarvo "+Motor.formatTime(avrg));
    }

    public static double getSumTime()
    {
        return sum;
    }

    @Override
    public String toString()
    {
        return "Order:"+id+": "+this.arrivalTime+":"+this.collectTime; 
    }

    @Override
    public int compareTo(Order arg)
    {

        switch (sortType)
        {
            case FIFO:  return sortFIFO(arg);
            
            case SIZE:  return sortSIZE(arg);

            case TIME:  return sortTIME(arg);
                        
        }
        return 0;


        /* if (this.arrivalTime < arg.arrivalTime) return -1;
        else if (this.arrimport simulator.Order.SortType;
ivalTime > arg.arrivalTime) return 1;
        else return 0; */
    }

    private int sortFIFO(Order arg)
    {
        if (this.arrivalTime < arg.arrivalTime) return -1;
        else if (this.arrivalTime > arg.arrivalTime) return 1;
        else return 0;
    }

    private int sortSIZE(Order arg)
    {
        if (this.collectTime < arg.collectTime) return -1;
        else if (this.collectTime > arg.collectTime) return 1;
        else return 0;
    }

    private int sortTIME(Order arg)
    {
        if (this.targetLeaveTime > arg.targetLeaveTime) return -1;
        else if (this.targetLeaveTime < arg.targetLeaveTime) return 1;
        else return 0;
    }

}
