attribute vec3 a_Position;
attribute vec3 a_Color;
uniform mat4 u_Matrix;
varying vec4 v_Color;

void main() {
    gl_Position = u_Matrix * vec4(a_Position,1.0);
    vec4 pos = vec4(80.0,80.0,80.0,1.0); //光源位置
    vec3 vp = normalize(pos.xyz-gl_Position.xyz); //点与光源的向量
    //vec3 normal = normalize((u_Matrix*vec4(a_Position+normalize(a_Position),1)).xyz-(u_Matrix*vec4(a_Position,1)).xyz);
    vec3 normal = normalize(u_Matrix*normalize(vec4(a_Position,0))).xyz;
    vec4 light_color = vec4(1.0,1.0,1.0,1.0); //光源颜色
    v_Color = vec4(a_Color,1.0)*light_color*max(0.0,dot(normal,vp));
}


