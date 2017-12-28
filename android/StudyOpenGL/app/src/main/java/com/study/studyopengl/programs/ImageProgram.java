/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.study.studyopengl.programs;

import android.content.Context;

import com.study.studyopengl.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform2fv;
import static android.opengl.GLES20.glUniform3fv;
import static android.opengl.GLES20.glUniformMatrix4fv;


public class ImageProgram extends ShaderProgram {
    protected static final String U_CHANGETYPE = "u_ChangeType";
    protected static final String U_CHANGEDATA = "u_ChangeData";
    protected static final String U_IMAGESIZE = "u_ImageSize";
    // Uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;
    private final int uChangeTypeLocation;
    private final int uImageSizeLocation;
    private final int uChangeDataLocation;


    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public ImageProgram(Context context) {
        super(context, R.raw.image_vs, R.raw.image_fs);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        uChangeTypeLocation = glGetUniformLocation(program, U_CHANGETYPE);
        uChangeDataLocation = glGetUniformLocation(program, U_CHANGEDATA);
        uImageSizeLocation = glGetUniformLocation(program, U_IMAGESIZE);


        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);

    }

    private final float[][] IMAGE_CHANGE_DATA = new float[][]{
            {0.0f, 0.0f, 0.0f}//原图
            , {0.299f, 0.587f, 0.114f}//灰度
            , {0.1f, 0.1f, 0.0f}//暖色调
            , {0.0f, 0.0f, 0.1f}//冷色调
            , {0.5f, 0.5f, 0.5f}//浮雕 （差值+灰度）
            , {0f, 0f, 0f}//图像对比度增强
            , {0.5f, 0.5f, 2f}// 放大镜  （以纹理中心放大2倍）
            , {0f, 0f, 10f}//马赛克
            , {0f, 0f, 0f}//图像扭曲
            , {0f, 0f, 0f}//图像颠倒
            , {0f, 0f, 10f}//马赛克
            , {0f, 0f, 0f}//图像扭曲
            , {0f, 0f, 0f}//图像颠倒
            , {0f, 0f, 0f}//图像颠倒
    };

    public void setUniforms(float[] matrix, int textureId, float[] imageSize, int changeType) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform1i(uChangeTypeLocation, changeType);
        glUniform3fv(uChangeDataLocation, 1, IMAGE_CHANGE_DATA[changeType], 0);
        glUniform2fv(uImageSizeLocation, 1, imageSize, 0);


        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
}
