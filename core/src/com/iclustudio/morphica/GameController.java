package com.iclustudio.morphica;

public class GameController implements IController {
    private GameView gameView;
    private GameModel gameModel;

    public GameController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }
    public void update(float deltaTime) {
        gameModel.update(deltaTime);
    }

    public void dispose() {
        gameModel.dispose();
        gameView.dispose();
    }
}
