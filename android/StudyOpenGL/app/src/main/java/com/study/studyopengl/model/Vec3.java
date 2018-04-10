package com.study.studyopengl.model;

import com.study.studyopengl.geometry.Vector;

/**
 * Created by Felix on 2018/4/7 22:57
 */

public class Vec3 {
    public Vec3() {
    }

    public Vec3(float arg1, float arg2, float arg3) {
        this.x = arg1;
        this.y = arg2;
        this.z = arg3;
        this.r = arg1;
        this.g = arg2;
        this.b = arg3;
        this.s = arg1;
        this.t = arg2;
    }

    public float x, y, z, s, t, r, g, b;

    public float length() {
        return (float) Math.sqrt(
                x * x
                        + y * y
                        + z * z);
    }

    public Vec3 crossProduct(Vec3 other) {
        return new Vec3(
                (y * other.z) - (z * other.y),
                (z * other.x) - (x * other.z),
                (x * other.y) - (y * other.x));
    }

    public float dotProduct(Vec3 other) {
        return x * other.x
                + y * other.y
                + z * other.z;
    }

    public Vec3 normalize() {
        float length = length();
        return new Vec3(this.x / length, this.y / length, this.z / length);
    }
}
