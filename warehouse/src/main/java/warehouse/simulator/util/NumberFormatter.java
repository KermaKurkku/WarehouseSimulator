package warehouse.simulator.util;

/**
 * Utility class for formatting numbers.
 * @author Jere Salmensaari
 */
public class NumberFormatter {

	/**
     *  Sets numbers to be 2 decimal paces
     *  This looks cleaner and is easier to ceep track of.
     * @param num Number to be formatted.
     * @return num Formatted number.
     */
	public static double format(double num)
    {
        
        num = Double.parseDouble(String.format("%.2f", num));
        return num;
    }
	
}
