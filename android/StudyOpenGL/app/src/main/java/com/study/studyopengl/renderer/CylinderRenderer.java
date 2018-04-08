package com.study.studyopengl.renderer;

import android.content.Context;
import android.util.Log;

import com.study.studyopengl.objects.Cylinder;
import com.study.studyopengl.programs.CylinderProgram;
import com.study.studyopengl.geometry.Position;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;


public class CylinderRenderer extends BaseRenderer {
    private final Context context;
    private CylinderProgram colorProgram;
    private Cylinder cylinder;

    public CylinderRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated:" + Thread.currentThread().getName());
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        cylinder = new Cylinder(new Position(0f, 0f, 0f), 0.5f, 4f, 360);
        colorProgram = new CylinderProgram(context);
        glEnable(GL_DEPTH_TEST);
    }

    private static final String TAG = "EGLRenderer";

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        Log.d(TAG, "onSurfaceChanged:" + Thread.currentThread().getName());
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
        Log.d(TAG, "onDrawFrame:" + Thread.currentThread().getName());
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        colorProgram.useProgram();

        setIdentityM(getModelMatrix());
        translateM(0f, 0f, -2.5f);
        rotateM((a += 3) % 360, 1f, 1f, 1f);

        colorProgram.setUniforms(getMvpMatrix(), 0.8f, 0.8f, 1f);
        cylinder.bindData(colorProgram);
        cylinder.draw();
    }

}