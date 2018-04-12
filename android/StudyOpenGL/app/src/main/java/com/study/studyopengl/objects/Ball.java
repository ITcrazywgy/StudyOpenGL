
package com.study.studyopengl.objects;

import android.opengl.GLES20;

import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.programs.BallProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.study.studyopengl.Constants.BYTES_PER_FLOAT;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class Ball {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private final VertexArray vertexArray;

    public Ball() {
        vertexArray = new VertexArray(createBallVertex());
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

                addColor(list);

                float x2 = (float) (sin(PI * v2) * cos(2 * PI * u));
                float y2 = (float) cos(PI * v2);
                float z2 = -(float) (sin(PI * v2) * sin(2 * PI * u));
                list.add(x2);
                list.add(y2);
                list.add(z2);

                addColor(list);
            }
        }
        float ver[] = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ver[i] = list.get(i);
        }
        return ver;
    }


    private Random random = new Random();

    private void addColor(List<Float> list) {
        int color = random.nextInt(3);
        if (color == 0) {
            list.add(1f);
            list.add(0f);
            list.add(0f);
        } else if (color == 1) {
            list.add(0f);
            list.add(1f);
            list.add(0f);
        } else {
            list.add(0f);
            list.add(0f);
            list.add(1f);
        }
    }


    public void bindData(BallProgram lightProgram) {
        vertexArray.setVertexAttributePointer(
                0,
                lightProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        vertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                lightProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexArray.capacity() / TOTAL_COMPONENT_COUNT);
    }
}
