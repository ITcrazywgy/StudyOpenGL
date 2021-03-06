package com.study.studyopengl.renderer;

import android.content.Context;

import com.study.studyopengl.R;
import com.study.studyopengl.objects.Earth;
import com.study.studyopengl.programs.EarthProgram;
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


public class EarthRenderer extends BaseRenderer {
    private final Context context;

    private EarthProgram program;
    private Earth earth;
    private int textureDay;
    private int textureNight;

    private float[] lightPosition;

    public EarthRenderer(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        program = new EarthProgram(context);
        earth = new Earth();
        textureDay = TextureHelper.loadTexture(context, R.drawable.earth_day);
        textureNight = TextureHelper.loadTexture(context, R.drawable.earth_night);
    }


    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);
        float r = (float) width / (float) height;
        orthoM(-1f, 1f, -1/r, 1/r, -1f, 1f);
        lightPosition = new float[]{100, 5, 0};
    }


    private float angle;

    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        program.useProgram();

        setIdentityM(getModelMatrix());
        scaleM(0.8f,0.8f,0.8f);
        rotateM((angle += 1), 0f, 1f, 0f);

        program.setUniforms(getMvpMatrix(), getModelMatrix(), getCameraPosition(), lightPosition, textureDay, textureNight);
        earth.bindData(program);
        earth.draw();
    }

}