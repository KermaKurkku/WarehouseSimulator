package simulator.view;

import java.awt.geom.Point2D;
import java.util.LinkedList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StationWithList {
	private LinkedList<Rectangle> subRectangles;
	private Rectangle mainRectangle;
	
	public StationWithList(Rectangle rect)
	{
		mainRectangle = rect;
		rect.setFill(Color.GREEN);
		rect.setArcHeight(2);
		rect.setArcWidth(2);
		subRectangles = new LinkedList<>();
	}
	
	public Rectangle getMainRectangle()
	{
		return mainRectangle;
	}
	
	public void setMainRectangle(Rectangle rect)
	{
		mainRectangle = rect;
	}
	
	public void addSubRectangle(Rectangle rect)
	{
		subRectangles.add(rect);
	}
	
	/**
	 * Returns the last rectangle or null if empty, and removes it from list
	 * @return rect Last rectangle of list or null if empty
	 */
	public Rectangle removeLastRectangle()
	{
		if (!subRectangles.isEmpty())
		{
			Rectangle rect = getLastRectangle();
			subRectangles.removeLast();
			return rect;
		}
		return null;
	}
	
	public Rectangle getLastRectangle()
	{
		return subRectangles.getLast();
	}
	
	public Point2D getLastCordinates()
	{
		return new Point2D.Double(subRectangles.getLast().getX(), subRectangles.getLast().getY());
	}
}
