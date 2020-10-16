package warehouse.simulator.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import warehouse.simulator.model.OrderResults;
import warehouse.simulator.model.Trace;
import warehouse.simulator.model.Trace.Level;

/**
 * Class used to write OrderResults objects to a database.
 * Uses the <a href="http://hibernate.org/">org.hibernate</a> library.
 * @author Jere Salmensaari
 */
public class OrderDataAccessObject implements IOrderDAO{
    private SessionFactory sessionF = null;
    private int i = 1;
    private String date;

    /**
     * Constructor of the class. Creates a sessionFactory
     * for communicating with the database.
     */
    public OrderDataAccessObject()
    {
        String cwd = System.getProperty("user.dir");

        try 
        {
            File f = new File(cwd+"/src/hibernate.cfg.xml");
            sessionF = new Configuration().configure(f).buildSessionFactory();
        } catch (Exception e)
        {
            Trace.out(Level.ERR, "Unable to create a sessionFactory: "+e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    /**
     * Writes the results in of the given OrderResults object
     * in a database.
     * @param results OrderResults to be written.
     * @return True or false.
     */
	public boolean writeOrderResults(OrderResults results) {
        this.date = java.time.LocalDate.now().toString();
        System.out.println(this.date);
        OrderResults test = readOrderResults(this.date);
        while (test != null)
        {
            this.date = java.time.LocalDate.now().toString()+"_"+i;
            i++;
            test = readOrderResults(this.date);
        }
        
        results.setDate(this.date);
        Transaction transAct = null;
        try (Session session = sessionF.openSession())
        {
            transAct = session.beginTransaction();
            session.saveOrUpdate(results);
            transAct.commit();
        } catch (Exception e)
        {
            if (transAct != null) transAct.rollback();
            Trace.out(Level.ERR, "Error: "+e.getMessage());
            return false;
        }

        return true;
	}

    @Override
    /**
     * Reads the results of a given key.
     * @param date Key of the results that are wanted.
     * @return OrderResults object containing the results.
     */
	public OrderResults readOrderResults(String date) {
        OrderResults returnable = null;
        try (Session session = this.sessionF.openSession())
        {
            session.beginTransaction();
            returnable = session.get(OrderResults.class, date);
            session.getTransaction().commit();
        } catch (ObjectNotFoundException e)
        {
            Trace.out(Level.ERR, "Error: "+e.getMessage());
        }
		return returnable;
	}

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Returns all keys in the database.
     * @return Array of keys.
     */
    public String[] getKeys() {
        List<OrderResults> keys = new ArrayList<>();
        String[] dates = null;
        try (Session session = this.sessionF.openSession())
        {
        	keys = session.createQuery("FROM OrderResults").getResultList();
        	dates = new String[keys.size()];
            for (int i = 0; i < keys.size(); i++)
            {
            	dates[i] = keys.get(i).getDate();
            }
            Arrays.sort(dates);
        } catch (Exception e)
        {
        	Trace.out(Level.ERR,"Error: "+e.getMessage());
        }
        
                
        return dates;
    }


}
