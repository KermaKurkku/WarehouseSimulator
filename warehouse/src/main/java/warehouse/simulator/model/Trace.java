package warehouse.simulator.model;

/**
 * Class used to handle printing information
 */
public class Trace {

    public enum Level{
    	/**
    	 * Lowest level of importance, general info.
    	 */
    	INFO, 
    	/**
    	 * importance of warnings.
    	 */
    	WAR, 
    	/**
    	 * Highest level of importance for errors.
    	 */
    	ERR}

    private static Level traceLevel;

    /**
     * Sets the level of importance of the prints,
     * @param lvl Level of importance.
     */
    public static void setTraceLevel(Level lvl)
    {
        traceLevel = lvl;
    }

    /**
     * Prints out the given text if it's level is higher
     * or same as the set level.
     * @param lvl Level of importance.
     * @param txt Text to print.
     */
    public static void out(Level lvl, String txt)
    {
        if (lvl.ordinal() >= traceLevel.ordinal())
        {
            System.out.println(txt);
        }
    }

    /**
     * Prints out the given text and result in a formatted
     * form.
     * @param txt Text to be printed.
     * @param reslt results.
     */
    public static void results(String txt, double reslt)
    {
        System.out.printf("%-50s%-8f\n", txt, reslt);
    }
    
}
