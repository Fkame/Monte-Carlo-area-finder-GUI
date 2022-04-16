package app;

import java.io.IOException;

import app.controllers.AreaFinder1dController;
import app.controllers.AreaFinder2dController;
import app.controllers.ChooseFunctionalitySceneController;
import app.controllers.ISceneController;
import app.wrappers.ScenesInfoContainer;
import javafx.application.Application;
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

        SceneInfoLoader<AreaFinder1dController> area1dFinderLoader = new SceneInfoLoader<>();
        scenesContainer.setAreaFinder1dWrapper(area1dFinderLoader.loadSceneData("area_finder_1d_scene"));

        if (scenesContainer.getChooseFunctionalitySceneWrapper() == null ||
            scenesContainer.getAreaFinder2dWrapper() == null ||
            scenesContainer.getAreaFinder1dWrapper() == null) 
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