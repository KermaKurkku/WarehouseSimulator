package warehouse.simulator.util;

public class NumberFormatter {

	/**
     *  This class sets numbers to be 2 decimal paces
     *  This looks cleaner and is easier to ceep track of
     * @param num Number to be formatted
     * @return  Formatted number
     */
	public static double format(double num)
    {
        
        num = Double.parseDouble(String.format("%.2f", num));
        return num;
    }
	
}
