package warehouse.simulator.model;

import warehouse.simulator.util.NumberFormatter;

/**
 * Clock singleton used to keep track of the time
 * in the simulation.
 * @author Jere Salmensaari
 */
public class Clock {
    private double time;
    private static Clock INSTANCE;

    /** 
     * Private constructor sets the time to 0.
     */
    private Clock()
    {
        time = 0;
    }

    /**
     * Returns the instance of the clock
     * @return this Instance of the clock.
     */
    public static Clock getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new Clock();
        }
        return INSTANCE;
    }

    /**
     * Sets the time in the clock.
     * @param time Time to be set.
     */
    public void setTime(double time)
    {
        this.time = NumberFormatter.format(time);
    }

    /**
     * Gets the time in the clock.
     * @return time Time in clock.
     */
    public double getTime()
    {
        return this.time;
    }
}
