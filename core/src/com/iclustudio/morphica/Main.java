package com.iclustudio.morphica;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Interpolation;

import java.util.List;
import java.util.Random;

public class Main extends ApplicationAdapter {

	private CatmullRomShape shape1;
	private Batch batch;
	@Override
	public void create() {
		shape1 = new CatmullRomShape(5, 60, 600, 4f);

	}

	@Override
	public void render() {
		shape1.render(Interpolation.sine);
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
