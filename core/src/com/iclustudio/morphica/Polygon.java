package com.iclustudio.morphica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Collections;


public class Polygon {
    private List<Vector2> vertices;

    public Polygon(int sides, float minRadius, float maxRadius) {
        vertices = new ArrayList<Vector2>();

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
        Vector2 center = getCenter();
        Collections.sort(vertices, new VertexComparator(center));
    }

    public List<Vector2> getVertices() {
        return vertices;
    }

    private Vector2 getCenter() {
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
