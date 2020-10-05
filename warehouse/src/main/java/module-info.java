module warehouse.simulator {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires json.simple;
    exports warehouse.simulator;
    opens warehouse.simulator to javafx.fxml;
    exports warehouse.simulator.view;
    opens warehouse.simulator.view to javafx.fxml;

}