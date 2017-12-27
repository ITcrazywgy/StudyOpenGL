package com.study.studyopengl.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.study.studyopengl.objects.Ball;
import com.study.studyopengl.programs.BallProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;


public class BallRenderer extends BaseRenderer{
    private final Context context;
    private BallProgram lightProgram;
    private Ball ball;

    public BallRenderer(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        lightProgram = new BallProgram(context);
        ball = new Ball();
        glEnable(GL_DEPTH_TEST);
    }


    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);
        float r = (float) width / (float) height;

        //视图矩阵
        setLookAtM(0.0f, 0.0f, 5.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //透视矩阵
        frustumM( -1f, 1f, -1/r, 1/r, 3.0f, 10f);

    }

    private float angle;

    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        lightProgram.useProgram();

        setIdentityM(getModelMatrix());
        rotateM((angle+=0.5),1f,1f,1f);

        lightProgram.setUniforms(getMvpMatrix());
        ball.bindData(lightProgram);
        ball.draw();
    }

}