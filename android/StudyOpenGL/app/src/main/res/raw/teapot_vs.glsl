uniform mat4 u_Matrix; //�ܾ���
uniform mat4 u_Matrix_Model; //ģ�;���
uniform vec3 u_Camera;
uniform vec3 u_Light;//��λ��

attribute vec3 a_Normal;//������
attribute vec3 a_Position;
attribute vec2 a_TextureCoordinates;


varying vec2 v_TextureCoordinates;
varying vec4 v_Ambient; //������
varying vec4 v_Diffuse; //ɢ���
varying vec4 v_Specular;//�����


void light(
inout vec4 ambient,
inout vec4 diffuse,
inout vec4 specular,

in vec3 lightLocation,

in vec4 lightAmbient,
in vec4 lightDiffuse,
in vec4 lightSpecular
){
    //������=���ʷ���ϵ��*������ǿ��
    ambient = lightAmbient;
    //ɢ���=���ʷ���ϵ��*ɢ���ǿ��*max(cos(�����)��0)
    vec3 normal=normalize(a_Normal);
    normal = (u_Matrix_Model*vec4(a_Position + normal,1)).xyz - (u_Matrix_Model*vec4(a_Position,1)).xyz;
    normal = normalize(normal);
    vec3 vp = normalize(lightLocation-(u_Matrix_Model*vec4(a_Position,1)).xyz);
    diffuse=lightDiffuse*max(0.0f,dot(vp,normal));
    //�����=���ʷ���ϵ��*�����ǿ��*max(0,cos(�����)���ֲڶȣ��η���)
    vec3 eye = normalize(u_Camera-(u_Matrix_Model*vec4(a_Position,1)).xyz);
    vec3 hv=normalize(vp+eye);
    //�ֲڶ�
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