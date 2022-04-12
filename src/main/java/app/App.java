package app;

import java.io.IOException;
import java.net.URL;

import app.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static MainController controller;

    @Override
    public void start(Stage stage) throws IOException {

        Parent node = App.loadFXML("choose_of_functionality_scene");
        if (node == null) {
            System.out.println("Node is null, exit from prog.");
            System.exit(-1);
        }
        scene = new Scene(node);

        //controller.prepareAllComponents();

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Окно выбора режима работы");
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        String path = "fxml/" + fxml + ".fxml";
        URL url = null;
        try {
            url = App.class.getResource(path);
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent root = fxmlLoader.load();
            //App.controller = fxmlLoader.getController();
            return root;
        }
        catch (Exception e) {
            System.out.println("Path=" + path + ", isURLNull=" + (url == null));
            if (url != null) System.out.println("URL path = " + url.getPath());
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch();
    }

}