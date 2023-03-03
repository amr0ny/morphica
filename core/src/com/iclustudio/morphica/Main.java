package com.iclustudio.morphica;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Interpolation;

import java.util.List;
import java.util.Random;

public class Main extends ApplicationAdapter {

	private Array<CatmullRomShape> shapes;
	private int shapesAmount;
	private Random random;
	@Override
	public void create() {
		shapesAmount = 5;
		shapes = new Array<CatmullRomShape>();
		shapes.setSize(shapesAmount);
		for (int i = 0; i < shapesAmount; i++) {
			shapes.set(i, new CatmullRomShape(MathUtils.random(4, 10), (float)MathUtils.random(10, 60), (float)MathUtils.random(300, 700), (float)MathUtils.random(1, 10)));
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1); // задает цвет очистки экрана (черный)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // очистить экран
		for (int i = 0; i < shapesAmount; i++) {
			shapes.get(i).render(Interpolation.sine);
		}
	}


	@Override
	public void dispose() {
		for (int i = 0; i < shapesAmount; i++) {
			shapes.get(i).dispose();
		}
	}
}
