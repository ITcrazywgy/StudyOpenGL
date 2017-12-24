package com.study.studyopengl.renderer;

import android.content.Context;

import com.study.studyopengl.objects.Cube;
import com.study.studyopengl.programs.CubeProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;


public class CubeRenderer extends BaseRenderer {
    private final Context context;
    private CubeProgram program;
    private Cube cube;

    public CubeRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        cube = new Cube();
        program = new CubeProgram(context);
        glEnable(GL_DEPTH_TEST);
    }


    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);
        float r = (float) width / (float) height;
        frustumM(-r, r, -1f, 1f, 1f, 10f);
        setLookAtM(0, 0, 3,
                0, 0, 0,
                0, 1, 0);
    }

    private int a = 0;

    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        program.useProgram();

        setIdentityM(getModelMatrix());
        translateM(0f, 0f, -2.5f);
        rotateM((a += 3) % 360, 1f, 1f, 1f);


        program.setUniforms(getMvpMatrix());
        cube.bindData(program);
        cube.draw();
    }

}