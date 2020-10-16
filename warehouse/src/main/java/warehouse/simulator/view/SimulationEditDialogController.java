package warehouse.simulator.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import warehouse.simulator.model.Order;
import warehouse.simulator.model.Order.SortType;
import warehouse.simulator.util.JSONReader;

import java.io.FileWriter;
import java.io.File;

import java.io.IOException;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Class for creating and controlling the simulation edit dialog
 * @author Jere Salmensaari
 */
public class SimulationEditDialogController {

	@FXML
	private TextField stationCountField;
	
	@FXML
	private ComboBox sortTypeList;
	
	@FXML
	private TextField minimumOrderField;
	
	@FXML
	private TextField medianOrderVarianceField;
	
	@FXML
	private TextField varianceOrderVarianceField;
	
	@FXML
	private TextField minimumRouteVarianceField;
	
	@FXML
	private TextField maximumRouteVarianceField;

	@FXML 
	private TextField medianOrderCollectTimeVariance;

	@FXML
	private TextField varianceOrderCollectTimeVariance;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private Button okBtn;

	@FXML
	private Button reset;

	private Stage dialogStage;

	private Object[] defaults;
	
	@FXML
	/**
	 * Called when the FXML file is loaded
	 * Creates the lists and onClick-events for the 
	 * interface.
	 * Reads the default settings from defaults.json
	 */
	private void initialize()
	{
		ObservableList<Order.SortType> options = FXCollections.observableArrayList(
				SortType.FIFO,
				SortType.SIZE,
				SortType.TIME
				);
		this.sortTypeList.getItems().addAll(options);
		readDefaults();
		// Clear input fields on click

		this.stationCountField.setOnMouseClicked((event) -> {
			stationCountField.setText("");
		});

		this.minimumOrderField.setOnMouseClicked((event) -> {
			minimumOrderField.setText("");
		});

		this.stationCountField.setOnMouseClicked((event) -> {
			stationCountField.setText("");
		});

		this.minimumRouteVarianceField.setOnMouseClicked((event) -> {
			minimumRouteVarianceField.setText("");
		});

		this.maximumRouteVarianceField.setOnMouseClicked((event) -> {
			maximumRouteVarianceField.setText("");
		});

		this.medianOrderCollectTimeVariance.setOnMouseClicked((event) -> {
			medianOrderCollectTimeVariance.setText("");
		});

		this.varianceOrderCollectTimeVariance.setOnMouseClicked((event) -> {
			varianceOrderCollectTimeVariance.setText("");
		});

		this.medianOrderVarianceField.setOnMouseClicked((event) -> {
			medianOrderVarianceField.setText("");
		});

		this.varianceOrderVarianceField.setOnMouseClicked((event) -> {
			varianceOrderVarianceField.setText("");
		});

	}

	/**
	 * Sets the stage of the class.
	 * @param stage Stage to be set.
	 */
	public void setDialogStage(Stage stage)
	{
		this.dialogStage = stage;
	}

