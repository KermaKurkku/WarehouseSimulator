package warehouse;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import warehouse.simulator.dao.IOrderDAO;
import warehouse.simulator.dao.OrderDataAccessObject;
import warehouse.simulator.model.OrderResults;
import warehouse.simulator.model.Trace;
import warehouse.simulator.model.Trace.Level;

/**
 * Test class for the simulator. Used to test saving to databae.
 * @author Jere Salmensaari
 *
 */
public class dbTest {
	private static IOrderDAO oDAO;
	private static OrderResults result;
	
	@BeforeAll
	/**
	 * Creates a new OrderResults object and a
	 * OrderDataAccessObject.
	 */
	public static void initialize()
	{
		
        Trace.setTraceLevel(Level.INFO);
        result = new OrderResults();
		result.setDate("Test");
		result.setCompleted(1);
		result.setLate(2);
		result.setAverage(1.5);
        result.setUncompleted(3);
        
        System.out.println("Uusi DAO");
		oDAO = new OrderDataAccessObject();
	}
	
	@Test
	/**
	 * Tries to write the results object to the database.
	 */
	public void write()
	{
		assertTrue(oDAO.writeOrderResults(result), "Failed write");
	}

	@Test
	/**
	 * Tries to read the written object
	 */
	public void read()
	{
		String date = java.time.LocalDate.now().toString();
		OrderResults returnResult = oDAO.readOrderResults(date);
		assertTrue(returnResult!=null, "Failed to read");
	}
	
}
