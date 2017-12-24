package com.study.studyopengl.renderer;

import android.content.Context;

import com.study.studyopengl.R;
import com.study.studyopengl.objects.Pyramid;
import com.study.studyopengl.programs.PyramidProgram;
import com.study.studyopengl.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;


public class PyramidRenderer extends BaseRenderer {
    private final Context context;

    private PyramidProgram program;
    private Pyramid pyramid;
    private int texture;

    public PyramidRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        program = new PyramidProgram(context);
        pyramid = new Pyramid();
        texture = TextureHelper.loadTexture(context, R.drawable.wall);
    }


    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);
        float r = (float) width / (float) height;
        setLookAtM(0.0f, 0.0f, 3f
                , 0f, 0f, 0f
                , 0f, 1.0f, 0.0f);
        frustumM(-r, r, -1f, 1f, 1f, 10f);
    }

    private float angle;

    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        program.useProgram();

        setIdentityM(getModelMatrix());
        translateM(0f, 0f, -1f);
        rotateM((angle += 1), 0f, 1f, 0f);

        program.setUniforms(getMvpMatrix(), texture);
        pyramid.bindData(program);
        pyramid.draw();
    }

}