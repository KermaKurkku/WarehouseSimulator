package warehouse.simulator.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import warehouse.simulator.view.animation.StationWithList;
import warehouse.simulator.view.animation.CollectingStationVisual;

// Figre out how to draw the visualization
public class Visualization extends Pane{
	private CollectingStationVisual[] collectingStations;
	private StationWithList router;
	private Pane pane;
	
	public Visualization(int w, int h)
	{
		super.setWidth(w);
		super.setHeight(h);
		
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

	public void clear()
	{
		this.getChildren().removeAll(this.getChildren());
	}
	
	public void drawRouter()
	{
		this.router = new StationWithList(new Rectangle(200, this.getHeight()/2-30, 20, 20), "Router");
		this.getChildren().addAll(this.router.getMainRectangle(), this.router.getText());
		
	}
	
	public void drawCollectingStations(int stationCount, int[] collectorCount)
	{
		Rectangle rect;
		this.collectingStations = new CollectingStationVisual[stationCount];
		double height = this.getHeight() - 100;
		
		for (int i = 0; i < this.collectingStations.length; i++)
		{
			this.collectingStations[i] = new CollectingStationVisual(
					new Rectangle(this.getWidth()/2, 
					(25 + (height*((double)i/(this.collectingStations.length-1)))), 
					40, 15), collectorCount[i]);
			this.collectingStations[i].drawCollectors();
			this.getChildren().add(this.collectingStations[i].getMainRectangle());
			this.getChildren().addAll(this.collectingStations[i].getCollectors());
			this.getChildren().add(this.collectingStations[i].getText());
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

	public void collectingCollectors(List<int[]> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			this.collectingStations[i].setCollecting(list.get(i));
		}
	}
	
	private void removeRectangles(Rectangle[] remove)
	{
		for (Rectangle rect : remove)
		{
			this.getChildren().remove(rect);
		}
	}
	
	private void addRectangles(Rectangle[] add)
	{
		this.getChildren().addAll(add);
	}

}
