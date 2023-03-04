package com.iclustudio.morphica;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.Random;

public class ShapeModel implements IModel {
    private final int POINTS_NUM = 100;


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
    private Polygon startPolygon;
    private Polygon targetPolygon;
    private Vector2[] currentControlPoints;
    private Vector2[] startControlPoints; // начальные позиции контрольных точек
    private Vector2[] targetControlPoints; // целевые позиции контрольных точек

    private Vector2 tmp;
    private float alpha = 0;

    private float timeElapsed = 0;

    public ShapeModel(int sides, float minRadius, float maxRadius, float duration, Interpolation interpolation) {
        this.sides = sides;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.duration = duration;
        this.interpolation = interpolation;

        this.vertices = new float[POINTS_NUM * 2];
        this.indices = new short[POINTS_NUM + 2];


        this.random = new Random();
        this.startPolygon = new Polygon(this.sides, this.minRadius, this.maxRadius);
        this.targetPolygon = new Polygon(this.sides, this.minRadius, this.maxRadius);

        this.startControlPoints = this.startPolygon.getVertices().toArray(new Vector2[sides]);
        this.targetControlPoints = this.targetPolygon.getVertices().toArray(new Vector2[sides]);
        this.currentControlPoints = Arrays.copyOf(startControlPoints, startControlPoints.length);

        float[] floatCurrentControlPoints = new float[currentControlPoints.length * 2]; // массив float дол
        for (int i = 0, j = 0; i < currentControlPoints.length; i++, j += 2) {
            floatCurrentControlPoints[j] = currentControlPoints[i].x;
            floatCurrentControlPoints[j + 1] = currentControlPoints[i].y;
        }

        this.startColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
        this.targetColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
        this.currentColor = new Color(startColor.r, startColor.g, startColor.b, 1f);
        this.pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        this.pixmap.setColor(currentColor);
        this.texture = new Texture(pixmap);
        this.region = new PolygonRegion(new TextureRegion(texture), floatCurrentControlPoints, new EarClippingTriangulator().computeTriangles(vertices).toArray());
        this.shapeRenderer = new ShapeRenderer();

        this.curve = new CatmullRomSpline<>(this.currentControlPoints, true);
        this.tmp = new Vector2();
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

        timeElapsed += deltaTime;
        alpha = timeElapsed / duration;

        currentColor.r = interpolation.apply(startColor.r, targetColor.r, alpha);
        currentColor.g = interpolation.apply(startColor.g, targetColor.g, alpha);
        currentColor.b = interpolation.apply(startColor.b, targetColor.b, alpha);

        for (int i = 0; i < sides; i++) {
            currentControlPoints[i] = new Vector2(interpolation.apply(startControlPoints[i].x, targetControlPoints[i].x, alpha), interpolation.apply(startControlPoints[i].y, targetControlPoints[i].y, alpha));
        }

        if (alpha > 1) {
            timeElapsed = 0;
            targetPolygon = new Polygon(sides, minRadius, maxRadius);
            targetControlPoints = targetPolygon.getVertices().toArray(new Vector2[sides]);
            targetColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
            startColor = new Color(currentColor.r, currentColor.g, currentColor.b, 1f);
            startControlPoints = Arrays.copyOf(currentControlPoints, currentControlPoints.length);
        }



        pixmap.setColor(currentColor);
        pixmap.fill();
        texture = new Texture(pixmap);
        //Создание PolygonRegion для триангуляции
        region = new PolygonRegion(new TextureRegion(texture), vertices, new EarClippingTriangulator().computeTriangles(vertices).toArray());

    }

    public PolygonRegion getShape() {
        return region;
    }

    public void dispose() {
        pixmap.dispose();
        shapeRenderer.dispose();
    }
}
