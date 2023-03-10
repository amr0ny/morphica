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
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

public class ShapeModel extends PolygonModel implements IModel {
    private final int POINTS_NUM = 40;


    private int sides;
    private float minRadius;
    private  float maxRadius;
    private float duration;
    private Interpolation interpolation;



    float[] vertices;
    short[] indices;


    private float alpha = 0;
    private float timeElapsed = 0;

    private CatmullRomSpline<Vector2> curve;
    private Pixmap pixmap;
    private Texture texture;
    private ShapeRenderer shapeRenderer;
    private PolygonRegion region;


    private PolygonModel currentPolygonModel;
    private PolygonModel startPolygonModel;
    private PolygonModel targetPolygonModel;

    public ShapeModel(int sides, float minRadius, float maxRadius, float duration, Interpolation interpolation) {
        super(sides, minRadius, maxRadius);
        startPolygonModel = new PolygonModel(sides, minRadius, maxRadius);
        startPolygonModel.nextColor();
        targetPolygonModel = new PolygonModel(sides, minRadius, maxRadius);
        targetPolygonModel.nextColor();
        currentPolygonModel = new PolygonModel(sides, minRadius, maxRadius);
        currentPolygonModel.setColor(startPolygonModel.getColor());

        this.sides = sides;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.duration = duration;
        this.interpolation = interpolation;

        this.vertices = new float[POINTS_NUM * 2];
        this.indices = new short[POINTS_NUM + 2];

        this.pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        this.pixmap.setColor(currentPolygonModel.getColor());
        this.texture = new Texture(pixmap);
        this.region = new PolygonRegion(new TextureRegion(texture), currentPolygonModel.getVertices(), new EarClippingTriangulator().computeTriangles(vertices).toArray());
        this.shapeRenderer = new ShapeRenderer();

        this.curve = new CatmullRomSpline<>(currentPolygonModel.getVertexes(), true);
    }


    public void update(float deltaTime) {

        Vector2 tmp = new Vector2();
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

        currentPolygonModel.setColor(new Color(interpolation.apply(startPolygonModel.getColor().r,
                targetPolygonModel.getColor().r, alpha), interpolation.apply(startPolygonModel.getColor().g,
                targetPolygonModel.getColor().g, alpha), interpolation.apply(startPolygonModel.getColor().b,
                targetPolygonModel.getColor().b, alpha), 1f));

        for (int i = 0; i < currentPolygonModel.getVertexCount(); i++) {
            currentPolygonModel.setVertex(i, interpolation.apply(startPolygonModel.getVertices()[i*2], targetPolygonModel.getVertices()[i*2], alpha), interpolation.apply(startPolygonModel.getVertices()[i*2+1], targetPolygonModel.getVertices()[i*2+1], alpha));
        }

        if (isTransitionOver()) {
            timeElapsed = 0;
            targetPolygonModel.nextColor();
            startPolygonModel.setColor(currentPolygonModel.getColor());
            startPolygonModel.setVertices(Arrays.copyOf(currentPolygonModel.getVertices(), currentPolygonModel.getVertexCount()*2));
        }
        this.curve = new CatmullRomSpline<>(currentPolygonModel.getVertexes(), true);
        pixmap.setColor(currentPolygonModel.getColor());
        pixmap.fill();
        texture = new Texture(pixmap);
        region = new PolygonRegion(new TextureRegion(texture), vertices, new EarClippingTriangulator().computeTriangles(vertices).toArray());

    }
    public boolean isTransitionOver(){
        return (alpha > 1 );
    }
    public PolygonRegion getShape() {
        return region;
    }
    public float getDuration() {
        return duration;
    }
    public PolygonModel getCurrentPolygonModel() {
        return currentPolygonModel;
    }
    public PolygonModel getStartPolygonModel() {
        return startPolygonModel;
    }
    public PolygonModel getTargetPolygonModel() {
        return targetPolygonModel;
    }
    public void setColor(Color color) {
        currentPolygonModel.setColor(color);
    }
    public Color getColor() {
        return currentPolygonModel.getColor();
    }
    public void dispose() {
        pixmap.dispose();
        shapeRenderer.dispose();
    }
}
