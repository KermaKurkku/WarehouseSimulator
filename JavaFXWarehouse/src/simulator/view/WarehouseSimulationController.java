package simulator.view;

import java.text.DecimalFormat;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import simulator.MainApp;
import simulator.util.NumberFormatter;

public class WarehouseSimulationController implements IGui {
	
	@FXML
	private TextField simTimeField;
	
	@FXML
	private TextField speedTextField;
	
	@FXML
	private Button fasterBtn;
	
	@FXML
	private Button slowerBtn;
	
	@FXML
	private Button simulateBtn;
	
	@FXML
	private TextField timeField;
	
	@FXML
	private AnchorPane splitAnchor;
	
	private Visualization visual;
	
	
	private MainApp mainApp;
	
	public WarehouseSimulationController()
	{
		
	}
	
	/**
	 * Called automatically when the fxml is loaded
	 */
	@FXML
	private void initialize()
	{
		
	}
	
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
	}
	
	public void setVisualization(Visualization visual)
	{
		this.visual = visual;
		AnchorPane.setLeftAnchor(this.visual, (double)25);
		AnchorPane.setRightAnchor(this.visual, (double)25);
		AnchorPane.setTopAnchor(this.visual, (double)100);
		AnchorPane.setBottomAnchor(this.visual, (double)5);
		
		splitAnchor.getChildren().add(this.visual);		
	}
	
	@Override
	public Visualization getVisual()
	{
		return this.visual;
	}

	@Override
	public double getTime() {
		return Integer.parseInt(this.simTimeField.getText());
	}

	@Override
	public long getDelay() {
		return Integer.parseInt(this.speedTextField.getText());
	}

	@Override
	public void setSimTime(double time) {
		DecimalFormat formatter = new DecimalFormat("#0.00");
		this.timeField.setText(formatter.format(time));
	}
	
	@FXML
	public void handleSimulation()
	{
		if (validInput())
			// This is dumb, fix maybe?
			this.mainApp.getController().startSimulation();
	}
	
	private boolean validInput()
	{
		String alrt = "";
		if (this.simTimeField.getText() == null || this.simTimeField.getText().length() == 0)
		{
			alrt += "Simulation time field input is not valid\n";
		} else
		{
			// Try to parse into string
			try
			{
				Integer.parseInt(this.simTimeField.getText());
			} catch (NumberFormatException e)
			{
				alrt += "Simulation time field input is not valid(Must be Integer)\n";
			}
		}
		if (this.speedTextField.getText() == null || this.speedTextField.getText().length() == 0)
		{
			alrt += "Speed field input is not valid\n";
		} else
		{
			// Try to parse into string
			try
			{
				Integer.parseInt(this.speedTextField.getText());
			} catch (NumberFormatException e)
			{
				alrt += "Speed field input is not valid(Must be Integer)\n";
			}
		}
		
		if (alrt.length() == 0)
		{
			return true;
		} else
		{
			// Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(this.mainApp.getPrimaryStage());
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(alrt);
            
            alert.showAndWait();
            
            return false;
		}
		
	}
	
}
