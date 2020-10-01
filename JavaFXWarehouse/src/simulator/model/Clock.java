package simulator.model;

import simulator.util.NumberFormatter;

// TODO javadoc
public class Clock {
    private double time;
    private static Clock INSTANCE;

    private Clock()
    {
        time = 0;
    }

    public static Clock getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new Clock();
        }
        return INSTANCE;
    }

    public void setTime(double time)
    {
        this.time = NumberFormatter.format(time);
    }

    public double getTime()
    {
        return this.time;
    }
}
