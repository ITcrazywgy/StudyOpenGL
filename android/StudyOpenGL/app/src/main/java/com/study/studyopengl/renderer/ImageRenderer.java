package com.study.studyopengl.renderer;

import android.content.Context;
import android.opengl.Matrix;

import com.study.studyopengl.R;
import com.study.studyopengl.objects.Image;
import com.study.studyopengl.programs.ImageProgram;
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


public class ImageRenderer extends BaseRenderer {
    private final Context context;

    private ImageProgram program;
    private Image image;
    private int texture;
    private int bitmapWidth;
    private int bitmapHeight;


    public ImageRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0f, 0f, 0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        program = new ImageProgram(context);
        image = new Image();
        int[] bitmapWH = new int[2];
        texture = TextureHelper.loadTexture(context, R.drawable.girl, bitmapWH);
        bitmapWidth = bitmapWH[0];
        bitmapHeight = bitmapWH[1];
    }


    private float[] imageSize;

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);
        float viewRatio = (float) width / (float) height;
        float imageRatio = (float) bitmapWidth / (float) bitmapHeight;
        imageSize = new float[2];
        if (imageRatio > viewRatio) { //图片比较矮肥
            orthoM(-1, 1, -imageRatio / viewRatio, imageRatio / viewRatio, -1f, 1);
            imageSize[0] = width;
            imageSize[1] = Math.round(imageSize[0] / imageRatio);
        } else { //图片比较高瘦
            orthoM(-viewRatio / imageRatio, viewRatio / imageRatio, -1, 1, -1f, 1);
            imageSize[1] = height;
            imageSize[0] = Math.round(imageSize[1] * imageRatio);
        }
    }


    @Override
    public void onDrawFrame(GL10 glUnused) {
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        program.useProgram();
        program.setUniforms(getProjectMatrix(), texture, imageSize, 8);
        image.bindData(program);
        image.draw();
    }

}