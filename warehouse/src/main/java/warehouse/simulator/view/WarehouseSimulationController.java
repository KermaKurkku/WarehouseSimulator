package warehouse.simulator.view;

import java.io.File;
import java.text.DecimalFormat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import warehouse.simulator.MainApp;
import warehouse.simulator.util.JSONReader;

/**
 * Class for handling the simulation view of the 
 * simulator. Controls the visualization and inputs 
 * of the simulator. 
 * @author Jere Salmensaari
 */
public class WarehouseSimulationController implements ISimulationGui {
	
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
	private TextField delayCurrentField;
	
	@FXML
	private TextField nextLeaveTime;
	
	@FXML
	private AnchorPane splitAnchor;

	@FXML
	private Text stationCountText;
	@FXML
	private Text collectorCountText;
	@FXML
	private Text sortTypeText;
	@FXML
	private Text minOrderText;
	@FXML
	private Text routingVarianceText;
	@FXML
	private Text collectTimeVarianceText;
	@FXML
	private Text orderGenVariance;

	@FXML
	private GridPane viewGrid;
	
	private Visualization visual;	
	
	private MainApp mainApp;
	
	// Called after loading of fxml file
	@FXML
	/**
	 * Initializes the visualization of the controller
	 * and puts it into the gridpane.
	 */
	private void initialize()
	{
		
		this.visual = new Visualization(800, 500);
		AnchorPane.setLeftAnchor(this.visual, (double)25);
		AnchorPane.setRightAnchor(this.visual, (double)25);
		AnchorPane.setTopAnchor(this.visual, (double)100);
		AnchorPane.setBottomAnchor(this.visual, (double)5);
		splitAnchor.getChildren().add(this.visual);	

		GridPane.setMargin(this.visual, new Insets(16, 0, 22, 22));

		viewGrid.add(this.visual, 0, 1, 2, 5);
		
		setSettingsText();
	}
	
	/**
     * Sets the reference to the application's MainApp.
     * @param mainApp MainApp reference.
     */
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
	}
	
	@Override
	/**
	 * Return the visualization of the class.
	 * @return visual Visualization of the class.
	 */
	public Visualization getVisual()
	{
		return this.visual;
	}

	@Override
	/**
	 * Returns the simulation time set for the motor in the GUI.
	 * @return simulationTime Simulation time for the motor.
	 */
	public double getTime() {
		return Double.parseDouble(this.simTimeField.getText());
	}

	@Override
	/**
	 * Returns the delay set for the motor in the GUI.
	 * @return delay Delay time for the motor.
	 */
	public long getDelay() {
		long delay = (long)(Double.parseDouble(this.speedTextField.getText()));
		return delay;
	}
	
	@Override
	/**
	 * Sets the delay text in the GUI.
	 * @param delay delay time in the motor.
	 */
	public void setDelayText(double speed)
	{
		this.speedTextField.setText(speed+"");
	}

	@Override
	/**
	 * Sets current time in the GUI.
	 * @param time Current time in the simulation.
	 */
	public void setSimTime(double time) {
		DecimalFormat formatter = new DecimalFormat("#0.00");
		this.timeField.setText(formatter.format(time));
	}

	@Override
	/**
	 * Sets next leave time field in the GUI.
	 * @param nextTime Next leave time from the motor.
	 */
	public void setNextLeaveTime(int leave)
	{
		this.nextLeaveTime.setText(leave+"");
	}
	
	@FXML
	/**
	 * Handles the activation of the simulate-button.
	 * Starts the simulation.
	 */
	public void handleSimulation()
	{
		if (isValidInput())
			// This is dumb, fix maybe?
			this.mainApp.getController().startSimulation();
		
	}

	@FXML
	/**
	 * Handles the activation of the stop-button.
	 * Calls the motor to stop the simulation
	 */
	public void handleStop()
	{
		this.mainApp.getController().stopSimulation();
	}
	
	@FXML
	/**
	 * Handles the activation of the faster-button.
	 * Lessens the delay of the motor.
	 */
	public void handleFaster()
	{
		System.out.println("Faster");
		this.mainApp.getController().faster();
		setDelayText(this.mainApp.getController().getDelay());
	}
	
	@FXML
	/**
	 * Handles the activation of the slower-button.
	 * Lenghtens the delay of the motor.
	 */
	public void handleSlower()
	{
		System.out.println("Slower");
		this.mainApp.getController().slower();
		setDelayText(this.mainApp.getController().getDelay());
	}

	/**
	 * Sets the text in the settings preview window.
	 * <p>
	 * Reads the settings.json file and sets the information 
	 * according to that. If settings.json does not exist,
	 * loads default.json.
	 */
	public void setSettingsText()
	{
		File f = new File(System.getProperty("user.dir")+"/src/main/resources/options/settings.json");
		if (!f.exists())
			f = new File(System.getProperty("user.dir")+"/src/main/resources/options/default.json");
		
		JSONObject obj = JSONReader.readJSON(f);
		this.stationCountText.setText((String)obj.get("stationCount"));
		JSONArray arr = (JSONArray)obj.get("collectors");
		String collString = "";
		for (int i = 0; i < arr.size(); i++)
		{
			if (i != arr.size()-1)
			{
				collString +=  (String)arr.toArray()[i]+", ";
			} else
			{
				collString += (String)arr.toArray()[i];
			}
		}
		this.collectorCountText.setText(collString);
		this.sortTypeText.setText((String)obj.get("sortType"));
		this.minOrderText.setText((String)obj.get("minimumOrders"));
		this.routingVarianceText.setText("min: "+(String)obj.get("minRouteVariance")
			+", max: "+(String)obj.get("maxRouteVariance"));
		this.collectTimeVarianceText.setText("median: "+(String)obj.get("medianOrderCollectVariance")
			+", var: "+(String)obj.get("varianceOrderCollectVariance"));
		this.orderGenVariance.setText("median: "+(String)obj.get("medianOrderVariance")
			+", var: "+(String)obj.get("varianceOrderVariance"));
	}
	
	/**
	 * Checks if the input in the fields of the 
	 * simulator are valid
	 * @return True if input is valid, otherwise false.
	 */
	private boolean isValidInput()
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
				Double.parseDouble(this.simTimeField.getText());
			} catch (NumberFormatException e)
			{
				alrt += "Simulation time field input is not valid(Must be number)\n";
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
				Double.parseDouble(this.speedTextField.getText());
			} catch (NumberFormatException e)
			{
				alrt += "Speed field input is not valid(Must be number)\n";
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
