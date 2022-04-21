package app.wrappers;

import java.util.Arrays;
import java.util.List;

import app.controllers.ChooseFunctionalitySceneController;
import app.controllers.ISceneController;
import app.controllers.areaFinder1d.AreaFinder1dController;
import app.controllers.areaFinder2d.AreaFinder2dController;

public class ScenesInfoContainer {
    
    private SceneInfoWrapper<ChooseFunctionalitySceneController> chooseFuncSceneWrapper;
    private SceneInfoWrapper<AreaFinder2dController> areaFinder2dWrapper;
    private SceneInfoWrapper<AreaFinder1dController> areaFinder1dWrapper;

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

    public void setAreaFinder1dWrapper(SceneInfoWrapper<AreaFinder1dController> areaFinder1dWrapper) {
        this.areaFinder1dWrapper = areaFinder1dWrapper;
    }

    public SceneInfoWrapper<AreaFinder1dController> getAreaFinder1dWrapper() {
        return this.areaFinder1dWrapper;
    }

    public List<ISceneController> getControllersAsList() {
        return Arrays.asList(this.chooseFuncSceneWrapper.getController(), 
                        this.areaFinder2dWrapper.getController(), 
                        this.areaFinder1dWrapper.getController());
    }
}
