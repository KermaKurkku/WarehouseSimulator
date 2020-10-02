package simulator;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import simulator.view.WarehouseSimulationController;
import simulator.controller.SimulationController;
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
	
	private LinkedList<Rectangle> list;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Warehouse Simulation");
		this.controller = new SimulationController();
		
		this.list = new LinkedList<>();
		
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
			WarehouseSimulationController warehouseGUIController = loader.getController();
			warehouseGUIController.setMainApp(this);
			
			// Dis Be shit
			Button testin = new Button();
			testin.setText("Add");
			testin.setOnAction((event) -> {
				if (list.isEmpty())
				{
					list.add(new Rectangle(200, 200, 5, 20));
				} else
				{
					list.add(new Rectangle(list.getLast().getX()-10, list.getLast().getY(),
							5, 20));
					System.out.println(list.size());
				}
				list.getLast().setFill(Color.ORANGE);
			});
			
			Button testin2 = new Button();
			testin2.setText("Yeet");
			testin2.setOnAction((event) -> {
				if (this.list.isEmpty())
				{
					return;
				}
				warehouseGUIController.getPane().getChildren().remove(this.list.getLast());
				this.list.removeLast();
				
			});
			BorderPane.setAlignment(testin, Pos.CENTER_LEFT);
			this.rootLayout.setBottom(testin);
			BorderPane.setAlignment(testin2, Pos.CENTER);
			this.rootLayout.setLeft(testin2);
			
			this.controller.setGUI(warehouseGUIController);
			
			
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
