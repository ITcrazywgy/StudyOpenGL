package com.study.studyopengl.camera.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.study.studyopengl.camera.program.TextureFilterProgram;

/**
 * Created by Felix on 2018/5/1 17:50
 */

public class TextureFilter extends AbsFilter {

    private TextureFilterProgram mProgram;
    private int mWidth;
    private int mHeight;

    public TextureFilter() {
        super();
    }

    public TextureFilter(int flipMode) {
        super(flipMode);
    }

    @Override
    public void onSurfaceCreated(Context context) {
        mProgram = new TextureFilterProgram(context);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        generateFrameBuffer(width, height);
    }


    @Override
    public void onDrawFrame(int inputTextureId) {
        GLES20.glViewport(0, 0, mWidth, mHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, getFrameBufferId());
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mProgram.useProgram();
        mProgram.setUniforms(getMatrix(), inputTextureId);
        bindVertexArray(mProgram.getPositionAttributeLocation(), mProgram.getTextureCoordinatesAttributeLocation());
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, getVertexCount());
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

}
