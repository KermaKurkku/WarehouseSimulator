package warehouse.simulator.view.animation;

import javafx.geometry.Point2D;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 * Class for creating a visualizaton for the 
 * collecting stations. Includes a StationWithList
 * and a given amount of CollectorVisuals.
 * @author Jere Salmensaari
 */
public class CollectingStationVisual extends StationWithList{
    private CollectorVisual[] collectors;

    /**
     * Constructor for CollectingStationVisual. 
     * @param rect Rectangle to set as the mainrectangle.
     * @param collectors Amount of collectors to draw.
     */
    public CollectingStationVisual(Rectangle rect, int collectors)
    {
        super(rect, "Collecting station");
        this.collectors = new CollectorVisual[collectors];
    }

    /**
     * Creates the collectors to the collector list 
     * in the class.
     */
    public void drawCollectors()
    {
        Point2D location = new Point2D(super.getMainRectangle().getX()+1.5, 
        super.getMainRectangle().getY()+(super.getMainRectangle().getHeight()+15));
        for (int i = 0; i < this.collectors.length; i++)
        {
            CollectorVisual col = new CollectorVisual(location.getX(), location.getY());
            this.collectors[i] = col;
            location = new Point2D(location.getX() + 10, location.getY());
        }
    }

    /**
     * Returns an Array of the collectors in the class
     * @return collectors Array of Collectors.
     */
    public Ellipse[] getCollectors()
    {
        return this.collectors;
    }

    /**
     * Sets a given number of collectors
     * to be collecting.
     * @param collecting Array of which collectors are collecting
     */
    public void setCollecting(int[] collecting)
    {
        for (int i = 0; i < collecting.length; i++)
        {
            if (collecting[i] == 1)
            {
                this.collectors[i].startCollecting();
            }
            else
            {    
                this.collectors[i].stopCollecting();
            }
        }
    }
}
