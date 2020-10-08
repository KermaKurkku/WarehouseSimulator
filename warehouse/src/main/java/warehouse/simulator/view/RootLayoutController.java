package warehouse.simulator.view;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;


import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import warehouse.simulator.MainApp;
import warehouse.simulator.util.JSONReader;

public class RootLayoutController {
    private MainApp mainApp;
    
    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleEdit()
    {
        this.mainApp.showSimulationEdit();
    }

    @FXML
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
        System.out.println(filename);
        String cwd = System.getProperty("user.dir");
        File f = new File(cwd+"/src/main/resources/options/settings.json");
        JSONObject jsonObj = JSONReader.readJSON(f);

        try (FileWriter writer = new FileWriter(cwd+"/src/main/resources/options/"+filename+".json"))
        {
            writer.write(jsonObj.toJSONString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
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

    

}
