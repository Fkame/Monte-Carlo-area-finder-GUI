package app.controllers;

import app.wrappers.ScenesInfoContainer;
import javafx.stage.Stage;

/**
 * Интерфейс для того, чтобы когда нужно, обезличнено использовать контроллеры сцен.
 */
public interface ISceneController {
    
    public void setStage(Stage stage);

    public void prepareAllComponents();

    public Stage getStage();

    public void setScenesWrapper(ScenesInfoContainer scenesWrapper);

    public ScenesInfoContainer getScenesWrapper();

    public void prepareStageBeforeShow();
}
