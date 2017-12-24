
package com.study.studyopengl.objects;

import android.opengl.GLES20;

import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.programs.EarthProgram;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.study.studyopengl.Constants.BYTES_PER_FLOAT;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class Earth {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COMPONENT_COUNT = 2;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + TEXTURE_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private final VertexArray vertexArray;
    private final float[] ballVertex;

    public Earth() {
        ballVertex = createBallVertex();
        vertexArray = new VertexArray(ballVertex);
    }

    private float[] createBallVertex() {
        int uCount = 180, vCount = 90;
        // x =   r * sin(pi * v) * cos(2 * pi * u)
        // y =   r * cos(pi * v)
        // z = - r * sin(pi * v) * sin(2 * pi * u)
        float v, v2, u;
        List<Float> list = new ArrayList<>();
        for (float i = 0; i < vCount; i++) {
            v = i / vCount;
            v2 = (i + 1) / vCount;
            for (float j = 0; j <= uCount; j++) {
                u = j / uCount;

                float x = (float) (sin(PI * v) * cos(2 * PI * u));
                float y = (float) cos(PI * v);
                float z = -(float) (sin(PI * v) * sin(2 * PI * u));
                list.add(x);
                list.add(y);
                list.add(z);

                // s t
                list.add(u);
                list.add(v);

                float x2 = (float) (sin(PI * v2) * cos(2 * PI * u));
                float y2 = (float) cos(PI * v2);
                float z2 = -(float) (sin(PI * v2) * sin(2 * PI * u));
                list.add(x2);
                list.add(y2);
                list.add(z2);

                list.add(u);
                list.add(v2);
            }
        }
        float ver[] = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ver[i] = list.get(i);
        }
        return ver;
    }

    public void bindData(EarthProgram earthProgram) {
        vertexArray.setVertexAttributePointer(
                0,
                earthProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        vertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                earthProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, ballVertex.length / TOTAL_COMPONENT_COUNT);
    }
}
