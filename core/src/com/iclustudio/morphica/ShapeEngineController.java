package com.iclustudio.morphica;

public class ShapeEngineController implements IController {

    ShapeEngineModel shapeEngineModel;
    ShapeEngineView shapeEngineView;

    public ShapeEngineController(ShapeEngineModel shapeEngineModel, ShapeEngineView shapeEngineView) {
        this.shapeEngineModel = shapeEngineModel;
        this.shapeEngineView = shapeEngineView;


    }

    public void update(float deltaTime) {
    //    shapeEngineModel.update(deltaTime);
    //    shapeEngineView.render();
    }

    public void dispose() {
        shapeEngineModel.dispose();
        shapeEngineView.dispose();
    }
}