	@FXML
	@SuppressWarnings("unchecked")
	/**
	 * Handles the activation of the OK-button.
	 * <p>
	 * Saves the given settings as settings.json in
	 * the resources folder of the appplication.
	 * <p>
	 * Shows an alert window if some of the input fields
	 * don't have the correct type of input.
	 */
	private void handleOK()
	{
		if (!isInputValid())
			return;

		JSONObject obj = new JSONObject();
		obj.put("stationCount", this.stationCountField.getText());

		String[] collectors = setCollectorCounts(Integer.parseInt((String)this.stationCountField.getText()));

		JSONArray list = new JSONArray();
		for (String s : collectors)
		{
			list.add(s);
		}

		obj.put("collectors", list);
		obj.put("sortType", this.sortTypeList.getSelectionModel().getSelectedItem().toString());
		obj.put("minimumOrders", this.minimumOrderField.getText());
		obj.put("minRouteVariance", this.minimumRouteVarianceField.getText());
		obj.put("maxRouteVariance", this.maximumRouteVarianceField.getText());
		obj.put("medianOrderCollectVariance", this.medianOrderCollectTimeVariance.getText());
		obj.put("varianceOrderCollectVariance", this.varianceOrderCollectTimeVariance.getText());
		obj.put("medianOrderVariance", this.medianOrderVarianceField.getText());
		obj.put("varianceOrderVariance", this.varianceOrderVarianceField.getText());

		String cwdr = System.getProperty("user.dir");
		try(FileWriter file = new FileWriter(cwdr+"/src/main/resources/options/settings.json"))
		{
			file.write(obj.toJSONString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println(obj);
		dialogStage.close();
	}

	@FXML 
	/**
	 * Handles the activation of the cancel-button.
	 * Closes the window.
	 */
	private void handleCancel()
	{
		dialogStage.close();
	}

	@SuppressWarnings("unchecked")
	@FXML
	/**
	 * Handles the activation of the default button.
	 * <p>
	 * Sets the settings to those of default.json.
	 */
	private void handleDefault()
	{
		JSONObject obj = new JSONObject();
		obj.put("stationCount", defaults[0]);
		obj.put("collectors", defaults[1]);
		obj.put("sortType", defaults[2]);
		obj.put("minimumOrders", defaults[3]);
		obj.put("minRouteVariance", defaults[4]);
		obj.put("maxRouteVariance", defaults[5]);
		obj.put("medianOrderCollectVariance", defaults[6]);
		obj.put("varianceOrderCollectVariance", defaults[7]);
		obj.put("medianOrderVariance", defaults[8]);
		obj.put("varianceOrderVariance", defaults[9]);

		String cwdr = System.getProperty("user.dir");
		try(FileWriter file = new FileWriter(cwdr+"/src/main/resources/options/settings.json"))
		{
			file.write(obj.toJSONString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		dialogStage.close();
	}

	/**
	 * Reads the defaults.json file.
	 */
	private void readDefaults()
	{
		defaults = new Object[10];
		File f = new File(System.getProperty("user.dir")+"/src/main/resources/options/default.json");
		JSONObject jsonObject = JSONReader.readJSON(f);
		defaults[0] = (String)jsonObject.get("stationCount");
		this.stationCountField.setText("Default: "+(String)defaults[0]);
		defaults[1] = (JSONArray) jsonObject.get("collectors");
		defaults[2] = (String)jsonObject.get("sortType");
		defaults[3] = (String)jsonObject.get("minimumOrders");
		this.minimumOrderField.setText("Default: "+(String)defaults[3]);
		defaults[4] = (String)jsonObject.get("minRouteVariance");
		this.minimumRouteVarianceField.setText("Default: "+(String)defaults[4]);
		defaults[5] = (String)jsonObject.get("maxRouteVariance");
		this.maximumRouteVarianceField.setText("Default: "+(String)defaults[5]);
		defaults[6] = (String)jsonObject.get("medianOrderCollectVariance");
		this.medianOrderCollectTimeVariance.setText("Default: "+ (String)defaults[6]);
		defaults[7] = (String)jsonObject.get("varianceOrderCollectVariance");
		this.varianceOrderCollectTimeVariance.setText("Default: "+defaults[7]);
		defaults[8] = (String)jsonObject.get("medianOrderVariance");
		this.medianOrderVarianceField.setText("Default: "+(String)defaults[8]);
		defaults[9] = (String)jsonObject.get("varianceOrderVariance");
		this.varianceOrderVarianceField.setText("Default: "+(String)defaults[9]);
	
	}

	/**
	 * Checks all input fields have valid input.
	 * @return True if input is valid, otherwise false.
	 */
	private boolean isInputValid()
	{
		if (!validateInput(this.stationCountField.getText()))
			return false;
		if (!validateInput(this.minimumOrderField.getText()))
			return false;
		if (!validateInput(this.medianOrderVarianceField.getText()))
			return false;
		if (!validateInput(this.varianceOrderVarianceField.getText()))
			return false;
		if (!validateInput(this.minimumRouteVarianceField.getText()))
			return false;
		if (!validateInput(this.maximumRouteVarianceField.getText()))
			return false;
		if (!validateInput(this.medianOrderCollectTimeVariance.getText()))
			return false;
		if (!validateInput(this.varianceOrderCollectTimeVariance.getText()))
			return false;
		if (this.sortTypeList.getSelectionModel().getSelectedItem() == null)
			return false;
		return true;
	}

	/**
	 * Checks if the given string is valid input for
	 * the purposes of this program
	 * @param input String to be checkd
	 * @return True if input is valid, otherwise false.
	 * <p>
	 * If input is not valid, shows an Alert window.
	 */
	private boolean validateInput(String input)
	{
		String errorString = "";

		if (input == null || input.length() == 0)
		{
			errorString += "All fields must have input";
		} else
		{
			try 
			{
				Double.parseDouble(this.stationCountField.getText());
			} catch (NumberFormatException e)
			{
				errorString += "All fields must be numbers!\n";
			}
		}
		if (errorString.length() == 0)
			return true;
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(dialogStage);
		alert.setTitle("Invalid fields");
		alert.setHeaderText("Fix invalid fields");
		alert.setContentText(errorString);
		alert.showAndWait();
		return false;

	}
	
	/**
	 * Opens popups to set the amount of collectors
	 * in each given collecting station.
	 * @param count Amount of collecting stations in
	 * the program.
	 * @return collectors Array that has the amount of
	 * collectors in the stations matching the index.
	 */
	private String[] setCollectorCounts(int count)
	{
		String[] collectors = new String[count];
		int defaultCount = 2;
		for (int i = 0; i < count;)
		{
			String errMessage = "";
			TextInputDialog dialog = new TextInputDialog();
			dialog.initOwner(dialogStage);
			dialog.setTitle("Collector counts");
			dialog.setHeaderText("Please input the amount of collectors for: station"+i
				+"\nDefault: "+defaultCount);
			Optional<String> optional;
			optional = dialog.showAndWait();

			if (!optional.isPresent())
			{
				return null;
			}

			if (optional.get().equals(""))
			{
				errMessage += "Input cannot be empty\n";
			}
			try
			{
				Integer.parseInt(optional.get());
			} catch (NumberFormatException e)
			{	
				errMessage += "Input must be number!";
			}
			if (errMessage.length() != 0)
			{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Input error");
				alert.setHeaderText(errMessage);
				alert.showAndWait();
				continue;
			}
			collectors[i] = optional.get();
			defaultCount++;
			i++;
		}
		return collectors;
	}


}
