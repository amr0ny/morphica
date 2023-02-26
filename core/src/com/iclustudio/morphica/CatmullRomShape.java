package com.iclustudio.morphica;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CatmullRomShape {


    private static final int POINTS_NUM = 1000;
    float[] vertices;
    short[] indices;
    private CatmullRomSpline<Vector2> curve;
    private Pixmap pixmap;
    private Texture texture;

    private ShapeRenderer shapeRenderer;
    private PolygonSpriteBatch batch;

    private PolygonRegion region;
    private Vector2[] controlPoints;
    private Vector2 tmp;

    public CatmullRomShape(float controlPoints[]) {
        for (int i = 0; i < controlPoints.length; i += 2) {
            this.controlPoints[i].x = controlPoints[i];
            this.controlPoints[i+1].y = controlPoints[i+1];
        }
        init();
    }

    public CatmullRomShape(Array<Vector2> controlPoints) {
        this.controlPoints = new Vector2[controlPoints.size];
        for (int i = 0; i < controlPoints.size; i++) {
            this.controlPoints[i] = controlPoints.get(i);
        }
        init();
    }

    private void init() {
        this.vertices = new float[POINTS_NUM * 2];
        this.indices = new short[POINTS_NUM + 2];
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new PolygonSpriteBatch();
        this.curve = new CatmullRomSpline<>(this.controlPoints, true);
    }
    public void drawShape(Color color) {
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
        pixmap.setColor(color);
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


    public void dispose() {
        shapeRenderer.dispose();
    }
}
