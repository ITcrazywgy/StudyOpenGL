package com.study.studyopengl.objects;

import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.programs.PyramidProgram;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static com.study.studyopengl.Constants.BYTES_PER_FLOAT;

public class Pyramid {

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X, Y,Z,  S,T
            0f, 1f, 0f, 0.5f, 0f,      //  top
            -1f, -1f, 1f, 0f, 1f,        //  front-left
            1f, -1f, 1f, 1f, 1f,        //  front-right

            0f, 1f, 0f, 0.5f, 0f,       //   top
            1f, -1f, 1f, 0f, 1f,        //  front-right
            1f, -1f, -1f, 1f, 1f,         //  back-right

            0f, 1f, 0f, 0.5f, 0f,       //   top
            1f, -1f, -1f, 0f, 1f,         //  back-right
            -1f, -1f, -1f, 1f, 1f,         //  back-left

            0f, 1f, 0f, 0.5f, 0f,       //   top
            -1f, -1f, -1f, 0f, 1f,         //  back-left
            -1f, -1f, 1f, 1f, 1f,        //   front-left



            1f, -1f, -1f, 1f, 0f,         //  back-right
            -1f, -1f, -1f, 0f, 0f,         //  back-left
            -1f, -1f, 1f, 0f, 1f,        //   front-left

            -1f, -1f, 1f, 0f, 1f,        //  front-left
            1f, -1f, 1f, 1f, 1f,        //  front-right
            1f, -1f, -1f, 1f, 0f         //  back-right
    };


    private final VertexArray vertexArray;

    public Pyramid() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(PyramidProgram colorProgram) {
        vertexArray.setVertexAttributePointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                colorProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_TRIANGLES,0,VERTEX_DATA.length / TOTAL_COMPONENT_COUNT);
    }
}
