package com.study.studyopengl.objects;

import android.content.Context;

import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.data.VertexArray.PTNVertex;
import com.study.studyopengl.programs.TeapotProgram;
import com.study.studyopengl.util.ObjectLoader;

import java.util.List;

import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES20.GL_TRIANGLES;
import static com.study.studyopengl.Constants.BYTES_PER_FLOAT;

public class Teapot {

    private static final int STRIDE = PTNVertex.TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;
    private final VertexArray vertexArray;

    private int vertexCount;

    public Teapot(Context context) {
        List<PTNVertex> ptnVertices = ObjectLoader.load(context, "teapot.obj");
        vertexCount = ptnVertices.size();
        vertexArray = new VertexArray(ptnVertices);
    }

    public void bindData(TeapotProgram program) {
        vertexArray.setVertexAttributePointer(
                0,
                program.getPositionAttributeLocation(),
                PTNVertex.POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttributePointer(
                PTNVertex.POSITION_COMPONENT_COUNT,
                program.getTextureCoordinatesAttributeLocation(),
                PTNVertex.TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttributePointer(
                PTNVertex.POSITION_COMPONENT_COUNT + PTNVertex.TEXTURE_COORDINATES_COMPONENT_COUNT,
                program.getNormalLocation(),
                PTNVertex.NORMAL_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
    }
}
