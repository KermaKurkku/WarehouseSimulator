package warehouse.simulator.view.animation;

import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;

/**
 * Class for visualizing collectors.
 * @author Jere Salmensaari
 */
public class CollectorVisual extends Ellipse{
    
    /**
     * Constructor for creating a collector visual.
     * @param x X-cordinate of the collector.
     * @param y Y-cordinate of the collector.
     */
    public CollectorVisual(double x, double y)
    {
        super(x, y, 3, 5);
        super.setFill(Color.GREEN);
    }

    /**
     * Sets the color of the collector to red.
     */
    public void startCollecting()
    {
        super.setFill(Color.RED);
    }

    /**
     * Sets the color of the collector to green.
     */
    public void stopCollecting()
    {
        super.setFill(Color.GREEN);
    }

}
