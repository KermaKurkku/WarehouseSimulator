package warehouse.simulator.view.animation;

import javafx.geometry.Point2D;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

public class CollectingStationVisual extends StationWithList{
    private CollectorVisual[] collectors;

    public CollectingStationVisual(Rectangle rect, int collectors)
    {
        super(rect, "Collecting station");
        this.collectors = new CollectorVisual[collectors];
    }

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

    public Ellipse[] getCollectors()
    {
        return this.collectors;
    }

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
