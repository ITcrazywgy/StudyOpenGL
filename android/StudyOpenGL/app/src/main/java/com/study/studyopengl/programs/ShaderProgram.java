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

import com.study.studyopengl.util.ShaderHelper;
import com.study.studyopengl.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;


public abstract class ShaderProgram {

    // Uniform constants
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_TEXTURE_UNIT_AMBIENT = "u_TextureUnit_Ambient";
    protected static final String U_TEXTURE_UNIT_DIFFUSE = "u_TextureUnit_Diffuse";
    protected static final String U_TEXTURE_UNIT_SPECULAR = "u_TextureUnit_Specular";
    protected static final String U_SHININESS = "u_Shininess";

    protected static final String U_LIGHT_POSITION = "u_Light_Position";
    protected static final String U_LIGHT_AMBIENT = "u_Light_Ambient";
    protected static final String U_LIGHT_DIFFUSE = "u_Light_Diffuse";
    protected static final String U_LIGHT_SPECULAR = "u_Light_Specular";

    protected static final String U_COLOR = "u_Color";
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_MATRIX_MODEL = "u_Matrix_Model";
    protected static final String U_MATRIX_NORMAL = "u_Matrix_Normal";
    protected static final String U_CAMERA = "u_Camera";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected static final String A_NORMAL = "a_Normal";

    // Shader program
    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }

    public void useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program);
    }
}
