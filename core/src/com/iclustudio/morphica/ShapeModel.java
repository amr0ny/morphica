package com.iclustudio.morphica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ShapeModel extends Polygon implements IModel {
    private final int POINTS_NUM = 200;


    private int sides;
    private float minRadius;
    private  float maxRadius;
    private float duration;
    private Interpolation interpolation;


    private Color currentColor;
    private Color targetColor;
    private Color startColor;
    float[] vertices;
    short[] indices;
    private CatmullRomSpline<Vector2> curve;


    private Pixmap pixmap;
    private Texture texture;
    private ShapeRenderer shapeRenderer;
    private PolygonRegion region;

    private Random random;


    private Vector2[] currentControlPoints;
    private Vector2[] startControlPoints;
    private Vector2[] targetControlPoints;
    private Vector2 tmp;
    private float alpha = 0;
    private float timeElapsed = 0;

    public ShapeModel(int sides, float minRadius, float maxRadius, float duration, Interpolation interpolation) {
        super();
        this.sides = sides;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.duration = duration;
        this.interpolation = interpolation;

        this.vertices = new float[POINTS_NUM * 2];
        this.indices = new short[POINTS_NUM + 2];
        this.random = new Random();
        setStartControlPoints(nextStartControlPoints());
        setTargetControlPoints(nextTargetControlPoints());

        this.currentControlPoints = Arrays.copyOf(startControlPoints, startControlPoints.length);
        this.startColor = nextColor();
        this.targetColor = nextColor();
        this.currentColor = new Color(startColor.r, startColor.g, startColor.b, 1f);
        this.pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        this.pixmap.setColor(currentColor);
        this.texture = new Texture(pixmap);
        this.region = new PolygonRegion(new TextureRegion(texture), toFloat(currentControlPoints), new EarClippingTriangulator().computeTriangles(vertices).toArray());
        this.shapeRenderer = new ShapeRenderer();
        this.curve = new CatmullRomSpline<>(this.currentControlPoints, true);
        this.tmp = new Vector2();
    }

    public Color nextColor() {
        return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
    }

    public void update(float deltaTime) {
        float t = 0;
        float step = 1f / POINTS_NUM;
        for (int i = 0; i < POINTS_NUM; i++) {
            curve.valueAt(tmp, t);
            vertices[i * 2] = tmp.x;
            vertices[i * 2 + 1] = tmp.y;
            t += step;
        }

        vertices[POINTS_NUM * 2 - 2] = vertices[0];
        vertices[POINTS_NUM * 2 - 1] = vertices[1];
        indices[0] = (short) (POINTS_NUM);
        for (int i = 0; i < POINTS_NUM; i++) {
            indices[i + 1] = (short) i;
        }
        indices[POINTS_NUM + 1] = 0;
        setVertices(vertices);

        timeElapsed += deltaTime;
        alpha = timeElapsed / duration;

        currentColor.r = interpolation.apply(startColor.r, targetColor.r, alpha);
        currentColor.g = interpolation.apply(startColor.g, targetColor.g, alpha);
        currentColor.b = interpolation.apply(startColor.b, targetColor.b, alpha);

        for (int i = 0; i < sides; i++) {
            currentControlPoints[i] = new Vector2(interpolation.apply(startControlPoints[i].x, targetControlPoints[i].x, alpha), interpolation.apply(startControlPoints[i].y, targetControlPoints[i].y, alpha));
        }
        if (isTransitionOver()) {
            timeElapsed = 0;
            targetColor = nextColor();
            startColor = new Color(currentColor.r, currentColor.g, currentColor.b, 1f);
            startControlPoints = Arrays.copyOf(currentControlPoints, currentControlPoints.length);
        }
        pixmap.setColor(currentColor);
        pixmap.fill();
        texture = new Texture(pixmap);
        //Создание PolygonRegion для триангуляции
        region = new PolygonRegion(new TextureRegion(texture), vertices, new EarClippingTriangulator().computeTriangles(vertices).toArray());

    }

    public Vector2[] nextStartControlPoints() {
        List<Vector2> startControlPoints = new ArrayList<Vector2>();
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
        Collections.sort(startControlPoints, new ShapeModel.VertexComparator(center));
        return startControlPoints.toArray(new Vector2[sides]);
    }
    public Vector2[] nextTargetControlPoints() {
        List<Vector2> targetControlPoints = new ArrayList<Vector2>(sides);
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
        Collections.sort(targetControlPoints, new ShapeModel.VertexComparator(center));
        return targetControlPoints.toArray(new Vector2[sides]);
    }
    public void setStartControlPoints(Vector2[] startControlPoints) {
        this.startControlPoints = Arrays.copyOf(startControlPoints, startControlPoints.length);
    }

    public void setTargetControlPoints(Vector2[] targetControlPoints) {
        this.targetControlPoints = Arrays.copyOf(targetControlPoints, targetControlPoints.length);
    }

    public Polygon getStartPolygon() { return new Polygon(toFloat(startControlPoints)); }
    public Polygon getTargetPolygon() {return new Polygon(toFloat(targetControlPoints)); }
    public Vector2[] getStartControlPoints() { return startControlPoints;}
    public Vector2[] getTargetControlPoints() { return targetControlPoints; }
    public Interpolation getInterpolation() {
        return  interpolation;
    }
    public float getDuration() {
        return duration;
    }
    public Vector2[] getCurrentControlPoints() {
        return currentControlPoints;
    }
    public boolean isTransitionOver(){
        return (alpha > 1 );
    }
    public PolygonRegion getShape() {
        return region;
    }
    public void dispose() {
        pixmap.dispose();
        shapeRenderer.dispose();
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


    public static float[] toFloat(Vector2[] vectorArr) {
        float[] floatArr = new float[vectorArr.length * 2]; // массив float дол
        for (int i = 0, j = 0; i < vectorArr.length; i++, j += 2) {
            floatArr[j] = vectorArr[i].x;
            floatArr[j + 1] = vectorArr[i].y;
        }
        return floatArr;
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
