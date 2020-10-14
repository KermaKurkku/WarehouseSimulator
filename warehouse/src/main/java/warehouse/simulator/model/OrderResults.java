package warehouse.simulator.model;

import javax.persistence.*;

@Entity
@Table(name="orders")
/**
 * Class used to save Order results into a database.
 * Uses JPA to annotate fields which should be saved 
 * to the database.
 * @author Jere Salmensaari
 */
public class OrderResults
{
    @Id
    @Column(name="date", unique=true)
    private String date ="";
    @Column(name="completed")
    private int completedOrders = 0;
    @Column(name="uncompleted")
    private int uncompletedOrders = 0;
    @Column(name="average")
    private double avrgTime = 0;
    @Column(name="late")
    private int lateOrders = 0;

    /**
     * Sets the date of the results.
     * @param date String to be set.
     */
    public void setDate(String date)
    {
        this.date = date;
    }

    /**
     * Returns the date of the results.
     * @return date Date of the results.
     */
    public String getDate()
    {
        return this.date;
    }

    /**
     * Sets the amount of completed orders of the simulation.
     * @param completed Amount of completed orders.
     */
    public void setCompleted(int completed)
    {
        this.completedOrders = completed;
    }

    /**
     * Returns the amount of completed orders.
     * @return completedOrders Amount of completed orders.
     */
    public int getCompleted()
    {
        return this.completedOrders;
    }

    /**
     * Sets the amount of uncompleted orders of the simulation.
     * @param uCompleted Amount of uncompleted orders.
     */
    public void setUncompleted(int uCompleted)
    {
        this.uncompletedOrders = uCompleted;
    }
    
    /**
     * Returns the amount of uncompleted orders of the simulation.
     * @return uncompletedOrders Amount of uncomplete orders.
     */
    public int getUncompleted()
    {
        return this.uncompletedOrders;
    }

    /**
     * Sets the average time of the completed orders.
     * @param avrg Average time of completed orders.
     */
    public void setAverage(double avrg)
    {
        this.avrgTime = avrg;
    }

    /**
     * Returns the average time of completed orders.
     * @return avrgTime Average time of completed orders.
     */
    public double getAverage()
    {
        return this.avrgTime;
    }

    /**
     * Set the amount of late orders in the simulation.
     * @param lateOrders Amount of late orders in the system.
     */
    public void setLate(int lateOrders)
    {
        this.lateOrders = lateOrders;
    }

    /**
     * Returns the amount of late orders in results.
     * @return lateOrders Amount of late orders.
     */
    public int getLate()
    {
        return this.lateOrders;
    }

    
}