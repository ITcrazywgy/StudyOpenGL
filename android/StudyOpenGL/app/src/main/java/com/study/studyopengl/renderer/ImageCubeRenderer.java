package com.study.studyopengl.renderer;

import android.content.Context;

import com.study.studyopengl.R;
import com.study.studyopengl.objects.ImageCube;
import com.study.studyopengl.objects.Pyramid;
import com.study.studyopengl.programs.ImageCubeProgram;
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


public class ImageCubeRenderer extends BaseRenderer {
    private final Context context;

    private ImageCubeProgram program;
    private ImageCube imageCube;
    private int texture;

    public ImageCubeRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        program = new ImageCubeProgram(context);
        imageCube = new ImageCube();
        texture = TextureHelper.loadTexture(context, R.drawable.girl);
    }


    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);
        float r = (float) width / (float) height;
        setLookAtM(0.0f, 0.0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        frustumM(-r, r, -1f, 1f, 1f, 10f);
    }

    private float angle;
    private boolean isRotateY = true;

    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        program.useProgram();

        setIdentityM(getModelMatrix());
        translateM(0f, 0f, -1f);

        if (isRotateY) {
            rotateM((angle += 1), 0f, 1f, 0f);
        } else {
            rotateM((angle += 1), -1f, 0f, 0f);
        }

        if (angle % 360 == 0) {
            isRotateY = !isRotateY;
        }

        program.setUniforms(getMvpMatrix(), texture);
        imageCube.bindData(program);
        imageCube.draw();
    }

}