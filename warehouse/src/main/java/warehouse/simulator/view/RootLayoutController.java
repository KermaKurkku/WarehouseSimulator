package warehouse.simulator.view;

import javafx.fxml.FXML;
import warehouse.simulator.MainApp;

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
}
