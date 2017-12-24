uniform mat4 u_Matrix;
attribute vec4 a_Position;  
attribute vec2 a_TextureCoordinates;
varying vec2 v_TextureCoordinates;
varying vec4 v_Color;
void main()                    
{
    gl_Position = u_Matrix * a_Position;
    v_TextureCoordinates = a_TextureCoordinates;

    vec4 pos = vec4(80.0f,80.0f,80.0f,1f); //光源位置
    vec4 normal=normalize(a_Position);
    vec3 vp = normalize(pos.xyz-gl_Position.xyz); //点与光源的向量
    vec3 newTarget = normalize((u_Matrix*(a_Position+normal)).xyz-(u_Matrix*a_Position).xyz);
    float diffuse=max(dot(newTarget,vp),0.0f);

    vec4 light_color = vec4(1f,1f,1f,1f); //光源颜色
    v_Color = light_color*diffuse;
}