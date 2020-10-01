package simulator;

public class Trace {

    public enum Level{INFO, WAR, ERR}

    private static Level traceLevel;

    public static void setTraceLevel(Level lvl)
    {
        traceLevel = lvl;
    }

    public static void out(Level lvl, String txt)
    {
        if (lvl.ordinal() >= traceLevel.ordinal())
        {
            System.out.println(txt);
        }
    }

    public static void results(String txt, double reslt)
    {
        System.out.printf("%-50s%-8f\n", txt, reslt);
    }
    
}
