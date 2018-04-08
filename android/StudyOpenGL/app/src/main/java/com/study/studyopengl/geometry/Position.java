package com.study.studyopengl.geometry;

/**
 * Created by Felix on 2018/1/6 00:05
 */
public class Position {
    public final float x, y, z;

    public Position(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position translateY(float distance) {
        return new Position(x, y + distance, z);
    }

    public Position translate(Vector vector) {
        return new Position(
                x + vector.x,
                y + vector.y,
                z + vector.z);
    }
}
