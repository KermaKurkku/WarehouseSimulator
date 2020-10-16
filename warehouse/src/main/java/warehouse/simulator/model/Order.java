package warehouse.simulator.model;

import warehouse.simulator.util.NumberFormatter;

/**
 * Class used to represent orders in the warehouse.
 * @author Jere Salmensaari
 */
public class Order implements Comparable<Order>{
    private double arrivalTime;
    private double exitTime;
    private double targetLeaveTime;

    private static final int[] leaveTimes = {
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

    /**
     * Different types for sorting orders.
     * @author Jere Salmensaari
     *
     */
    public enum SortType {
    	/**
    	 * Sorting by collect time.
    	 */
        SIZE,
        /**
         * Sorting by arrival time, first in - first out.
         */
        FIFO,
        /**
         * Sorting by leave time.
         */
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
    
    /**
     * Constructor for creating an order.
     * @param collectTime Time to collect the order.
     * @param leaveTime Time when the order should leave.
     * @param station CollectingStation the order should be collected.
     */
    public Order(double collectTime, int leaveTime, CollectingStation station)
    {
        this.id = i++;
        this.collectTime = NumberFormatter.format(Math.abs(collectTime));
        this.targetLeaveTime = leaveTimes[leaveTime];
        this.collectingStation = station;
        this.arrivalTime = Clock.getInstance().getTime();

        arrivalTime = Clock.getInstance().getTime();
        Trace.out(Trace.Level.INFO, "Uusi tilaus:"+id+":"+this.arrivalTime+":"+this.collectTime+":"+this.collectingStation);
    }

    /**
     * Returns the Id of the order.
     * @return id Id of the order.
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * Returns the exit time of the order.
     * @return exitTime Exit time of the order.
     */
    public double getExitTime()
    {
        return this.exitTime;
    }

    /**
     * Sets the exit time of the order.
     * @param exitTime Exit time to be set.
     */
    public void setExitTime(double exitTime)
    {
        this.exitTime = NumberFormatter.format(exitTime);
        Order.sum += (this.exitTime-this.arrivalTime);
    }

    /**
     * Returns the arrival time of the order.
     * @return arrivalTime Arrival time of the order.
     */
    public double getArrivalTime()
    {
        return this.arrivalTime;
    }

    /**
     * Sets the arrival time of the order.
     * @param arrTime Arrival time to be set.
     */
    public void setArrivalTime(double arrTime)
    {
        this.arrivalTime = NumberFormatter.format(arrTime);
    }

    /**
     * Returns the collecting time of the order.
     * @return collectTime Collecting time of the order.
     */
    public double getCollectionTime()
    {
        return this.collectTime;
    }

    /**
     * Returns the target leave time of the order.
     * @return targetLeaveTime Target leave time of the order.
     */
    public double getTargetLeaveTime()
    {
        return this.targetLeaveTime;
    }

    /**
     * Returns the CollectingStation the orders should be collected in.
     * @return collectingStation CollectingStation the order is collected in.
     */
    public CollectingStation getStation()
    {
        return this.collectingStation;
    }

    /**
     * Prints out all the information of the order.
     */
    public void report()
    {
        Trace.out(Trace.Level.INFO, "Order: "+this.id+" arrived: "+this.arrivalTime);
        Trace.out(Trace.Level.INFO, "Order: "+ this.id+" left: "+this.exitTime);
        Trace.out(Trace.Level.INFO, "Order: "+this.id+" stayed: "+(this.exitTime-this.arrivalTime));
        double avrg = (double)(Order.sum/i);
        Trace.out(Trace.Level.WAR, "Average collecting time of orders: "+NumberFormatter.format(avrg));
    }

    /**
     * Returns the total collecting time of all orders.
     * @return sum Total time of all orders.
     */
    public static double getSumTime()
    {
        return sum;
    }

    @Override
    /**
     * String representation of the order.
     */
    public String toString()
    {
        return "Order:"+id+": "+this.arrivalTime+":"+this.collectTime; 
    }

    @Override
    /**
     * Custom compare method for sorting orders.
     */
    public int compareTo(Order arg)
    {

        switch (sortType)
        {
            case FIFO:  return sortFIFO(arg);
            
            case SIZE:  return sortSIZE(arg);

            case TIME:  return sortTIME(arg);
                        
        }
        return 0;

    }

    /**
     * Order sorting by arrival time.
     * @param arg Order to compare.
     * @return -1, 1 or 0 Depending if arg arrivalTime 
     * is bigger or smaller.
     */
    private int sortFIFO(Order arg)
    {
        if (this.arrivalTime < arg.arrivalTime) return -1;
        else if (this.arrivalTime > arg.arrivalTime) return 1;
        else return 0;
    }

    /**
     * Order sorting by collectTime.
     * @param arg Order to compare.
     * @return -1, 1 or 0 Depending if arg collectTime 
     * is bigger or smaller.
     */
    private int sortSIZE(Order arg)
    {
        if (this.collectTime < arg.collectTime) return -1;
        else if (this.collectTime > arg.collectTime) return 1;
        else return 0;
    }

    /**
     * Order sorting by leaveTime.
     * @param arg Order to compare.
     * @return -1, 1 or 0 Depending if arg leaveTime 
     * is bigger or smaller.
     */
    private int sortTIME(Order arg)
    {
        if (this.targetLeaveTime > arg.targetLeaveTime) return -1;
        else if (this.targetLeaveTime < arg.targetLeaveTime) return 1;
        else return 0;
    }
    
    /**
     * Return all possible leave times of orders.
     * @return leaveTimes Array of possible leave times.
     */
    public static int[] getLeaveTimes()
    {
    	return leaveTimes;
    }

}
