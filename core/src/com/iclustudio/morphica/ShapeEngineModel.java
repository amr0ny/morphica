package com.iclustudio.morphica;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class ShapeEngineModel implements IModel {


    private int shapesAmount;
    private float minDuration;
    private float maxDuration;
    private Color[] colorScheme;

    private Array<ShapeModel> shapeModels;

    public ShapeEngineModel(int minAmount, int maxAmount, int minDuration, int maxDuration, Color[] colorScheme) {
        shapesAmount = MathUtils.random(minAmount, maxAmount);
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.colorScheme = colorScheme;

        shapeModels = new Array<ShapeModel>();
        shapeModels.setSize(shapesAmount);

        for (int i = 0; i < shapesAmount; i++) {
            shapeModels.set(i, new ShapeModel(MathUtils.random(4, 10), (float) MathUtils.random(10, 60), (float) MathUtils.random(150, 500), (float) MathUtils.random(minDuration, maxDuration), Interpolation.linear));
        }

        for (int i = 0; i < shapesAmount-1; i++) {
            for (int j = 0; j < shapesAmount-i-1; j++) {
                if (shapeModels.get(j).getDuration() < shapeModels.get(j+1).getDuration()) {
                    ShapeModel tmpShapeModel = shapeModels.get(j);
                    shapeModels.set(j, shapeModels.get(j+1));
                    shapeModels.set(j+1, tmpShapeModel);
                }
            }
        }

        // A birth of blobs lifecycle stage
        // ...


    }

    public void update(float deltaTime) {

        // The transition lifecycle stage
        // ...




        // The end of transition lifecycle stage

        for (int i = 0; i < shapesAmount; i++) {
            if (shapeModels.get(i).isTransitionOver()) {
                // ...


                shapeModels.get(i).getTargetPolygonModel().nextVertices();
            }
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
