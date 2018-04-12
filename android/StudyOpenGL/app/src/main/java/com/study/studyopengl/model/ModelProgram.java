/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.study.studyopengl.model;

import android.content.Context;

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
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniformMatrix4fv;


public class ModelProgram extends ShaderProgram {
    // Uniform locations
    private final int uMatrixLocation;
    private final int uModelMatrixLocation;
    private final int uNormalMatrixLocation;
    private final int uAmbientTextureUnitLocation;
    private final int uDiffuseTextureUnitLocation;
    private final int uSpecularTextureUnitLocation;
    private final int uLightPositionLocation;
    private final int uLightAmbientLocation;
    private final int uLightDiffuseLocation;
    private final int uLightSpecularLocation;
    private final int uShininessLocation;


    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;
    private final int aNormalLocation;


    public ModelProgram(Context context) {
        super(context, R.raw.model_vs, R.raw.model_fs);
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uModelMatrixLocation = glGetUniformLocation(program, U_MATRIX_MODEL);
        uNormalMatrixLocation = glGetUniformLocation(program, U_MATRIX_NORMAL);

        uDiffuseTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT_DIFFUSE);
        uAmbientTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT_AMBIENT);
        uSpecularTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT_SPECULAR);
        uShininessLocation = glGetUniformLocation(program, U_SHININESS);

        uLightPositionLocation = glGetUniformLocation(program, U_LIGHT_POSITION);
        uLightAmbientLocation = glGetUniformLocation(program, U_LIGHT_AMBIENT);
        uLightDiffuseLocation = glGetUniformLocation(program, U_LIGHT_DIFFUSE);
        uLightSpecularLocation = glGetUniformLocation(program, U_LIGHT_SPECULAR);


        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        aNormalLocation = glGetAttribLocation(program, A_NORMAL);
    }


    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }

    public int getNormalAttributeLocation() {
        return aNormalLocation;
    }

    public void setMatrix(float[] mvp, float[] model, float[] normalMatrix) {

            glUniformMatrix4fv(uMatrixLocation, 1, false, mvp, 0);


            glUniformMatrix4fv(uModelMatrixLocation, 1, false, model, 0);


            glUniformMatrix4fv(uNormalMatrixLocation, 1, false, normalMatrix, 0);

    }

    public void setLightPosition(float x, float y, float z) {
        glUniform3f(uLightPositionLocation, x, y, z);
    }

    public void setMaterial(Model.Material material) {
        if (material == null) return;
        int diffuseTexId = material.getTextureId(Model.Texture.TYPE_TEXTURE_DIFFUSE);
        int specularTexId = material.getTextureId(Model.Texture.TYPE_TEXTURE_SPECULAR);
        if (diffuseTexId != 0) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, diffuseTexId);
            glUniform1i(uDiffuseTextureUnitLocation, 0);
            glUniform1i(uAmbientTextureUnitLocation, 0);
        }
        if (specularTexId != 0) {
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, specularTexId);
            glUniform1i(uSpecularTextureUnitLocation, 1);
        }
        Vec3 ambientColor = material.Ka;
        Vec3 diffuseColor = material.Kd;
        Vec3 specularColor = material.Ks;
        glUniform3f(uLightAmbientLocation, ambientColor.r, ambientColor.g, ambientColor.b);
        glUniform3f(uLightDiffuseLocation, diffuseColor.r, diffuseColor.g, diffuseColor.b);
        glUniform3f(uLightSpecularLocation, specularColor.r, specularColor.g, specularColor.b);
        glUniform3f(uLightPositionLocation, 0, 0, 20);
        float specularExponent = material.Ns;
        glUniform1f(uShininessLocation, specularExponent);
    }
}
