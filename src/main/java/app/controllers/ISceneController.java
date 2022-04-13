package app.controllers;

import app.wrappers.ScenesInfoContainer;
import javafx.stage.Stage;

/**
 * FIXME: что-то сделать с этим классом, может использовать, а может удалить вообще, т.к. сейчас он не используется.
 * Интерфейс для унификации интерфейса всех контроллеров.
 */
public interface ISceneController {
    
    public void setStage(Stage stage);

    public void prepareAllComponents();

    public Stage getStage();

    public void setScenesInfoContainer(ScenesInfoContainer infoContainer);

    public ScenesInfoContainer getScenesInfoContainer();
}
