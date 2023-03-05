package com.iclustudio.morphica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import com.badlogic.gdx.math.MathUtils;

import org.w3c.dom.ranges.RangeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Polygon {

    private Random random;

    private List<Vector2> startControlPoints;
    private List<Vector2> targetControlPoints;
    private int sides;
    private float minRadius;
    private float maxRadius;

    public Polygon(int sides, float minRadius, float maxRadius) {

        this.sides = sides;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;

        random = new Random();


        nextStartControlPoints();
        nextTargetControlPoints();
    }

    public Vector2[] getStartControlPoints() {
        return startControlPoints.toArray(new Vector2[sides]);
    }
    public Vector2[] getTargetControlPoints() {
        return targetControlPoints.toArray(new Vector2[sides]);
    }

    public void setStartControlPoints(Vector2[] newStartControlPoints) {
        try {
            for (int i = 0; i < startControlPoints.size(); i++) {
                startControlPoints.set(i, newStartControlPoints[i]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setTargetControlPoints(Vector2[] newTargetControlPoints) {
       try {
           for (int i = 0; i < targetControlPoints.size(); i++) {
               targetControlPoints.set(i, newTargetControlPoints[i]);
           }
       } catch (ArrayIndexOutOfBoundsException e) {
           System.out.println(e.getMessage());
       }
    }

    public Color nextColor() {
        return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
    }

    public void nextStartControlPoints() {
        startControlPoints = new ArrayList<Vector2>();
        // Вычисляем угол между вершинами многоугольника.
        float angle = MathUtils.PI2 / sides;

        // Находим центр экрана.
        float offsetX = MathUtils.random(maxRadius, Gdx.graphics.getWidth()-maxRadius);
        float offsetY = MathUtils.random(maxRadius, Gdx.graphics.getHeight()-maxRadius);

        // Создаем вершины многоугольника.
        for (int i = 0; i < sides; i++) {
            // Вычисляем координаты вершины.
            float radius = MathUtils.random(minRadius, maxRadius);
            float x = radius * MathUtils.cos(i * angle) + offsetX;
            float y = radius * MathUtils.sin(i * angle) + offsetY;
            Vector2 vertex = new Vector2(x, y);

            // Добавляем вершину в список.
            startControlPoints.add(vertex);
        }

        // Сортируем вершины по углу.
        Vector2 center = getCenter(startControlPoints);
        Collections.sort(startControlPoints, new VertexComparator(center));
    }
    public void nextTargetControlPoints() {
        targetControlPoints = new ArrayList<Vector2>(sides);
        // Вычисляем угол между вершинами многоугольника.
        float angle = MathUtils.PI2 / sides;

        // Находим центр экрана.
        float offsetX = MathUtils.random(maxRadius, Gdx.graphics.getWidth()-maxRadius);
        float offsetY = MathUtils.random(maxRadius, Gdx.graphics.getHeight()-maxRadius);

        // Создаем вершины многоугольника.
        for (int i = 0; i < sides; i++) {
            // Вычисляем координаты вершины.
            float radius = MathUtils.random(minRadius, maxRadius);
            float x = radius * MathUtils.cos(i * angle) + offsetX;
            float y = radius * MathUtils.sin(i * angle) + offsetY;
            Vector2 vertex = new Vector2(x, y);

            // Добавляем вершину в список.
            targetControlPoints.add(vertex);
        }

        // Сортируем вершины по углу.
        Vector2 center = getCenter(targetControlPoints);
        Collections.sort(targetControlPoints, new VertexComparator(center));
    }

    private static Vector2 getCenter(List<Vector2> controlPoints) {
        float x = 0;
        float y = 0;

        for (Vector2 vertex : controlPoints) {
            x += vertex.x;
            y += vertex.y;
        }

        return new Vector2(x / controlPoints.size(), y / controlPoints.size());
    }

    private static class VertexComparator implements java.util.Comparator<Vector2> {
        private Vector2 center;

        public VertexComparator(Vector2 center) {
            this.center = center;
        }

        @Override
        public int compare(Vector2 v1, Vector2 v2) {
            float angle1 = MathUtils.atan2(v1.y - center.y, v1.x - center.x);
            float angle2 = MathUtils.atan2(v2.y - center.y, v2.x - center.x);
            return Float.compare(angle1, angle2);
        }
    }
}
