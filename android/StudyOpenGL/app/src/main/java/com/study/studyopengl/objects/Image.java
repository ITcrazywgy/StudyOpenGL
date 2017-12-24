package com.study.studyopengl.objects;

import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.programs.ImageProgram;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static com.study.studyopengl.Constants.BYTES_PER_FLOAT;

public class Image {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X,Y S,T
            -1.0f, 1.0f, 0.0f, 0.0f,
            -1.0f, -1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 1.0f, 1.0f
    };

    private final VertexArray vertexArray;

    public Image() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ImageProgram program) {
        vertexArray.setVertexAttributePointer(
                0,
                program.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                program.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_TRIANGLE_STRIP, 0, VERTEX_DATA.length / TOTAL_COMPONENT_COUNT);
    }

}
