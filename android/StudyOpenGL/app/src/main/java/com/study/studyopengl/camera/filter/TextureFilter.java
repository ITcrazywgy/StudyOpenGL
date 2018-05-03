package com.study.studyopengl.camera.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.study.studyopengl.camera.program.TextureFilterProgram;
import com.study.studyopengl.util.TextureHelper;

/**
 * Created by Felix on 2018/5/1 17:50
 */

public class TextureFilter extends AbsFilter {

    private TextureFilterProgram mProgram;
    private int[] mFramebufferIds;
    private int[] mTextureIds;
    private int mWidth;
    private int mHeight;
    private float[] mMatrix;


    public void setMatrix(float[] matrix) {
        this.mMatrix = matrix;
    }

    @Override
    public void onSurfaceCreated(Context context) {
        mProgram = new TextureFilterProgram(context);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        deleteBuffers();
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

    public void bindData() {
        mVertexArray.setVertexAttributePointer(
                0,
                mProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        mVertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                mProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    @Override
    public void onDrawFrame(int inputTextureId) {
        boolean isDepthTestEnable = GLES20.glIsEnabled(GLES20.GL_DEPTH_TEST);
        if (isDepthTestEnable) {
            GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        }
        GLES20.glViewport(0, 0, mWidth, mHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebufferIds[0]);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mProgram.useProgram();
        mProgram.setUniforms(mMatrix, inputTextureId);
        bindData();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mVertexArray.capacity() / TOTAL_COMPONENT_COUNT);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        if (isDepthTestEnable) {
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }
    }

    private void deleteBuffers() {
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

}
