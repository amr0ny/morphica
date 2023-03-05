package com.iclustudio.morphica;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class Main extends ApplicationAdapter {

	private GameController gameController;
	private GameModel gameModel;
	private GameView gameView;

	@Override
	public void create() {

		gameModel = new GameModel();
		gameView = new GameView(gameModel);
		gameController = new GameController(gameModel, gameView);
	}

	@Override
	public void render() {
		gameController.update(Gdx.graphics.getDeltaTime());
	}


	@Override
	public void dispose() {
		gameController.dispose();
	}
}