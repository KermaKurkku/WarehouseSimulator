package test;

import simulator.*;
import simulator.model.Motor;
import simulator.model.Order;
import simulator.model.Trace;
import simulator.model.Order.SortType;
import simulator.model.Trace.Level;

// TODO test 
// TODO later make a main and interface

public class Test {
    public static void main(String[] args)
    {
        Trace.setTraceLevel(Level.INFO);
        Order.setSortType(SortType.SIZE);
//        Motor motor = new Motor();
//       
//        motor.setSimulatorTime(24);
//        motor.run();
        
    }
}
