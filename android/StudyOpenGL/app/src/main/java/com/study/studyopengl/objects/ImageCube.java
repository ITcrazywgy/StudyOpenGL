
package com.study.studyopengl.objects;

import com.study.studyopengl.data.IndexArray;
import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.programs.CubeProgram;
import com.study.studyopengl.programs.ImageCubeProgram;
import com.study.studyopengl.programs.PyramidProgram;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;
import static com.study.studyopengl.Constants.BYTES_PER_FLOAT;


public class ImageCube {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = {
            // Order of coordinates: X, Y,Z, S,T
            -1.0f, 1.34f, 1.0f, 0.0f, 0.0f,         // (0) Top-left near
            1.0f, 1.34f, 1.0f, 1.0f, 0.0f,         // (1) Top-right near
            -1.0f, -1.34f, 1.0f, 0.0f, 1.0f,         // (2) Bottom-left near
            1.0f, -1.34f, 1.0f, 1.0f, 1.0f,         // (3) Bottom-right near

            -1.0f, 1.34f, -1.0f, 1.0f, 0.0f,         // (4) Top-left far
            1.0f, 1.34f, -1.0f, 0.0f, 0.0f,         // (5) Top-right far
            -1.0f, -1.34f, -1.0f, 1.0f, 1.0f,        // (6) Bottom-left far
            1.0f, -1.34f, -1.0f, 0.0f, 1.0f,         // (7) Bottom-right far


            -1.0f, -1.34f, 1.0f, 1.0f, 0.0f,         // (8) Bottom-left near
            1.0f, -1.34f, 1.0f,  0.0f, 0.0f,         // (9) Bottom-right near
            -1.0f, 1.34f, -1.0f, 0.0f, 0.75f,         // (10) Top-left far
            1.0f, 1.34f, -1.0f,  1.0f, 0.75f         // (11) Top-right far
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


            // Bottom
            6,7,8,
            8,7,9,

            // Top
            0,1,10,
            10, 1, 11

    };


    private final VertexArray vertexArray;
    private final IndexArray indexArray;

    public ImageCube() {
        vertexArray = new VertexArray(VERTEX_DATA);
        indexArray = new IndexArray(INDEX_DATA);
    }

    public void bindData(ImageCubeProgram program) {
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
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, indexArray.getIndexBuffer());
    }
}
