package app.controllers;

import app.controllers.support.SupplyMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ChooseFunctionalitySceneController {
    
    public ChooseFunctionalitySceneController() { }

    @FXML
    public void on2dModeClicked(ActionEvent event) {
        SupplyMethods.getErrorAlert("Окно ещё не готово").showAndWait();
    }

    @FXML
    public void on1dModeClicked(ActionEvent event) {
        SupplyMethods.getErrorAlert("Окно ещё не готово").showAndWait();
    }
}
