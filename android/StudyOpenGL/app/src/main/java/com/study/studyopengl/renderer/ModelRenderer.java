package com.study.studyopengl.renderer;

import android.content.Context;

import com.study.studyopengl.model.Model;
import com.study.studyopengl.model.ModelProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;


public class ModelRenderer extends BaseRenderer {
    private final Context context;

    private Model model;
    private ModelProgram program;

    public ModelRenderer(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        program = new ModelProgram(context);
        model = new Model(context, "dragon.obj");
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);

        //视图矩阵
        setLookAtM(20, 20, 20, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //透视矩阵
        float r = (float) width / (float) height;
        frustumM(-1f, 1f, -1 / r, 1 / r, 2, 50);
    }

    private int angel = -90;

    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        program.useProgram();
        setIdentityM(getModelMatrix());

        rotateM(-90, 1f, 0f, 0f);
        rotateM(angel += 1, 0f, 0f, 1f);
        scaleM(0.8f, 0.8f, 0.8f);

        program.setMatrix(getMvpMatrix(), getModelMatrix(),getNormalMatrix());
        program.setLightPosition(0, 0, 20);
        model.draw(program);
    }

}