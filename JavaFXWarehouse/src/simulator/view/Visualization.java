package simulator.view;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import simulator.view.animation.StationWithList;

// Figre out how to draw the visualization
public class Visualization extends Pane{
	private StationWithList[] collectingStations;
	private StationWithList router;
	private Pane pane;
	
	public Visualization(int w, int h, Pane pane)
	{
		//emptyScreen();
		super.setWidth(w);
		super.setHeight(h);
		this.pane = pane;
		
		// Fix later
//		Image img = null;
//		try 
//		{
//			img = new Image(new FileInputStream("src/images/Warehouse.png"));
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		ImageView imgView = new ImageView(img);
//		imgView.setRotate(90);
//		this.pane.getChildren().add(imgView);
		
		
		
		
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
	
	public void drawRouter()
	{
		this.router = new StationWithList(new Rectangle(150, this.pane.getHeight()/2-10, 20, 20));
		this.pane.getChildren().add(this.router.getMainRectangle());
		
	}
	
	public void drawCollectingStations(int amount)
	{
		Rectangle rect;
		this.collectingStations = new StationWithList[amount];
		double height = this.getHeight() - 50;
		
		for (int i = 0; i < this.collectingStations.length; i++)
		{
			this.collectingStations[i] = new StationWithList(
					new Rectangle(this.getWidth()/2, 
					(25 + (height*((double)i/(this.collectingStations.length-1)))), 
					40, 15));
			this.pane.getChildren().add(this.collectingStations[i].getMainRectangle());
		}
	}
	
	public void drawOrders(int[] count)
	{
		
		for (int i = 0; i < count.length; i++)
		{
			if (this.collectingStations[i].getSubRectangles() != null)
			{
				removeRectangles(this.collectingStations[i].getSubRectangles());
			}
			this.collectingStations[i].drawSubRectangles(count[i]);
			addRectangles(this.collectingStations[i].getSubRectangles());
		}
		
	}
	
	public void drawRouterOrders(int count)
	{
		if (this.router.getSubRectangles() != null)
		{
			removeRectangles(this.router.getSubRectangles());
		}
		this.router.drawSubRectangles(count);
		addRectangles(this.router.getSubRectangles());
	}
	
	private void removeRectangles(Rectangle[] remove)
	{
		for (Rectangle rect : remove)
		{
			this.pane.getChildren().remove(rect);
		}
	}
	
	private void addRectangles(Rectangle[] add)
	{
		this.pane.getChildren().addAll(add);
	}

}
