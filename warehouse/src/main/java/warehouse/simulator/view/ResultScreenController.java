package warehouse.simulator.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import warehouse.simulator.MainApp;
import warehouse.simulator.model.OrderResults;
import warehouse.simulator.util.NumberFormatter;

/**
 * Class for handling the loading and controlling of the results screen
 * of the application.
 * @author Jere Salmensaari
 */
public class ResultScreenController {
	
	private ListView<String> list;
	
	@FXML
	private Text collOrdrs;
	
	@FXML
	private Text unComplOrdrs;
	
	@FXML
	private Text avrgOrdrTime;
	
	@FXML
	private Text lateOrdrs;
	
	@FXML
	private Button exitBtn;
	@FXML
	private VBox box;
	private Stage dialogStage;
	
	private MainApp mainApp;
	
	/**
	 * Fetches all the names in the database and creates a List of
	 * them to put inside the ListView of the window.
	 */
	public void loadData()
	{
		list = new ListView<>();
		String[] data = this.mainApp.getController().getDateData();
		for (String s: data)
		{
			list.getItems().add(s);
		}
		list.setOnMouseClicked((event) -> {
			String selected = (String)list.getSelectionModel().getSelectedItem();
			displayData(selected);
		});
		box.getChildren().addAll(list);
	}
	
	/**
	 * Sets the stage of the class.
	 * @param stage Stage to be set.
	 */
	public void setDialogStage(Stage stage)
	{
		this.dialogStage = stage;
	}

	/**
	 * Gives the class a reference to the MainApp-class 
	 * of the program.
	 * @param mainApp MainApp class.
	 */
	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
		loadData();
	}
	
	/**
	 * Inhabits the detailed result fields of the window
	 * depending on which element of the ListView is selected.
	 * @param clicked Item selected from the ListView.
	 */
	private void displayData(String clicked)
	{
		OrderResults results = this.mainApp.getController().getResults(clicked);
		this.collOrdrs.setText(results.getCompleted()+"");
		this.unComplOrdrs.setText(results.getUncompleted()+"");
		this.avrgOrdrTime.setText(NumberFormatter.format(results.getAverage())+"");
		this.lateOrdrs.setText(results.getLate()+"");
	}
	
	@FXML
	/**
	 * Handles activaton of the exit button.
	 * Closes the window.
	 */
	private void handleExit()
	{
		dialogStage.close();
	}
	
}
