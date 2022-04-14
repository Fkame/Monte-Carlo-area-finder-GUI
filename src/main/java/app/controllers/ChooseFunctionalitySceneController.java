package app.controllers;

import app.controllers.support.SupplyMethods;
import app.wrappers.SceneInfoWrapper;
import app.wrappers.ScenesInfoContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChooseFunctionalitySceneController implements ISceneController {
    
    private Stage stage;
    private ScenesInfoContainer scenesWrapper;
    
    public ChooseFunctionalitySceneController() { }

    @FXML
    public void on2dModeClicked(ActionEvent event) {
        SceneInfoWrapper<AreaFinder2dController> nextSceneWrapper = scenesWrapper.getAreaFinder2dWrapper();

        /** 
         * TODO: Готовить все сцены вначале или каждый раз создавать новые, а если уже создавал, тогда getScene == null;
         */
        Scene nextScene = nextSceneWrapper.getRoot().getScene();
        if (nextScene == null) nextScene = new Scene(nextSceneWrapper.getRoot());
        this.stage.setScene(nextScene);

        nextSceneWrapper.getController().prepareStageBeforeShow();
    }

    @FXML
    public void on1dModeClicked(ActionEvent event) {
        SupplyMethods.getErrorAlert("Окно ещё не готово").showAndWait();
    }

    @Override 
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void prepareAllComponents() {

    }

    @Override
    public void prepareStageBeforeShow() {
        stage.centerOnScreen();
        stage.setTitle("Окно выбора режима работы");
        stage.setResizable(false);
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public void setScenesWrapper(ScenesInfoContainer scenesWrapper) {
        this.scenesWrapper = scenesWrapper;
    }

    @Override
    public ScenesInfoContainer getScenesWrapper() {
        return this.scenesWrapper;
    }
}
