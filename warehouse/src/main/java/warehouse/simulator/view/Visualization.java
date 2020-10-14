package warehouse.simulator.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import warehouse.simulator.view.animation.StationWithList;
import warehouse.simulator.view.animation.CollectingStationVisual;

/**
 * Class for showing the visualization of the warehouse.
 * @author Jere Salmensaari
 */
public class Visualization extends Pane{
	private CollectingStationVisual[] collectingStations;
	private StationWithList router;
	private ImageView imgView;

	/**
	 * Constructor to set the size of the
	 * visualization and set the base image.
	 * @param w Width of the visualization.
	 * @param h Height of the visualization.
	 */
	public Visualization(int w, int h)
	{
		super.setWidth(w);
		super.setHeight(h);
		
		// Fix later
		Image img = null;
		try 
		{
			img = new Image(new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/images/Warehouse.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.imgView = new ImageView(img);
		this.imgView.setX(this.getLayoutX());
		this.getChildren().add(imgView);
		
		
		
		
	}

	/**
	 * Removes all the objects from the visualization.
	 */
	public void clear()
	{
		this.getChildren().removeAll(this.getChildren());
	}
	
	/**
	 * Adds a router to the visualization.
	 */
	public void drawRouter()
	{
		this.router = new StationWithList(new Rectangle(200, this.getHeight()/2-30, 20, 20), "Router");
		this.getChildren().add(this.imgView);
		this.getChildren().addAll(this.router.getMainRectangle(), this.router.getText());
		
		
	}
	
	/**
	 * Adds a specified amount of collectingStations
	 * to the visualization.
	 * @param stationCount Amount of Collecting Statons to draw.
	 * @param collectorCount Amount of collectors on the stations.
	 */
	public void drawCollectingStations(int stationCount, int[] collectorCount)
	{
		this.collectingStations = new CollectingStationVisual[stationCount];
		double height = this.getHeight() - 50;
		
		for (int i = 0; i < this.collectingStations.length; i++)
		{
			this.collectingStations[i] = new CollectingStationVisual(
					new Rectangle(this.getWidth()/2, 
					((height*((double)i/(this.collectingStations.length))+ 50)), 
					40, 15), collectorCount[i]);
			this.collectingStations[i].drawCollectors();
			this.getChildren().add(this.collectingStations[i].getMainRectangle());
			this.getChildren().addAll(this.collectingStations[i].getCollectors());
			this.getChildren().add(this.collectingStations[i].getText());
		}
		System.out.println(this.getChildren());
	}
	
	/**
	 * Draw orders on the collecting stations.
	 * @param count Amount of orders.
	 */
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
	
	/**
	 * Draw orders on the router.
	 * @param count Amount of orders.
	 */
	public void drawRouterOrders(int count)
	{
		if (this.router.getSubRectangles() != null)
		{
			removeRectangles(this.router.getSubRectangles());
		}
		this.router.drawSubRectangles(count);
		addRectangles(this.router.getSubRectangles());
	}

	/**
	 * Sets the amount of collectors to be set collecting.
	 * @param list List of the amount of collecting collectors
	 * for each station.
	 */
	public void collectingCollectors(List<int[]> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			this.collectingStations[i].setCollecting(list.get(i));
		}
	}
	
	/**
	 * Removes given rectangles from the visualization.
	 * @param remove List of rectangles to be removed.
	 */
	private void removeRectangles(Rectangle[] remove)
	{
		for (Rectangle rect : remove)
		{
			this.getChildren().remove(rect);
		}
	}
	
	/**
	 * Adds specified rectangles to the visualization.
	 * @param add List of rectangles to be added to the visualization.
	 */
	private void addRectangles(Rectangle[] add)
	{
		this.getChildren().addAll(add);
	}

}
