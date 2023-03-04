package com.iclustudio.morphica;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GameModel implements IModel {
    int shapesAmount;
    Array<ShapeModel> shapeModels;

    Interpolation interpolation;
    public GameModel(int shapesAmount) {
        this.shapesAmount = shapesAmount;
        this.shapeModels = new Array<ShapeModel>();
        this.interpolation = Interpolation.sine;
        shapeModels.setSize(shapesAmount);
        for (int i = 0; i < shapesAmount; i++) {
            shapeModels.set(i, new ShapeModel(MathUtils.random(4, 10), (float)MathUtils.random(10, 60), (float)MathUtils.random(300, 700), (float)MathUtils.random(1, 10), interpolation));
        }
    }

    public void update(float deltaTime) {
        for (int i = 0; i < shapesAmount; i++) {
            shapeModels.get(i).update(deltaTime);
        }
    }

    public Array<ShapeModel> getShapeModels() {
        return shapeModels;
    }
    public void dispose() {
        for (int i = 0; i < shapesAmount; i++) {
            shapeModels.get(i).dispose();
        }
    }
}
