package warehouse.simulator.view.animation;

import javafx.geometry.Point2D;
import java.util.LinkedList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StationWithList {
	private Rectangle[] subRectangles = null;
	private Rectangle mainRectangle = null;
	
	public StationWithList(Rectangle rect)
	{
		mainRectangle = rect;
		rect.setFill(Color.GREEN);
		rect.setArcHeight(2);
		rect.setArcWidth(2);
	}
	
	public Rectangle getMainRectangle()
	{
		return mainRectangle;
	}
	
	public void setMainRectangle(Rectangle rect)
	{
		mainRectangle = rect;
	}
	
	public void drawSubRectangles(int count)
	{
		Rectangle rect;
		subRectangles = new Rectangle[count];
		Point2D location = new Point2D(mainRectangle.getX()-5, mainRectangle.getY());
		for (int i = 0; i < count; i++)
		{
			rect = new Rectangle(location.getX(), location.getY(), 3, mainRectangle.getHeight());
			rect.setFill(Color.ORANGE);
			subRectangles[i] = rect;
			if (i != 0) 
			{
				location = new Point2D(location.getX()-5, location.getY());
			}
		}
	}
	
	public Rectangle[] getSubRectangles()
	{
		return subRectangles;
	}
	

}
