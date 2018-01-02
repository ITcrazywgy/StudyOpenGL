package com.study.studyopengl.renderer;

import android.content.Context;

import com.study.studyopengl.R;
import com.study.studyopengl.objects.Earth;
import com.study.studyopengl.objects.Teapot;
import com.study.studyopengl.programs.EarthProgram;
import com.study.studyopengl.programs.TeapotProgram;
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


public class TeapotRenderer extends BaseRenderer {
    private final Context context;

    private TeapotProgram program;
    private Teapot teapot;
    private int texture;

    private float[] lightPosition;

    public TeapotRenderer(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        program = new TeapotProgram(context);
        teapot = new Teapot(context);
        texture = TextureHelper.loadTexture(context, R.drawable.teaport);
    }


    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);
        //视图矩阵
        setLookAtM(0, 0, 30, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        float r = (float) width / (float) height;
        frustumM(-1f, 1f, -1 / r, 1 / r, 2, 100);
        lightPosition = new float[]{100, 5, 0};
    }

    private float angle;

    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        program.useProgram();

        setIdentityM(getModelMatrix());
        //translateM(0, -2f, -50f);
        rotateM(angle += 2, 0f, 1f, 0f);
        program.setUniforms(getMvpMatrix(), getModelMatrix(), getCameraPosition(), lightPosition, texture);
        teapot.bindData(program);
        teapot.draw();
    }

}