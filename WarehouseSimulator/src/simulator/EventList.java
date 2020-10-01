package simulator;

import java.util.PriorityQueue;

/**
 * This class is responsible for keeping a list of all events in the system
 * 
 */

 // TODO rest of javadoc

public class EventList {
    private PriorityQueue<Event> queue = new PriorityQueue<Event>();

    public EventList(){};

    /**
     * Removes the top from the list
     * @return Top event of the list
     */
    public Event removeEvent()
    {
        Trace.out(Trace.Level.INFO,"Poisto "+ this.queue.peek().getType());
        return this.queue.remove();
    }

    /**
     * Adds an Event to the list
     * @param e Event to be added
     */
    public void add(Event e)
    {
        this.queue.add(e);
    }

    /**
     * Returns the time of the next Event
     * @return Time of next event
     */
    public double getNextTime()
    {
        if (this.queue.isEmpty()) return -1;
        return this.queue.peek().getTime();
    }

    /**
     * Check if list is empty
     * @return true if list is empty, false if not
     */
    public boolean isEmpty()
    {
        return this.queue.isEmpty();
    }

    public void printEvents()
    {
        if (isEmpty()) System.out.println("Lista on tyhjä");
        for (Event e : this.queue)
        {
            System.out.println(e.getType()+ " "+ e.getTime());
        }
    }
}
