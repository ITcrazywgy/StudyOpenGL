uniform mat4 u_Matrix; //总矩阵
uniform mat4 u_Matrix_Model; //模型矩阵
uniform vec3 u_Camera;
uniform vec3 u_Light;//光位置

attribute vec3 a_Normal;//法向量
attribute vec3 a_Position;
attribute vec2 a_TextureCoordinates;


varying vec2 v_TextureCoordinates;
varying vec4 v_Ambient; //环境光
varying vec4 v_Diffuse; //散射光
varying vec4 v_Specular;//镜面光


void light(
inout vec4 ambient,
inout vec4 diffuse,
inout vec4 specular,

in vec3 lightLocation,

in vec4 lightAmbient,
in vec4 lightDiffuse,
in vec4 lightSpecular
){
    //环境光=材质反射系数*环境光强度
    ambient = lightAmbient;
    //散射光=材质反射系数*散射光强度*max(cos(入射角)，0)
    vec3 normal=normalize(a_Normal);
    normal = (u_Matrix_Model*vec4(a_Position + normal,1)).xyz - (u_Matrix_Model*vec4(a_Position,1)).xyz;
    normal = normalize(normal);
    vec3 vp = normalize(lightLocation-(u_Matrix_Model*vec4(a_Position,1)).xyz);
    diffuse=lightDiffuse*max(0.0f,dot(vp,normal));
    //镜面光=材质反射系数*镜面光强度*max(0,cos(入射角)【粗糙度（次方】)
    vec3 eye = normalize(u_Camera-(u_Matrix_Model*vec4(a_Position,1)).xyz);
    vec3 hv=normalize(vp+eye);
    //粗糙度
    float ess = 50.0f;
    specular=lightSpecular*max(0.0f,pow(dot(normal,hv),ess));
}


void main()                    
{
    gl_Position = u_Matrix * vec4(a_Position,1);
    v_TextureCoordinates = a_TextureCoordinates;

    vec4 ambientT= vec4(0,0,0,0);
    vec4 diffuseT= vec4(0,0,0,0);
    vec4 specularT=vec4(0,0,0,0);

    light(ambientT,diffuseT,specularT,
          u_Light,
          vec4(0.05f,0.05f,0.05f,1),
          vec4(1,1,1,1),
          vec4(0.3f,0.3f,0.3f,1));

    v_Ambient = ambientT;
    v_Diffuse = diffuseT;
    v_Specular = specularT;
}