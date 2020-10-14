package warehouse.simulator.model;

import java.util.PriorityQueue;

/**
 * Class is responsible for keeping a 
 * list of all events in the system.
 * @author Jere Salmensaari
 */
public class EventList {
    private PriorityQueue<Event> queue = new PriorityQueue<Event>();

    public EventList(){};

    /**
     * Removes the top from the list.
     * @return Top event of the list.
     */
    public Event removeEvent()
    {
        Trace.out(Trace.Level.INFO,"Poisto "+ this.queue.peek().getType());
        return this.queue.remove();
    }

    /**
     * Adds an Event to the list.
     * @param e Event to be added.
     */
    public void add(Event e)
    {
        this.queue.add(e);
    }

    /**
     * Returns the time of the next Event.
     * @return Time of next event.
     */
    public double getNextTime()
    {
        if (this.queue.isEmpty()) return -1;
        return this.queue.peek().getTime();
    }

    /**
     * Returns true if the list is empty.
     * @return True or false.
     */
    public boolean isEmpty()
    {
        return this.queue.isEmpty();
    }

}
