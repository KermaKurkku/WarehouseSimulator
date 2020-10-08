package warehouse.simulator.view;

import java.io.File;
import java.text.DecimalFormat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import warehouse.simulator.MainApp;
import warehouse.simulator.util.JSONReader;
import warehouse.simulator.util.NumberFormatter;

// TODO javadoc
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
	
	@FXML
	private Pane animationPane;

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
	
	private Visualization visual;	
	
	private MainApp mainApp;
	
	public WarehouseSimulationController()
	{
		
	}
	
	// Called after loading of fxml file
	@FXML
	private void initialize()
	{
		
		this.visual = new Visualization(767, 500);
		AnchorPane.setLeftAnchor(this.visual, (double)25);
		AnchorPane.setRightAnchor(this.visual, (double)25);
		AnchorPane.setTopAnchor(this.visual, (double)100);
		AnchorPane.setBottomAnchor(this.visual, (double)5);
		splitAnchor.getChildren().add(this.visual);	
		
		setSettingsText();
	}
	
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
	}
	
	public Pane getPane()
	{
		return this.animationPane;
	}
	
	@Override
	public Visualization getVisual()
	{
		return this.visual;
	}

	@Override
	public double getTime() {
		return Double.parseDouble(this.simTimeField.getText());
	}

	@Override
	public long getDelay() {
		long delay = (long)(Double.parseDouble(this.speedTextField.getText()) *100);
		return delay;
	}
	
	@Override
	public void setDelayText(double speed)
	{
		DecimalFormat formatter = new DecimalFormat("#0,00");
		this.speedTextField.setText(formatter.format(speed));
	}

	@Override
	public void setSimTime(double time) {
		DecimalFormat formatter = new DecimalFormat("#0.00");
		this.timeField.setText(formatter.format(time));
	}
	
	@FXML
	public void handleSimulation()
	{
		if (isValidInput())
			// This is dumb, fix maybe?
			this.mainApp.getController().startSimulation();
		
	}

	@FXML
	public void handleStop()
	{
		this.mainApp.getController().stopSimulation();
	}
	
	@FXML
	public void handleFaster()
	{
		System.out.println("Faster");
		this.mainApp.getController().faster();
	}
	
	@FXML
	public void handleSlower()
	{
		System.out.println("Slower");
		this.mainApp.getController().slower();
	}

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
