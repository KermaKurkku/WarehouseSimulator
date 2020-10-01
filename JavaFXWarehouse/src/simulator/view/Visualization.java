package simulator.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Visualization extends Canvas{
	
	private GraphicsContext gc;
	
	public Visualization(int w, int h)
	{
		super(w,h);
		gc = this.getGraphicsContext2D();
		emptyScreen();
	}
	
	public void emptyScreen()
	{
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	public void drawRouter()
	{
		
	}

}
