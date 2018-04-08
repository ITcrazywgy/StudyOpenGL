package com.study.studyopengl.geometry;

/**
 * Created by Administrator on 2018/1/5.
 */
public class Vector {
    public final float x, y, z;

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float length() {
        return (float) Math.sqrt(
                x * x
                        + y * y
                        + z * z);
    }

    // http://en.wikipedia.org/wiki/Cross_product
    public Vector crossProduct(Vector other) {
        return new Vector(
                (y * other.z) - (z * other.y),
                (z * other.x) - (x * other.z),
                (x * other.y) - (y * other.x));
    }

    // http://en.wikipedia.org/wiki/Dot_product
    public float dotProduct(Vector other) {
        return x * other.x
                + y * other.y
                + z * other.z;
    }

    public Vector scale(float f) {
        return new Vector(
                x * f,
                y * f,
                z * f);
    }

    public Vector normalize() {
        float length = length();
        return new Vector(this.x / length, this.y / length, this.z / length);
    }


}
