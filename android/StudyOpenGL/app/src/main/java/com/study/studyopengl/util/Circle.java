package com.study.studyopengl.util;

/**
 * Created by Administrator on 2017/12/9.
 */
public class Circle {
    public final Point center;
    public final float radius;

    public Circle(Point center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public Circle scale(float scale) {
        return new Circle(center, radius * scale);
    }
}
