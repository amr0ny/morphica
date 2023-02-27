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
import com.badlogic.gdx.utils.Array;
import java.util.Random;

public class CatmullRomShape {


    private static final int POINTS_NUM = 1000;

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
    private RandomPolygon currentRandomPolygon;
    private RandomPolygon startRandomPolygon;
    private RandomPolygon targetRandomPolygon;

    private Vector2[] currentControlPoints;
    private Vector2[] startControlPoints; // начальные позиции контрольных точек
    private Vector2[] targetControlPoints; // целевые позиции контрольных точек
    private PolygonRegion region;
    private Vector2 tmp;
    private float progress = 0;
    private float duration;


    public CatmullRomShape(int sides, float minRadius, float maxRadius, float duration) {
        this.sides = sides;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.duration = duration;
        this.startRandomPolygon = new RandomPolygon(this.sides, this.minRadius, this.maxRadius);
        this.targetRandomPolygon = new RandomPolygon(this.sides, this.minRadius, this.maxRadius);
        this.currentRandomPolygon = this.startRandomPolygon;
        this.startControlPoints = this.startRandomPolygon.getVertices().toArray(new Vector2[sides]);
        this.targetControlPoints = this.targetRandomPolygon.getVertices().toArray(new Vector2[sides]);
        this.currentControlPoints = this.startControlPoints;
        this.vertices = new float[POINTS_NUM * 2];
        this.indices = new short[POINTS_NUM + 2];
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new PolygonSpriteBatch();
        this.curve = new CatmullRomSpline<>(this.currentControlPoints, true);
        this.random = new Random();
        startColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
        targetColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
        currentColor = startColor;
    }
    public void drawShape() {
        tmp = new Vector2();
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

        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(currentColor);
        pixmap.fill();
        texture = new Texture(pixmap);
        pixmap.dispose();

        //Создание PolygonRegion для триангуляции
        region = new PolygonRegion(new TextureRegion(texture),
                vertices, new EarClippingTriangulator().computeTriangles(vertices).toArray());

        // Создаем PolygonSpriteBatch и отрисовываем
        batch.begin();
        batch.draw(region, 0, 0);
        batch.end();
    }

    public void render(Interpolation interpolation) {
        Gdx.gl.glClearColor(1, 1, 1, 1); // задает цвет очистки экрана (черный)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // очистить экран

        // обновление текущего прогресса изменения
        progress += Gdx.graphics.getDeltaTime() / duration;


        currentColor.r = interpolation.apply(startColor.r, targetColor.r, progress);
        currentColor.g = interpolation.apply(startColor.g, targetColor.g, progress);
        currentColor.b = interpolation.apply(startColor.b, targetColor.b, progress);
        currentColor.a = interpolation.apply(startColor.a, targetColor.a, progress);

        if (progress > 1f) {
            progress = 0f;

            targetRandomPolygon = new RandomPolygon(sides, minRadius, maxRadius);
            targetColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
            targetControlPoints = targetRandomPolygon.getVertices().toArray(new Vector2[sides]);
            startColor = currentColor;
            startRandomPolygon = currentRandomPolygon;

            for (int i = 0; i < sides; i++) {
                currentControlPoints[i] = new Vector2(interpolation.apply(startControlPoints[i].x, targetControlPoints[i].x, progress), Interpolation.smooth2.apply(startControlPoints[i].y, targetControlPoints[i].y, progress));

            }
        }
        drawShape();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
