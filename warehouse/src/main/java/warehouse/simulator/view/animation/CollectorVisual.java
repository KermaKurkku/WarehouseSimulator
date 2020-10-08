package warehouse.simulator.view.animation;

import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;

public class CollectorVisual extends Ellipse{
    
    public CollectorVisual(double x, double y)
    {
        super(x, y, 3, 5);
        super.setFill(Color.GREEN);
    }

    public void startCollecting()
    {
        super.setFill(Color.RED);
    }

    public void stopCollecting()
    {
        super.setFill(Color.GREEN);
    }

}
