package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static MainController controller;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main_scene"));
        controller.prepareChartsAndTables();

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        App.controller = fxmlLoader.getController();
        return root;
    }

    public static void main(String[] args) {
        launch();
    }

}