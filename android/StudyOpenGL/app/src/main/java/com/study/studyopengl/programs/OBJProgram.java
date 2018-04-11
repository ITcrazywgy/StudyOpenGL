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
import com.study.studyopengl.parser.MTLMaterial;
import com.study.studyopengl.parser.OBJModel;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniformMatrix4fv;


public class OBJProgram extends ShaderProgram {
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


    public OBJProgram(Context context) {
        super(context, R.raw.obj_vs, R.raw.obj_fs);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uModelMatrixLocation = glGetUniformLocation(program, U_MATRIX_MODEL);
        uNormalMatrixLocation = glGetUniformLocation(program, U_MATRIX_NORMAL);

        uAmbientTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT_AMBIENT);
        uDiffuseTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT_DIFFUSE);
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

    public void setMaterial(OBJModel model, MTLMaterial material) {
        if (material == null) return;
        int ambientTexId = model.getTextureId(material.getAmbientTexture());
        int diffuseTexId = model.getTextureId(material.getDiffuseTexture());
        int specularTexId = model.getTextureId(material.getSpecularTexture());

        int index = 0;
        if (ambientTexId != 0) {
            glActiveTexture(GL_TEXTURE0 + index);
            glBindTexture(GL_TEXTURE_2D, ambientTexId);
            glUniform1i(uAmbientTextureUnitLocation, index);
            index++;
        }
        if (diffuseTexId != 0) {
            glActiveTexture(GL_TEXTURE0 + index);
            glBindTexture(GL_TEXTURE_2D, diffuseTexId);
            glUniform1i(uDiffuseTextureUnitLocation, index);
            index++;
        }
        if (specularTexId != 0) {
            glActiveTexture(GL_TEXTURE0 + index);
            glBindTexture(GL_TEXTURE_2D, specularTexId);
            glUniform1i(uSpecularTextureUnitLocation, index);
        }

        float[] ambientColor = material.getAmbientColor();
        float[] diffuseColor = material.getDiffuseColor();
        float[] specularColor = material.getSpecularColor();
        glUniform3f(uLightAmbientLocation, ambientColor[0], ambientColor[1], ambientColor[2]);
        glUniform3f(uLightDiffuseLocation, diffuseColor[0], diffuseColor[1], diffuseColor[2]);
        glUniform3f(uLightSpecularLocation, specularColor[0], specularColor[1], specularColor[2]);
        glUniform3f(uLightPositionLocation, 0, 0, 20);

        float specularExponent = material.getSpecularExponent();
        glUniform1f(uShininessLocation, specularExponent);
    }
}
