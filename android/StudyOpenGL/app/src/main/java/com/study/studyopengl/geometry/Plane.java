package com.study.studyopengl.geometry;

/**
 * Created by Administrator on 2018/1/5.
 */
public class Plane {
    public final Position point;
    public final Vector normal;

    public Plane(Position point, Vector normal) {
        this.point = point;
        this.normal = normal;
    }
}
