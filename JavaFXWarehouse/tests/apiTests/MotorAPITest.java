package apiTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eduni.distributions.Normal;
import simulator.model.IMotor;
import simulator.model.Motor;
import simulator.model.Order;
import simulator.model.OrderGenerator;
import simulator.model.Trace;
import simulator.model.WarehouseRouter;
import simulator.model.Order.SortType;
import simulator.model.Trace.Level;

class MotorAPITest {
	static Motor motor;
	
	@BeforeAll
	static void setup()
	{
		Trace.setTraceLevel(Level.WAR);
		Order.setSortType(SortType.FIFO);
		motor = new Motor();
		motor.setCollectingStationCount(2);	
		motor.generateOrders(5);
	}
	
	
	@Test
	void collectingStationCount() {
		IMotor apiMotor = motor;
		int count = apiMotor.getCollectingStationCount();
		assertEquals(2, count, "Wrong amount of CollectingStations in motor");
	}
	
	@Test
	void collectorCount()
	{
		IMotor apiMotor = motor;
		int[] collector = apiMotor.getStationCollectorCount();
		int total = 0;
		for (int i : collector)
		{
			total += i;
		}
		assertEquals(5, total, "Incorrect amount of collectors");
		assertEquals(2, collector[0], "Incorrect amount of collectors on station 1");
		assertEquals(3, collector[1], "Incorrect amount of collectors on station 2");
		
	}
	
	@Test
	void stationOrderCount()
	{
		IMotor apiMotor = motor;
		int[] orders = apiMotor.getStationOrdersCount();
		int total = 0;
		for (int i : orders)
		{
			total += i;
		}
		assertEquals(0, total, "Incorrect amount of orders in stations");
	}
	
	@Test
	public void routerOrderCount() {
		IMotor apiMotor = motor;
		int orders = apiMotor.getRouterOrdersCount();
		assertEquals(5, orders, "Incorrect amount of orders in router");
		motor.generateOrders(5);
		orders = apiMotor.getRouterOrdersCount();
		assertEquals(10, orders, "Incorrect amount of orders in router after generating new ones");
	}

}
