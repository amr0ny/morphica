package com.iclustudio.morphica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;

public class GameView implements IView {
    GameModel gameModel;
    Array<ShapeView> shapeViews;
    public GameView(GameModel gameModel) {
        this.gameModel = gameModel;
        this.shapeViews = new Array<ShapeView>();
        this.shapeViews.setSize(this.gameModel.getShapeModels().size);
        for (int i = 0; i < this.gameModel.getShapeModels().size; i++) {
            shapeViews.set(i, new ShapeView(gameModel.getShapeModels().get(i)));
        }

    }

    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1); // задает цвет очистки экрана (черный)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // очистить экран

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
