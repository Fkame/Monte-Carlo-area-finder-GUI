module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires exp4j;
    opens app.controllers to javafx.fxml;
    opens app.controllers.areaFinder1d to javafx.fxml;
    opens app.controllers.areaFinder2d to javafx.fxml;
    opens app.controllers.areaFinder1d.wrappers to javafx.base;
    opens app.controllers.areaFinder2d.wrappers to javafx.base;
    
    exports app;
}
