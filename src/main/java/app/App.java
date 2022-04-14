package app;

import java.io.IOException;
import java.net.URL;

import app.controllers.ChooseFunctionalitySceneController;
import app.controllers.ISceneController;
import app.controllers.AreaFinder2dController;
import app.wrappers.SceneInfoWrapper;
import app.wrappers.ScenesInfoContainer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        ScenesInfoContainer scenesContainer = new ScenesInfoContainer();

        SceneInfoLoader<ChooseFunctionalitySceneController> chooseFuncSceneLoader = new SceneInfoLoader<>();
        scenesContainer.setChooseFunctionalitySceneWrapper(chooseFuncSceneLoader.loadSceneData("choose_of_functionality_scene"));

        SceneInfoLoader<AreaFinder2dController> area2dFinderLoader = new SceneInfoLoader<>();
        scenesContainer.setAreaFinder2dWrapper(area2dFinderLoader.loadSceneData("area_finder_2d_scene"));

        if (scenesContainer.getChooseFunctionalitySceneWrapper() == null ||
            scenesContainer.getAreaFinder2dWrapper() == null) 
        {
            System.out.println("Node is null, exit from prog.");
            System.exit(-1);
        }

        for (ISceneController controller : scenesContainer.getControllersAsList()) {
            controller.prepareAllComponents();
            controller.setStage(stage);
            controller.setScenesWrapper(scenesContainer);
        }

        Parent root = scenesContainer.getChooseFunctionalitySceneWrapper().getRoot();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        ISceneController chooseStageController = scenesContainer.getChooseFunctionalitySceneWrapper().getController();
        chooseStageController.prepareStageBeforeShow();

        stage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }

}