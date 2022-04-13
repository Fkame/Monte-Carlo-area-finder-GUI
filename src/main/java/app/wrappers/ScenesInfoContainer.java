package app.wrappers;

import app.controllers.AreaFinder2dController;
import app.controllers.ChooseFunctionalitySceneController;

public class ScenesInfoContainer {
    
    private SceneInfoWrapper<ChooseFunctionalitySceneController> chooseFuncSceneWrapper;
    private SceneInfoWrapper<AreaFinder2dController> areaFinder2dWrapper;

    public ScenesInfoContainer() { }

    public void setChooseFunctionalitySceneWrapper(SceneInfoWrapper<ChooseFunctionalitySceneController> chooseFuncSceneWrapper) {
        this.chooseFuncSceneWrapper = chooseFuncSceneWrapper;
    }

    public SceneInfoWrapper<ChooseFunctionalitySceneController> getChooseFunctionalitySceneWrapper() {
        return this.chooseFuncSceneWrapper;
    }

    public void setAreaFinder2dWrapper(SceneInfoWrapper<AreaFinder2dController> areaFinder2dWrapper) {
        this.areaFinder2dWrapper = areaFinder2dWrapper;
    }

    public SceneInfoWrapper<AreaFinder2dController> getAreaFinder2dWrapper() {
        return this.areaFinder2dWrapper;
    }
}
