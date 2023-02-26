package com.iclustudio.morphica;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.List;
import java.util.Random;

public class Main extends ApplicationAdapter {
	private CatmullRomShape shape;
	private Random random;

	private Color startColor;
	private Color targetColor;

	private Color currentColor;

	private RandomPolygon startRandomPolygon;
	private RandomPolygon targetRandomPolygon;
	private Array<Vector2> startControlPoints; // начальные позиции контрольных точек
	private Array<Vector2> targetControlPoints; // целевые позиции контрольных точек
	private float progress; // текущий прогресс изменения (от 0 до 1)
	private float duration = 1f; // продолжительность изменения (в секундах)


	@Override
	public void create() {
		// Создайте экземпляр класса Random.
		random = new Random();
		// Создайте новый цвет со случайными значениями красного, зеленого и синего компонентов.
		startColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
		targetColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
		currentColor = new Color();
		startRandomPolygon = new RandomPolygon(7, 40, 450);
		targetRandomPolygon = new RandomPolygon(7, 40, 450);
		startControlPoints = ToArray(startRandomPolygon.getVertices());
		targetControlPoints = ToArray(targetRandomPolygon.getVertices());

		shape = new CatmullRomShape(startControlPoints);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1); // задает цвет очистки экрана (черный)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // очистить экран

		// обновление текущего прогресса изменения
		progress += Gdx.graphics.getDeltaTime() / duration;

		currentColor.r = startColor.r * (1 - progress) + targetColor.r * progress;
		currentColor.g = startColor.g * (1 - progress) + targetColor.g * progress;
		currentColor.b = startColor.b * (1 - progress) + targetColor.b * progress;
		currentColor.a = startColor.a * (1 - progress) + targetColor.a * progress;

		if (progress > 1f) {
			progress = 0f;
			// обновление целевых позиций контрольных точек для следующей анимации
			// ...
			targetRandomPolygon = new RandomPolygon(7, 40, 450);
			targetControlPoints = ToArray(targetRandomPolygon.getVertices());
			targetColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
			startColor = currentColor;
		}

		// линейная интерполяция между начальными и целевыми позициями контрольных точек
		for (int i = 0; i < startControlPoints.size; i++) {
			startControlPoints.get(i).lerp(targetControlPoints.get(i), progress);
		}
		// отрисовываем фигуру

		shape.drawShape(currentColor);
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
