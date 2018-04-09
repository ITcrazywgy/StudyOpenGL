package com.study.studyopengl.model;

import com.study.studyopengl.geometry.Position;
import com.study.studyopengl.geometry.Vector;

/**
 * Created by Felix on 2018/4/9 22:01
 */

public class VectorHelper {
    public static Vec3 vectorBetween(Vec3 from, Vec3 to) {
        return new Vec3(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z);
    }
}
