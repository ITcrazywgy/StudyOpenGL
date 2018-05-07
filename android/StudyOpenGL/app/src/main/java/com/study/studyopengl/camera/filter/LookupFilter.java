package com.study.studyopengl.camera.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.study.studyopengl.R;
import com.study.studyopengl.camera.program.LookupFilterProgram;
import com.study.studyopengl.util.TextureHelper;


public class LookupFilter extends AbsFilter {

    private LookupFilterProgram mProgram;
    private int mWidth;
    private int mHeight;
    private int mMaskTextureId;

    public LookupFilter() {
        super();
    }

    public LookupFilter(int flipMode) {
        super(flipMode);
    }

    @Override
    public void onSurfaceCreated(Context context) {
        mProgram = new LookupFilterProgram(context);
        mMaskTextureId = TextureHelper.loadTexture(context, R.drawable.purity);
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

        // GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebufferIds[0]);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mProgram.useProgram();
        mProgram.setUniforms(getMatrix(), inputTextureId, mMaskTextureId, 0.5f);
        bindVertexArray(mProgram.getPositionAttributeLocation(), mProgram.getTextureCoordinatesAttributeLocation());
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, getVertexCount());
        // GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

    }

}
