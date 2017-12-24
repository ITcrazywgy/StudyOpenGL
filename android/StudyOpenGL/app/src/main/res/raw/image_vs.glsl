uniform mat4 u_Matrix;
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
varying vec4 v_Position_Changed;

void main(){
    gl_Position=u_Matrix*a_Position;
    v_TextureCoordinates=a_TextureCoordinates;
    v_Position=a_Position;
    v_Position_Changed=gl_Position;
}