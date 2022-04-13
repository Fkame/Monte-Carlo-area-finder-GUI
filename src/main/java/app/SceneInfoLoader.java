package app;

import java.io.IOException;
import java.net.URL;

import app.wrappers.SceneInfoWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class SceneInfoLoader<T> {

    public SceneInfoWrapper<T> loadSceneData(String fxmlFileNameWithScene) throws IOException {
        String path = "fxml/" + fxmlFileNameWithScene + ".fxml";
        URL url = null;
        try {
            url = App.class.getResource(path);
            FXMLLoader fxmlLoader = new FXMLLoader(url);

            Parent root = fxmlLoader.load();
            T controller = fxmlLoader.getController();
            return new SceneInfoWrapper<T>(root, controller);
        }
        catch (Exception e) {
            System.out.println("Path=" + path + ", isURLNull=" + (url == null));
            if (url != null) System.out.println("URL path = " + url.getPath());
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
