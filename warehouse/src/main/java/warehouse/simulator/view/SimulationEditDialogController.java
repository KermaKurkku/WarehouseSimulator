package warehouse.simulator.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import warehouse.simulator.model.Order;
import warehouse.simulator.model.Order.SortType;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

// TODO javadoc
public class SimulationEditDialogController {

	@FXML
	private TextField stationCountField;
	
	@FXML
	private ComboBox sortTypeList;
	
	@FXML
	private TextField minimumOrderField;
	
	@FXML
	private TextField minimumOrderVarianceField;
	
	@FXML
	private TextField maximumOrderVarianceField;
	
	@FXML
	private TextField minimumRouteVarianceField;
	
	@FXML
	private TextField maximumRouteVarianceField;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private Button okBtn;
	
	public SimulationEditDialogController()
	{
		
	}
	
	@FXML
	private void initialize()
	{
		ObservableList<Order.SortType> options = FXCollections.observableArrayList(
				SortType.FIFO,
				SortType.SIZE,
				SortType.TIME
				);
		this.sortTypeList.getItems().addAll(options);
	}

	@FXML
	private void handleOK()
	{
		JSONObject obj = new JSONObject();
		obj.put("stationCount", this.stationCountField.getText());
		obj.put("minimumOrders", this.minimumOrderField.getText());
		obj.put("minOrderVariance", this.minimumOrderVarianceField.getText());
		obj.put("maxOrderVariance", this.maximumOrderVarianceField.getText());
		obj.put("minRouteVariance", this.minimumRouteVarianceField.getText());
		obj.put("maxRouteVariance", this.maximumRouteVarianceField.getText());

		String cwdr = System.getProperty("user.dir");
		try(FileWriter file = new FileWriter(cwdr+"/settings.json"))
		{
			file.write(obj.toJSONString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println(obj);
	}
	
}
