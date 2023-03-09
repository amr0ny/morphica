package com.iclustudio.morphica;

import com.badlogic.gdx.math.Polygon;

public class GameModel implements IModel{

    private ShapeEngineModel shapeEngineModel;

    public GameModel() {
        Polygon poly = new Polygon();
        shapeEngineModel = new ShapeEngineModel(4, 4, 2, 10, null);
    }

    public void update(float deltaTime) {
        shapeEngineModel.update(deltaTime);
    }

    public ShapeEngineModel getShapeEngineModel(){
        return shapeEngineModel;
    }

    public void dispose() {
        shapeEngineModel.dispose();
    }

}
