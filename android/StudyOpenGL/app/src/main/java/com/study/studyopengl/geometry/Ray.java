package com.study.studyopengl.geometry;

/**
 * Created by Administrator on 2018/1/5.
 */
public class Ray {
    public final Position point;
    public final Vector vector;

    public Ray(Position point, Vector vector) {
        this.point = point;
        this.vector = vector;
    }
}
