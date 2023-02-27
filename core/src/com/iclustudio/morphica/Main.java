package com.iclustudio.morphica;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Interpolation;

import java.util.List;
import java.util.Random;

public class Main extends ApplicationAdapter {
	private CatmullRomShape shape;
	private Random random;

	private Interpolation interpolation;
	@Override
	public void create() {
		random = new Random();
		shape = new CatmullRomShape(7, 40, 450, 4f);
		interpolation = Interpolation.smooth2;
	}

	@Override
	public void render() {
		shape.render(interpolation);
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
		shape.dispose();
	}
}
