package warehouse.simulator.view.animation;

import javafx.geometry.Point2D;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Class for visualizing a station with a order list.
 * @author Jere Salmensaari
 */
public class StationWithList {
	private Rectangle[] subRectangles = null;
	private Rectangle mainRectangle = null;
	private Text info = null;
	
	/**
	 * Creates a StationWithList with the specified main
	 * rectangle and text.
	 * @param rect Rectangle to be set as mainRectangle.
	 * @param text Text to identify the StationWithList.
	 */
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
	
	/**
	 * Returns the main rectangle
	 * @return mainRectangle Main rectangle of the class.
	 */
	public Rectangle getMainRectangle()
	{
		return mainRectangle;
	}

	/**
	 * Returns the text of the claas
	 * @return info Info text of the class
	 */
	public Text getText()
	{
		return info;
	}
	
	/**
	 * Sets a new rectangle as the mainRectangle 
	 * of the class
	 * @param rect Rectangle to be set as the main rectangle.
	 */
	public void setMainRectangle(Rectangle rect)
	{
		mainRectangle = rect;
	}
	
	/**
	 * Draws a given count of subrectangles.
	 * @param count Amount of subrectangles to draw.
	 */
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
			if ((i+1) % 30 == 0) 
			{
				location = new Point2D(mainRectangle.getX()-5, location.getY()+mainRectangle.getHeight()/2+2);				
			} else if (i != 0)
			{
				location = new Point2D(location.getX()-5, location.getY());
			}
		}
	}
	
	/**
	 * Returns an Array of all current subreactangles.
	 * @return subRectangles Array of current subRectangles.
	 */
	public Rectangle[] getSubRectangles()
	{
		return subRectangles;
	}
	

}
