package com.iclustudio.morphica;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ShapeEngineModel implements IModel {



    private int shapesAmount;
    private float minDuration;
    private float maxDuration;
    private Color[] colorScheme;

    private Array<ShapeModel> shapeModels;

    public ShapeEngineModel(int minAmount, int maxAmount, int minDuration, int maxDuration, Color[] colorScheme) {
        shapesAmount = MathUtils.random(minAmount, maxAmount);
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.colorScheme = colorScheme;

        shapeModels = new Array<ShapeModel>();
        shapeModels.setSize(shapesAmount);

        for (int i = 0; i < shapesAmount; i++) {
            shapeModels.set(i, new ShapeModel(MathUtils.random(4, 10), (float) MathUtils.random(10, 60), (float) MathUtils.random(150, 500), (float) MathUtils.random(minDuration, maxDuration), Interpolation.sine));
        }

        for (int i = 0; i < shapesAmount-1; i++) {
            for (int j = 0; j < shapesAmount-i-1; j++) {
                if (shapeModels.get(j).getDuration() < shapeModels.get(j+1).getDuration()) {
                    ShapeModel tmpShapeModel = shapeModels.get(j);
                    shapeModels.set(j, shapeModels.get(j+1));
                    shapeModels.set(j+1, tmpShapeModel);
                }
            }
        }
        int a = 0;

        boolean intersectsExist = true;
        while (intersectsExist) {
            intersectsExist = false;
            for (int i = 0; i < shapesAmount - 1; i++) {
                for (int j = i + 1; j < shapesAmount; j++) {
                    float tmpVertices2[] = shapeModels.get(j).getTransformedVertices();
                    for (int k = 0; k < tmpVertices2.length; k += 2) {

                        if (shapeModels.get(i).contains(tmpVertices2[k], tmpVertices2[i + 2])) {
                            shapeModels.get(i).setStartControlPoints(shapeModels.get(i).nextStartControlPoints());
                            intersectsExist = true;
                            a++;
                            System.out.println(a);
                        }
                    }
                }

                //    if (polygonIntersect(shapeModels.get(i).getStartControlPoints(), shapeModels.get(j).getStartControlPoints())) {
                //        shapeModels.get(i).setStartControlPoints(shapeModels.get(i).nextStartControlPoints());
                //        intersectsExist = true;
                //        break;
                //    }
                //    a++;
                //}
                //if (intersectsExist) {
                //    break;
                //}
            }
        }
        System.out.println(a);
    }

    public void update(float deltaTime) {


        // Blobs lifecycle
        // ...


        // The of transition of blobs
        // ...
        for (int i = 0; i < shapesAmount; i++) {
            if (shapeModels.get(i).isTransitionOver()) {
                //...

                shapeModels.get(i).setTargetControlPoints(shapeModels.get(i).nextTargetControlPoints());
            }

            shapeModels.get(i).update(deltaTime);
        }
        //....
    }

    // polygon1 – самый быстрый из двух.
    private boolean polygonIntersect(Vector2[] controlPoints1, Vector2[] controlPoints2) {
        int sides1 = controlPoints1.length;
        int sides2 = controlPoints2.length;

            for (int j = 0; j < sides1; j++) {
                Vector2 p1 = controlPoints1[j];
                Vector2 p2 = controlPoints1[(j + 1) % sides1];
                for (int k = 0; k < sides2; k++) {
                    Vector2 q1 = controlPoints2[k];
                    Vector2 q2 = controlPoints2[(k + 1) % sides2];
                    if (Intersections.segmentsIntersect(p1, p2, q1, q2)) {
                         return true;
                    }
                }
            }
        return false;
    }

    public Array<ShapeModel> getShapeModels() {
        return shapeModels;
    }

    public void dispose() {
        for (int i = 0; i < shapesAmount; i++) {
            shapeModels.get(i).dispose();
        }
    }


    private static class Intersections {
        private static int STEPS = 3;

        public static boolean segmentsIntersect(Vector2 p1, Vector2 q1, Vector2 p2, Vector2 q2) {
            int o1 = orientation(p1, q1, p2);
            int o2 = orientation(p1, q1, q2);
            int o3 = orientation(p2, q2, p1);
            int o4 = orientation(p2, q2, q1);

            if (o1 != o2 && o3 != o4) {
                return true;
            }

            if (o1 == 0 && onSegment(p1, p2, q1)) {
                return true;
            }

            if (o2 == 0 && onSegment(p1, q2, q1)) {
                return true;
            }

            if (o3 == 0 && onSegment(p2, p1, q2)) {
                return true;
            }

            if (o4 == 0 && onSegment(p2, q1, q2)) {
                return true;
            }

            return false;
        }

        public static int orientation(Vector2 p, Vector2 q, Vector2 r) {
            float val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

            if (val == 0) {
                return 0;
            }

            return (val > 0) ? 1 : 2;
        }

        public static boolean onSegment(Vector2 p, Vector2 q, Vector2 r) {
            if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)) {
                return true;
            }

            return false;
        }

    }

    //Следует отталкиваться не от достаточности, а от необходимости
}
