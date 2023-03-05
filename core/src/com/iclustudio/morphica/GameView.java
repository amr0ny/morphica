package com.iclustudio.morphica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;

public class GameView implements IView {

    GameModel gameModel;
    ShapeEngineView shapeEngineView;

    public GameView(GameModel gameModel) {
        shapeEngineView = new ShapeEngineView(gameModel.getShapeEngineModel());

    }

    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1); // задает цвет очистки экрана (черный)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // очистить экран

        shapeEngineView.render();
    }

    public void dispose() {
        shapeEngineView.dispose();
    }
}
