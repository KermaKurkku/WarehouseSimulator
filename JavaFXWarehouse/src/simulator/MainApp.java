package simulator;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import simulator.view.WarehouseSimulationController;
import simulator.controller.Controller;
import simulator.controller.IController;
import simulator.model.Order;
import simulator.model.Trace;
import simulator.model.Trace.Level;
import simulator.model.Order.SortType;
import simulator.view.IGui;
import simulator.view.Visualization;

public class MainApp extends Application{

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private IController controller;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Warehouse Simulation");
		
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
			this.primaryStage.show();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void showSimulation()
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
			WarehouseSimulationController controller = loader.getController();
			controller.setMainApp(this);
			Visualization visual = new Visualization(550, 450);
			controller.setVisualization(visual);
			
			this.controller = new Controller(controller);
			
		} catch(IOException e)
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
	
}
