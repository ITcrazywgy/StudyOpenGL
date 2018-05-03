package com.study.studyopengl.camera.program;

import android.content.Context;
import android.opengl.GLES11Ext;

import com.study.studyopengl.R;
import com.study.studyopengl.programs.ShaderProgram;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Felix on 2018/5/1 18:18
 */

public class LookupFilterProgram extends ShaderProgram {

    private static final String U_TEXTURE_UNIT_MASK = "u_TextureUnit_Mask";
    private static final String U_INTENSITY = "u_Intensity";
    int uMatrixLocation;
    int uTextureUnitLocation;
    int uTextureUnitMaskLocation;
    int uIntensityLocation;

    int aPositionLocation;
    int aTextureCoordinatesLocation;


    public LookupFilterProgram(Context context) {
        super(context, R.raw.filter_base_vs, R.raw.filter_lookup_fs);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        uTextureUnitMaskLocation = glGetUniformLocation(program, U_TEXTURE_UNIT_MASK);
        uIntensityLocation = glGetUniformLocation(program, U_INTENSITY);

        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }

    public void setUniforms(float[] matrix, int textureId, int maskTextureId,float intensity) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform1f(uIntensityLocation, intensity);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, maskTextureId);
        glUniform1i(uTextureUnitMaskLocation, 1);
    }


}
