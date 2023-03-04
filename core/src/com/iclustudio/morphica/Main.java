package com.iclustudio.morphica;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Interpolation;

import java.util.Random;

public class Main extends ApplicationAdapter {

	private GameController gameController;
	private GameModel gameModel;
	private GameView gameView;


	private Array<Shape> shapes;
	private Random random;
	private int shapesAmount;



	@Override
	public void create() {

		shapesAmount = 3;

		gameModel = new GameModel(shapesAmount);
		gameView = new GameView(gameModel);
		gameController = new GameController(gameModel, gameView);
	}

	@Override
	public void render() {

		gameController.update(Gdx.graphics.getDeltaTime());
		gameView.render();

	}


	@Override
	public void dispose() {
		gameController.dispose();
	}
}
