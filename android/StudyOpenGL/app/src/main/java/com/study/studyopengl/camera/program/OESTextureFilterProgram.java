package com.study.studyopengl.camera.program;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.study.studyopengl.R;
import com.study.studyopengl.programs.ShaderProgram;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by Felix on 2018/5/1 18:18
 */

public class OESTextureFilterProgram extends ShaderProgram {

    private static final String U_SAMPLE_EXTERNAL_OES = "u_SamplerExternalOES";
    int uMatrixLocation;
    int uSampleExternalOESLocation;
    int aPositionLocation;
    int aTextureCoordinatesLocation;


    public OESTextureFilterProgram(Context context) {
        super(context, R.raw.filter_vs, R.raw.filter_oes_fs);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uSampleExternalOESLocation = glGetUniformLocation(program, U_SAMPLE_EXTERNAL_OES);

        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }

    public void setUniforms(float[] matrix, int textureId) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        glUniform1i(uSampleExternalOESLocation, 0);
    }


}
