package com.study.studyopengl.objects;

import com.study.studyopengl.data.IndexArray;
import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.programs.CubeProgram;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;
import static com.study.studyopengl.Constants.BYTES_PER_FLOAT;

public class Cube {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X, Y,Z, R, G, B
            -1, 1, 1, 1, 1, 0,    // (0) Top-left near
            1, 1, 1, 1, 1, 0,    // (1) Top-right near
            -1, -1, 1, 1, 1, 0,     // (2) Bottom-left near
            1, -1, 1, 1, 1, 0,    // (3) Bottom-right near

            -1, 1, -1, 0, 1, 1,     // (4) Top-left far
            1, 1, -1, 0, 1, 1,    // (5) Top-right far
            -1, -1, -1, 0, 1, 1,     // (6) Bottom-left far
            1, -1, -1, 0, 1, 1,     // (7) Bottom-right far
    };

    private static final byte[] INDEX_DATA = {
            // Front
            0, 3, 1,
            2, 3, 0,
            // Back
            5, 6, 4,
            7, 6, 5,
            // Left
            4, 2, 0,
            6, 2, 4,
            // Right
            1, 7, 5,
            3, 7, 1,
            // Top
            4, 1, 5,
            0, 1, 4,
            // Bottom
            7, 2, 6,
            3, 2, 7
    };


    private final VertexArray vertexArray;
    private final IndexArray indexArray;

    public Cube() {
        vertexArray = new VertexArray(VERTEX_DATA);
        indexArray = new IndexArray(INDEX_DATA);
    }

    public void bindData(CubeProgram cubeProgram) {
        vertexArray.setVertexAttributePointer(
                0,
                cubeProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                cubeProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, indexArray.getIndexBuffer());
    }
}
