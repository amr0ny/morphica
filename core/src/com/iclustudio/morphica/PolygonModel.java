package com.iclustudio.morphica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class PolygonModel extends Polygon {
    private int sides;
    private float minRadius;
    private float maxRadius;
    private Random random;


    private Color color;

    public PolygonModel(int sides, float minRadius, float maxRadius) {
        super();
        this.sides = sides;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        random = new Random();
        nextVertices();
        nextColor();
    }

    public void nextColor() {
        color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
    }


    public void nextVertices() {
        List<Vector2> vertices = new ArrayList<Vector2>(sides);
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
            vertices.add(vertex);
        }

        // Сортируем вершины по углу.
        Vector2 center = getCenter(vertices);
        Collections.sort(vertices, new VertexComparator(center));


        setVertices(vertices.toArray(new Vector2[sides]));
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setVertices(Vector2[] vertices) {
        float[] floatVertices = new float[vertices.length * 2]; // массив float дол
        for (int i = 0, j = 0; i < vertices.length; i++, j += 2) {
            floatVertices[j] = vertices[i].x;
            floatVertices[j + 1] = vertices[i].y;
        }
        super.setVertices(floatVertices);
    }
    public Vector2[] getVertexes() {
        Vector2[] vectorVertices = new Vector2[getVertices().length/2];

        for (int i = 0; i < getVertices().length; i += 2) {
            vectorVertices[i / 2] = new Vector2(getVertices()[i], getVertices()[i + 1]);
        }
        return vectorVertices;
    }
    private static Vector2 getCenter(List<Vector2> vertices) {
        float x = 0;
        float y = 0;

        for (Vector2 vertex : vertices) {
            x += vertex.x;
            y += vertex.y;
        }

        return new Vector2(x / vertices.size(), y / vertices.size());
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
