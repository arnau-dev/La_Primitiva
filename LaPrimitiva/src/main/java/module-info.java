module primitiva.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    exports Controller;
    opens Controller to javafx.fxml;
    exports View;
    opens View to javafx.fxml;
}