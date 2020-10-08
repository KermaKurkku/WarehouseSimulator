package warehouse.simulator.view.animation;

import java.lang.System.Logger.Level;

import javafx.geometry.Point2D;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StationWithList {
	private Rectangle[] subRectangles = null;
	private Rectangle mainRectangle = null;
	private Text info = null;
	
	public StationWithList(Rectangle rect, String text)
	{
		mainRectangle = rect;
		rect.setFill(Color.GREEN);
		rect.setArcHeight(2);
		rect.setArcWidth(2);
		info = new Text();
		info.setX(mainRectangle.getX());
		info.setY(mainRectangle.getY()-10);
		info.setText(text);
		info.setFont(new Font("Arial", 11));
	}
	
	public Rectangle getMainRectangle()
	{
		return mainRectangle;
	}

	public Text getText()
	{
		return info;
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
			if (count > 30)
			{
				rect = new Rectangle(location.getX(), location.getY(), 3, mainRectangle.getHeight()/2-2);
			} else if (count > 30 && i > 30)
			{
				rect = new Rectangle(location.getX(), location.getY(), 3, mainRectangle.getHeight());
			} else
			{
				rect = new Rectangle(location.getX(), location.getY(), 3, mainRectangle.getHeight());
			}
			
			
			rect.setFill(Color.ORANGE);
			subRectangles[i] = rect;
			if (i == 29) 
			{
				location = new Point2D(mainRectangle.getX()-5, location.getY()+mainRectangle.getHeight()/2+2);				
			} else if (i != 0)
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
