package com.iclustudio.morphica;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GameModel implements IModel{

    private ShapeEngineModel shapeEngineModel;

    public GameModel() {
        shapeEngineModel = new ShapeEngineModel(3, 3, 3, 7, null);
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
