module app {
    requires javafx.controls;
    requires javafx.fxml;
    opens app.controllers to javafx.fxml;
    opens app.wrappers to javafx.base;
    
    exports app;
}
