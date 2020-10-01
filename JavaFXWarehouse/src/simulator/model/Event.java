package simulator.model;

import simulator.util.NumberFormatter;

// TODO javadoc

public class Event implements Comparable<Event>{
    private EventType type;
    private double time;

    public Event(EventType type, double time)
    {
        this.type = type;
        this.time = NumberFormatter.format(time);
    }

    public void setType(EventType type)
    {
        this.type = type;
    }

    public EventType getType()
    {
        return this.type;
    }

    public void setTime(double time)
    {
        this.time = time;
    }

    public double getTime()
    {
        return this.time;
    }

    @Override
    public int compareTo(Event arg)
    {
        if (this.time < arg.time) return -1;
        else if (this.time > arg.time) return 1;
        return 0;
    }

}
