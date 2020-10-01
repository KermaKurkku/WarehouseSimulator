package simulator;

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
        this.time = Motor.formatTime(time);
    }

    public double getTime()
    {
        return this.time;
    }
}
