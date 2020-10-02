package simulator.view;

import java.awt.geom.Point2D;
import java.util.LinkedList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// Figre out how to draw the visualization
public class Visualization extends Pane{
	private StationWithList[] collectingStations;
	private StationWithList router;
	private Pane pane;
	
	public Visualization(int w, int h, Pane pane)
	{
		//emptyScreen();
		//super.setWidth(w);
		//super.setHeight(h);
		this.pane = pane;
	}
	
//	public void emptyScreen()
//	{
//		gc.setFill(Color.WHITE);
//		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
//	}
	
//	public void drawRouter()
//	{
//		gc.strokeOval(100-10, this.getHeight()/2+10, 20, 20);
//	}
//	
	public void drawCollectingStations(int amount)
	{
		Rectangle rect;
		this.collectingStations = new StationWithList[amount];
		double height = this.getHeight();
		
		for (int i = 0; i < this.collectingStations.length; i++)
		{
			this.collectingStations[i] = new StationWithList(
					new Rectangle(this.getWidth()/2, 
					(25 + (height*((double)i/(this.collectingStations.length-1)))), 
					20, 20));
			this.pane.getChildren().add(this.collectingStations[i].getMainRectangle());
			System.out.println(this.pane.getMaxWidth());
		}
	}
	
	public void drawRect()
	{
		
		
	}

}
