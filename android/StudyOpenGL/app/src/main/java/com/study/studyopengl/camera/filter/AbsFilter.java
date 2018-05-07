package com.study.studyopengl.camera.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.study.studyopengl.data.VertexArray;
import com.study.studyopengl.util.TextureHelper;

import static com.study.studyopengl.Constants.BYTES_PER_FLOAT;

/**
 * Created by Felix on 2018/5/1 17:24
 */

public abstract class AbsFilter {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    public static final int TEXTURE_FLIP_NONE = 0;
    public static final int TEXTURE_FLIP_HORIZONTAL = 1;
    public static final int TEXTURE_FLIP_VERTICAL = 1 << 1;

    public AbsFilter() {
        setFlipMode(TEXTURE_FLIP_NONE);
    }

    public AbsFilter(int flipMode) {
        setFlipMode(flipMode);
    }

    private static final float[] VERTEX_DATA = {
            -1.0f, -1.0f, 0.0f, 0.0f,
            1.0f, -1.0f, 1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f
    };


    private float[] mMatrix;

    private VertexArray mVertexArray;
    private int[] mTextureIds;
    private int[] mFramebufferIds;

    void bindVertexArray(int positionLocation, int textureCoordinatesLocation) {
        mVertexArray.setVertexAttributePointer(
                0,
                positionLocation,
                POSITION_COMPONENT_COUNT,
                STRIDE);
        mVertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                textureCoordinatesLocation,
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    int getVertexCount() {
        return mVertexArray.capacity() / TOTAL_COMPONENT_COUNT;
    }

    private int mCurrentFlipMode = Integer.MIN_VALUE;

    public void setFlipMode(int flipMode) {
        if (this.mCurrentFlipMode != flipMode) {
            if ((flipMode & TEXTURE_FLIP_HORIZONTAL) != 0) {
                VERTEX_DATA[2] = flip(VERTEX_DATA[2]);
                VERTEX_DATA[6] = flip(VERTEX_DATA[6]);
                VERTEX_DATA[10] = flip(VERTEX_DATA[10]);
                VERTEX_DATA[14] = flip(VERTEX_DATA[14]);
            }

            if ((flipMode & TEXTURE_FLIP_VERTICAL) != 0) {
                VERTEX_DATA[3] = flip(VERTEX_DATA[3]);
                VERTEX_DATA[7] = flip(VERTEX_DATA[7]);
                VERTEX_DATA[11] = flip(VERTEX_DATA[11]);
                VERTEX_DATA[15] = flip(VERTEX_DATA[15]);
            }
            mVertexArray = new VertexArray(VERTEX_DATA);
            this.mCurrentFlipMode = flipMode;
        }
    }

    private float flip(float v) {
        return 1.0f - v;
    }

    public void setMatrix(float[] matrix) {
        this.mMatrix = matrix;
    }

    public float[] getMatrix() {
        return this.mMatrix;
    }

    public abstract void onSurfaceCreated(Context context);

    public abstract void onSurfaceChanged(int width, int height);

    protected void generateFrameBuffer(int width, int height) {
        deleteFrameBuffer();
        mFramebufferIds = new int[1];
        mTextureIds = new int[1];
        GLES20.glGenFramebuffers(1, mFramebufferIds, 0);
        GLES20.glGenTextures(1, mTextureIds, 0);
        TextureHelper.createEmptyTexture(mTextureIds[0], width, height, GLES20.GL_RGBA);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebufferIds[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTextureIds[0], 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public abstract void onDrawFrame(int inputTextureId);

    private void deleteFrameBuffer() {
        if (mTextureIds != null) {
            GLES20.glDeleteTextures(mTextureIds.length, mTextureIds, 0);
            mTextureIds = null;
        }
        if (mFramebufferIds != null) {
            GLES20.glDeleteFramebuffers(mFramebufferIds.length, mFramebufferIds, 0);
            mFramebufferIds = null;
        }
    }

    public int getTextureId() {
        return mTextureIds[0];
    }

    protected int getFrameBufferId() {
        return mFramebufferIds[0];
    }

}
