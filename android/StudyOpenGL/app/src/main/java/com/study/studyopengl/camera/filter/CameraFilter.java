package com.study.studyopengl.camera.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.study.studyopengl.camera.program.OESTextureFilterProgram;
import com.study.studyopengl.util.MatrixHelper;
import com.study.studyopengl.util.TextureHelper;

/**
 * Created by Felix on 2018/5/1 17:50
 */

public class CameraFilter extends AbsFilter {

    private OESTextureFilterProgram mProgram;
    private int[] mFramebufferIds;
    private int[] mOutputTextureIds;
    private int mWidth;
    private int mHeight;
    private float[] mFitCenterMatrix;

    @Override
    public void onSurfaceCreated(Context context) {
        mProgram = new OESTextureFilterProgram(context);
    }

    @Override
    public void onSurfaceChanged(int width, int height, int previewWidth, int previewHeight) {
        this.mWidth = width;
        this.mHeight = height;
        destroyFrameBuffers();
        mFramebufferIds = new int[1];
        mOutputTextureIds = new int[1];
        GLES20.glGenFramebuffers(1, mFramebufferIds, 0);
        GLES20.glGenTextures(1, mOutputTextureIds, 0);
        TextureHelper.createEmptyTexture(mOutputTextureIds[0], width, height, GLES20.GL_RGBA);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebufferIds[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mOutputTextureIds[0], 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        mFitCenterMatrix = MatrixHelper.getFitCenterMatrix(width, height, previewWidth, previewHeight);
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
        //GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebufferIds[0]);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mProgram.useProgram();
        mProgram.setUniforms(mFitCenterMatrix, inputTextureId);
        bindData();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mVertexArray.capacity() / TOTAL_COMPONENT_COUNT);
        //GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        if (isDepthTestEnable) {
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }
    }

    private void destroyFrameBuffers() {
        if (mOutputTextureIds != null) {
            GLES20.glDeleteTextures(mOutputTextureIds.length, mOutputTextureIds, 0);
            mOutputTextureIds = null;
        }
        if (mFramebufferIds != null) {
            GLES20.glDeleteFramebuffers(mFramebufferIds.length, mFramebufferIds, 0);
            mFramebufferIds = null;
        }
    }

    @Override
    public void onSurfaceDestroy() {
        destroyFrameBuffers();
    }

}
