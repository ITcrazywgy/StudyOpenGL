/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.study.studyopengl.data;

import com.study.studyopengl.util.Geometry.Point;
import com.study.studyopengl.util.Geometry.TextureCoordinates;
import com.study.studyopengl.util.Geometry.Vector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.study.studyopengl.Constants.BYTES_PER_FLOAT;

public class VertexArray {
    private final FloatBuffer floatBuffer;

    public VertexArray(float[] vertexData) {
        floatBuffer = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        floatBuffer.position(0);
    }

    public void setVertexAttributePointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer);
        glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);
    }


    public VertexArray(List<PTNVertex> vertexData) {
        floatBuffer = ByteBuffer
                .allocateDirect(vertexData.size() * PTNVertex.TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        float[] vertex = new float[ PTNVertex.TOTAL_COMPONENT_COUNT];
        for (int i = 0; i < vertexData.size(); i++) {
            Point position = vertexData.get(i).getPosition();
            TextureCoordinates textureCoordinates = vertexData.get(i).getTextureCoordinates();
            Vector normal = vertexData.get(i).getNormal();
            vertex[0] = position.x;
            vertex[1] = position.y;
            vertex[2] = position.z;
            vertex[3] = textureCoordinates.s;
            vertex[4] = textureCoordinates.t;
            vertex[5] = normal.x;
            vertex[6] = normal.y;
            vertex[7] = normal.z;
            floatBuffer.put(vertex);
        }
        floatBuffer.position(0);
    }

    public static class PTNVertex {
        public static final int POSITION_COMPONENT_COUNT = 3;
        public static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
        public static final int NORMAL_COMPONENT_COUNT = 3;
        public static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT;

        private Point position;
        private TextureCoordinates textureCoordinates;
        private Vector normal;

        public Point getPosition() {
            return position;
        }

        public void setPosition(Point position) {
            this.position = position;
        }

        public TextureCoordinates getTextureCoordinates() {
            return textureCoordinates;
        }

        public void setTextureCoordinates(TextureCoordinates textureCoordinates) {
            this.textureCoordinates = textureCoordinates;
        }

        public Vector getNormal() {
            return normal;
        }

        public void setNormal(Vector normal) {
            this.normal = normal;
        }
    }

}
