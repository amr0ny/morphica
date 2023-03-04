package com.iclustudio.morphica;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;

public class ShapeView implements IView {
    ShapeModel shapeModel;
    PolygonSpriteBatch batch;

    public ShapeView(ShapeModel shapeModel) {
        this.shapeModel = shapeModel;
        batch = new PolygonSpriteBatch();
    }

    public void render() {
        batch.begin();
        batch.draw(shapeModel.getShape(), 0, 0);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }
}
