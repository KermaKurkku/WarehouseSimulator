package warehouse.simulator.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;


import org.json.simple.JSONObject;

import warehouse.simulator.MainApp;
import warehouse.simulator.util.JSONReader;

/**
 * Controller for the root layout of the application.
 * @author Jere Salmensaari
 */
public class RootLayoutController {
    private MainApp mainApp;
    
    /**
     * Sets the reference to the application's MainApp.
     * @param mainApp MainApp reference.
     */
    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }

    @FXML
    /**
	 * Handles activation of the edit menu option.
	 * Opens simulationEditDialog
	 */
    private void handleEdit()
    {
        this.mainApp.showSimulationEdit();
    }

    @FXML
    /**
     * Handles activation of the save-as button.
     * Opens a dialog for setting the name of the JSON
     * file to be saved.
     * <p>
     * Saves the current settings with a set name as a JSON file.
     * <p>
     * Shows an alert window if the application is unable to save
     * the file or if the given name is empty.
     */
    private void handleSaveAs()
    {
    	
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save As:");
        dialog.setHeaderText("Give a name for the file:");
        Optional<String> optional = dialog.showAndWait();

        if (!optional.isPresent())
			{
				return;
			}

        String filename = optional.get().split("[.]")[0];
        if (filename.length() == 0)
        {
        	Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Failed to save");
            alert.setHeaderText("No name given!");
            alert.showAndWait();
            return;
        }
        if (filename.equals("default"))
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Failed to save");
            alert.setHeaderText("Cannot overwrite the default settings");
            alert.showAndWait();
            return;
        }
        System.out.println(filename);
        String cwd = System.getProperty("user.dir");
        File f = new File(cwd+"/src/main/resources/options/settings.json");
        JSONObject jsonObj = JSONReader.readJSON(f);

        try (FileWriter writer = new FileWriter(cwd+"/src/main/resources/options/"+filename+".json"))
        {
            writer.write(jsonObj.toJSONString());
        } catch (IOException e)
        {
        	Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Failed to save");
            alert.setHeaderText("Failed to save the file!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    /**
     * Handles activation of the load menu-item.
     * Gives a drop-down menu of the available settings 
     * files to select.
     * <p>
     * Loads the selected file as settings.json.
     */
    private void handleLoad()
    {
        String cwd = System.getProperty("user.dir");
        File directory = new File(cwd+"/src/main/resources/options/");
        File[] list = directory.listFiles();
        String[] filenames = new String[list.length];
        for (int i = 0; i < list.length; i++)
        {
            filenames[i] = list[i].getName();
        }
        ChoiceDialog dialog = new ChoiceDialog("Settings", filenames);
        dialog.setTitle("Load settings");
        dialog.setHeaderText("Select a settings file:\n Warning! This will overwrite current settings");
        dialog.showAndWait();
        String file = (String)dialog.getSelectedItem();
        if (file.equals("Settings"))
        {
            return;
        }
        File f = new File(cwd+"/src/main/resources/options/"+file);
        JSONObject jsonObj = JSONReader.readJSON(f);
        
        try (FileWriter writer = new FileWriter(cwd+"/src/main/resources/options/settings.json"))
        {
            writer.write(jsonObj.toJSONString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

       this.mainApp.updateSettingsText();
    }
    
    @FXML
    /**
     * Handles the activation of the show-results
     * menu item.
     * Opens the result window
     */
    public void handleShowResults()
    {
    	this.mainApp.showResults();
    }

    

}
