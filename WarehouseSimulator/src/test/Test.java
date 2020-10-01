package test;

import simulator.*;
import simulator.Order.SortType;
import simulator.Trace.Level;

// TODO test 
// TODO later make a main and interface

public class Test {
    public static void main(String[] args)
    {
        Trace.setTraceLevel(Level.INFO);
        Order.setSortType(SortType.SIZE);
        Motor motor = new Motor();
       
        motor.setSimulatorTime(24);
        motor.run();
        
    }
}
