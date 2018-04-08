package com.study.studyopengl.renderer;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import static android.opengl.Matrix.multiplyMM;

public abstract class BaseRenderer implements GLSurfaceView.Renderer {

    private final float[] projectMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    // mpvMatrix = projectMatrix * viewMatrix * modelMatrix
    private final float[] mvpMatrix = new float[16];
    private final float[] normalMatrix = new float[16];


    protected BaseRenderer() {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setIdentityM(projectMatrix, 0);
    }

    //透视矩阵
    protected void frustumM(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(projectMatrix, 0, left, right, bottom, top, near, far);
    }

    //正交矩阵
    protected void orthoM(float left, float right, float bottom, float top, float near, float far) {
        //透视矩阵
        Matrix.orthoM(projectMatrix, 0, left, right, bottom, top, near, far);
    }

    float[] cameraPosition = new float[3];//摄像机位置

    protected void setLookAtM(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
        //视图矩阵
        Matrix.setLookAtM(viewMatrix, 0,
                eyeX, eyeY, eyeZ
                , centerX, centerY, centerZ
                , upX, upY, upZ);

        cameraPosition[0] = eyeX;
        cameraPosition[1] = eyeY;
        cameraPosition[2] = eyeZ;
    }

    public float[] getCameraPosition() {
        return cameraPosition;
    }

    protected void setIdentityM(float[] sm) {
        //模型矩阵
        Matrix.setIdentityM(sm, 0);
    }

    protected void translateM(float x, float y, float z) {
        Matrix.translateM(modelMatrix, 0, x, y, z);
    }

    protected void rotateM(float a, float x, float y, float z) {
        Matrix.rotateM(modelMatrix, 0, a, x, y, z);
    }

    protected void scaleM(float x, float y, float z) {
        Matrix.scaleM(modelMatrix, 0, x, y, z);
    }


    protected float[] getProjectMatrix() {
        return projectMatrix;
    }

    protected float[] getModelMatrix() {
        return modelMatrix;
    }

    protected float[] getViewMatrix() {
        return viewMatrix;
    }

    protected float[] getMvpMatrix() {
        final float[] temp = new float[16];
        multiplyMM(temp, 0, viewMatrix, 0, modelMatrix, 0);
        multiplyMM(mvpMatrix, 0, projectMatrix, 0, temp, 0);
        return mvpMatrix;
    }

    public float[] getNormalMatrix() {
        final float[] temp = new float[16];
        Matrix.invertM(temp, 0, modelMatrix, 0);
        Matrix.transposeM(normalMatrix, 0, temp, 0);
        return normalMatrix;
    }
}
