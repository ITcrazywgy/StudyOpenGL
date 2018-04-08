package com.study.studyopengl.geometry;

/**
 * Created by Administrator on 2018/1/5.
 */
public class Circle {
    public final Position center;
    public final float radius;

    public Circle(Position center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public Circle scale(float scale) {
        return new Circle(center, radius * scale);
    }
}
