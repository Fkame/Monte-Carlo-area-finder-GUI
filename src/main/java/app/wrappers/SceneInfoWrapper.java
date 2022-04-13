package app.wrappers;

import javafx.scene.Parent;

/**
 * Класс представляет собой контейнер для информации о сцене: корневой узел ({@link SceneInfoWrapper#root}) и класс контроллера для элементов внутри 
 * корневого узла ({@link SceneInfoWrapper#controller}) 
 * <p>&lt T &gt - класс контроллера, как так каждый контроллер это свой уникальный тип.
 */
public class SceneInfoWrapper<T> {
    private Parent root;
    private T controller;
    public SceneInfoWrapper(Parent root, T controller) {
        this.root = root;
        this.controller = controller;
    }

    public Parent getRoot() { return this.root; }
    public T getController() { return this.controller; }
}
