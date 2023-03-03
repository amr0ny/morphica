package com.iclustudio.morphica;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.Random;

public class CatmullRomShape {


    private final int POINTS_NUM = 100;

    private int sides;
    private float minRadius;
    private  float maxRadius;
    private Color currentColor;
    private Color targetColor;
    private Color startColor;
    float[] vertices;
    short[] indices;
    private CatmullRomSpline<Vector2> curve;
    private Pixmap pixmap;
    private Texture texture;

    private ShapeRenderer shapeRenderer;
    private PolygonSpriteBatch batch;
    private Random random;
    private RandomPolygon startRandomPolygon;
    private RandomPolygon targetRandomPolygon;
    private Vector2[] currentControlPoints;
    private Vector2[] startControlPoints; // начальные позиции контрольных точек
    private Vector2[] targetControlPoints; // целевые позиции контрольных точек
    private PolygonRegion region;
    private Vector2 tmp;
    private float alpha = 0;
    private float duration;

    private float timeElapsed = 0;
    private float drawingTime = 0;

    public CatmullRomShape(int sides, float minRadius, float maxRadius, float duration) {
        this.sides = sides;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.duration = duration;
        this.random = new Random();
        this.vertices = new float[POINTS_NUM * 2];
        this.indices = new short[POINTS_NUM + 2];
        this.shapeRenderer = new ShapeRenderer();
        this.pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        this.startRandomPolygon = new RandomPolygon(this.sides, this.minRadius, this.maxRadius);
        this.targetRandomPolygon = new RandomPolygon(this.sides, this.minRadius, this.maxRadius);
        this.startControlPoints = this.startRandomPolygon.getVertices().toArray(new Vector2[sides]);
        this.targetControlPoints = this.targetRandomPolygon.getVertices().toArray(new Vector2[sides]);
        this.currentControlPoints = Arrays.copyOf(startControlPoints, startControlPoints.length);

        this.startColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
        this.targetColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
        this.currentColor = new Color(startColor.r, startColor.g, startColor.b, 1f);

        this.pixmap.setColor(currentColor);
        this.texture = new Texture(pixmap);
        this.batch = new PolygonSpriteBatch();
        this.curve = new CatmullRomSpline<>(this.currentControlPoints, true);

        float[] floatCurrentControlPoints = new float[currentControlPoints.length * 2]; // массив float дол
        for (int i = 0, j = 0; i < currentControlPoints.length; i++, j += 2) {
            floatCurrentControlPoints[j] = currentControlPoints[i].x;
            floatCurrentControlPoints[j + 1] = currentControlPoints[i].y;
        }
        this.region = new PolygonRegion(new TextureRegion(texture), floatCurrentControlPoints, new EarClippingTriangulator().computeTriangles(vertices).toArray());

        this.tmp = new Vector2();

    }
    public void drawShape() {
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


    }

    public void render(Interpolation interpolation) {
             // обновление текущего прогресса изменения
        timeElapsed += Gdx.graphics.getDeltaTime();
        alpha = timeElapsed / duration;

        currentColor.r = interpolation.apply(startColor.r, targetColor.r, alpha);
        currentColor.g = interpolation.apply(startColor.g, targetColor.g, alpha);
        currentColor.b = interpolation.apply(startColor.b, targetColor.b, alpha);

        for (int i = 0; i < sides; i++) {
            currentControlPoints[i] = new Vector2(interpolation.apply(startControlPoints[i].x, targetControlPoints[i].x, alpha), interpolation.apply(startControlPoints[i].y, targetControlPoints[i].y, alpha));
        }

        if (alpha > 1) {
            timeElapsed = 0;
            targetRandomPolygon = new RandomPolygon(sides, minRadius, maxRadius);
            targetControlPoints = targetRandomPolygon.getVertices().toArray(new Vector2[sides]);
            targetColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
            startColor = new Color(currentColor.r, currentColor.g, currentColor.b, 1f);
            startControlPoints = Arrays.copyOf(currentControlPoints, currentControlPoints.length);
        }

        drawShape();


        pixmap.setColor(currentColor);
        pixmap.fill();
        texture = new Texture(pixmap);
        //Создание PolygonRegion для триангуляции
        region = new PolygonRegion(new TextureRegion(texture), vertices, new EarClippingTriangulator().computeTriangles(vertices).toArray());
        // Создаем PolygonSpriteBatch и отрисовываем
        batch.begin();
        batch.draw(region, 0, 0);
        batch.end();
    }

    public void dispose() {
        pixmap.dispose();
        shapeRenderer.dispose();
    }

    public Texture getTexture() {
        return texture;
    }
}
