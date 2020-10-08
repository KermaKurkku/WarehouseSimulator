package warehouse.simulator;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import warehouse.simulator.view.RootLayoutController;
import warehouse.simulator.view.SimulationEditDialogController;
import warehouse.simulator.view.WarehouseSimulationController;
import warehouse.simulator.controller.SimulationController;
import warehouse.simulator.controller.IController;
import warehouse.simulator.model.Order;
import warehouse.simulator.model.Trace;
import warehouse.simulator.model.Trace.Level;
import warehouse.simulator.model.Order.SortType;


public class MainApp extends Application{

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private IController controller;
	private WarehouseSimulationController whSimController;
			
	@Override
	public void start(Stage primaryStage) {
		Trace.setTraceLevel(Level.WAR);
		Order.setSortType(SortType.FIFO);
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Warehouse Simulation");
		this.controller = new SimulationController();

		initRootLayout();
		
		showSimulation();
	}
	
	/**
	 * Initialize the root layout
	 */
	public void initRootLayout()
	{
		try
		{
			// Load the root layout
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			this.rootLayout = (BorderPane) loader.load();
			
			// Set the root layout into the scene and show
			Scene scene = new Scene(this.rootLayout);
			this.primaryStage.setScene(scene);

			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			this.primaryStage.show();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void showSimulation() // Create the warehouse simulation view
	{
		try
		{
			// Load simulation
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/WarehouseSimulation.fxml"));
			AnchorPane simulation = (AnchorPane) loader.load();
			
			// Set simulation in the center of root
			this.rootLayout.setCenter(simulation);
			
			// Give the controller access to MainApp
			this.whSimController = loader.getController();
			this.whSimController.setMainApp(this);
			
			this.controller.setGUI(whSimController);

		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void showSimulationEdit()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SimulationEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit simulation");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(this.primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			SimulationEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			dialogStage.showAndWait();
			updateSettingsText();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage()
	{
		return this.primaryStage;
	}
	
	public IController getController()
	{
		return this.controller;
	}
	
	public static void main(String[] args) 
    {
		Trace.setTraceLevel(Level.INFO);
		Order.setSortType(SortType.SIZE);
        launch(args);
	}
	
	public void updateSettingsText()
	{
		this.whSimController.setSettingsText();
	}
	
}
