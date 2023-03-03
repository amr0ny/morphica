package com.iclustudio.morphica;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Interpolation;

import java.util.List;
import java.util.Random;

public class Main extends ApplicationAdapter {

	private CatmullRomShape shape1;
	private CatmullRomShape shape2;
	private SpriteBatch batch;
	@Override
	public void create() {
		shape1 = new CatmullRomShape(5, 60, 600, 2f);
		shape2 = new CatmullRomShape(7, 49, 400, 4f);
		batch = new SpriteBatch();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1); // задает цвет очистки экрана (черный)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // очистить экран
		shape1.render(Interpolation.sine);
		shape2.render(Interpolation.sine);
	}

	public Array<Vector2> ToArray(List<Vector2> list) {
		Array<Vector2> array = new Array<Vector2>();
		for(int i = 0; i < list.size(); i++) {
			array.add(list.get(i));
		}
		return array;
	}

	@Override
	public void dispose() {
		shape1.dispose();
	}

}
