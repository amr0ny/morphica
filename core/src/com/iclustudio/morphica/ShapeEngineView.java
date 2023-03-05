package com.iclustudio.morphica;

import com.badlogic.gdx.utils.Array;

public class ShapeEngineView implements IView {
    ShapeEngineModel shapeEngineModel;
    Array<ShapeView> shapeViews;

    public ShapeEngineView(ShapeEngineModel shapeEngineModel) {
        this.shapeEngineModel = shapeEngineModel;
        this.shapeViews = new Array<ShapeView>();
        this.shapeViews.setSize(this.shapeEngineModel.getShapeModels().size);
        for (int i = 0; i < this.shapeEngineModel.getShapeModels().size; i++) {
            shapeViews.set(i, new ShapeView(shapeEngineModel.getShapeModels().get(i)));
        }
    }

    public void render() {
        for (int i = 0; i < shapeViews.size; i++) {
            shapeViews.get(i).render();
        }
    }

    public void dispose() {
        for (int i = 0; i < shapeViews.size; i++) {
            shapeViews.get(i).dispose();
        }
    }
}
