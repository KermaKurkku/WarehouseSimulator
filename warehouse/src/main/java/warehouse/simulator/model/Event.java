package warehouse.simulator.model;

import warehouse.simulator.util.NumberFormatter;

/**
 * Class to create an event for the warehouse.
 * @author Jer Salmensaari
 */
public class Event implements Comparable<Event>{
    private EventType type;
    private double time;

    /**
     * Constructor for the class
     * @param type Event type of the event.
     * @param time Time when the event happens.
     */
    public Event(EventType type, double time)
    {
        this.type = type;
        this.time = NumberFormatter.format(time);
    }

    /**
     * Sets the type of the event.
     * @param type Event type to be set.
     */
    public void setType(EventType type)
    {
        this.type = type;
    }

    /**
     * Returns the type of the Event.
     * @return type EventType of the event
     */
    public EventType getType()
    {
        return this.type;
    }

    /**
     * Sets the time of the event.
     * @param time Time to be set.
     */
    public void setTime(double time)
    {
        this.time = time;
    }

    /**
     * Returns the time of the event.
     * @return time Time of the event.
     */
    public double getTime()
    {
        return this.time;
    }

    @Override
    /**
     * Custom comparing for the event, to compare their time.
     */
    public int compareTo(Event arg)
    {
        if (this.time < arg.time) return -1;
        else if (this.time > arg.time) return 1;
        return 0;
    }

}
