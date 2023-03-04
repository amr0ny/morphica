package com.iclustudio.morphica;

public class ShapeController implements IController {
    ShapeModel shapeModel;
    ShapeView shapeView;

    public ShapeController(ShapeModel shapeModel, ShapeView shapeView) {
        this.shapeModel = shapeModel;
        this.shapeView = shapeView;
    }

    public void update(float deltaTime) {
        shapeModel.update(deltaTime);
        shapeView.render();
    }

    public void dispose() {
        shapeModel.dispose();
        shapeView.dispose();
    }
}
