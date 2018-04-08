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
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform3fv;
import static android.opengl.GLES20.glUniformMatrix4fv;


public class EarthProgram extends ShaderProgram {
    // Uniform locations
    private static final String U_TEXTURE_UNIT_DAY = "u_TextureUnit_Day";
    private static final String U_TEXTURE_UNIT_NIGHT = "u_TextureUnit_Night";

    private final int uMatrixLocation;
    private final int uTextureDayUnitLocation;
    private final int uTextureNightUnitLocation;
    private final int uLightPositionLocation;
    private final int uCameraLocation;
    private final int uModelMatrixLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;


    public EarthProgram(Context context) {
        super(context, R.raw.earth_vs, R.raw.earth_fs);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureDayUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT_DAY);
        uTextureNightUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT_NIGHT);
        uLightPositionLocation = glGetUniformLocation(program, U_LIGHT_POSITION);
        uCameraLocation = glGetUniformLocation(program, U_CAMERA);
        uModelMatrixLocation = glGetUniformLocation(program, U_MATRIX_MODEL);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, float[] mMatrix, float[] camera, float[] light, int textureDay, int textureNight) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniformMatrix4fv(uModelMatrixLocation, 1, false, mMatrix, 0);

        glUniform3fv(uCameraLocation,1,camera,0);
        glUniform3fv(uLightPositionLocation,1,light,0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureDay);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, textureNight);
        glUniform1i(uTextureDayUnitLocation, 0);
        glUniform1i(uTextureNightUnitLocation, 1);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }


}
