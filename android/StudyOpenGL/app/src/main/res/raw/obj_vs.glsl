uniform mat4 u_Matrix;
uniform mat4 u_Matrix_Model;
uniform mat4 u_Matrix_Normal;
attribute vec3 a_Position;
attribute vec2 a_TextureCoordinates;
attribute vec3 a_Normal;

varying vec3 v_Normal;
varying vec2 v_TextureCoordinates;
varying vec3 v_FragPos;

void main()                    
{
    gl_Position = u_Matrix * vec4(a_Position,1.0);
    v_TextureCoordinates = a_TextureCoordinates;
    //v_Normal = mat3(transpose(inverse(u_Matrix_Model))) * a_Normal;
    v_Normal = mat3(u_Matrix_Normal) * a_Normal;
    v_FragPos = vec3(u_Matrix_Model * vec4(a_Position, 1.0));
}