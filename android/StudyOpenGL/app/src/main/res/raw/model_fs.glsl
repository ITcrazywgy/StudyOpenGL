precision mediump float;
varying vec2 v_TextureCoordinates;

uniform sampler2D u_TextureUnit_Ambient;
uniform sampler2D u_TextureUnit_Diffuse;
uniform sampler2D u_TextureUnit_Specular;
uniform float u_Shininess;

uniform vec3 u_Light_Position;
uniform vec3 u_Light_Ambient;
uniform vec3 u_Light_Diffuse;
uniform vec3 u_Light_Specular;



varying vec3 v_FragPos;
varying vec3 v_Normal;
uniform vec3 u_Camera;

void main()
{
    /*// ambient
    vec3 ambient =u_Light_Ambient * texture2D(u_TextureUnit_Ambient, v_TextureCoordinates).rgb;

    // diffuse
    vec3 norm = normalize(v_Normal);
    vec3 lightDir = normalize(u_Light_Position - v_FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = u_Light_Diffuse * diff * texture2D(u_TextureUnit_Diffuse, v_TextureCoordinates).rgb;

    // specular
    vec3 viewDir = normalize(u_Camera - v_FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), u_Shininess);
    vec3 specular = u_Light_Specular * spec * texture2D(u_TextureUnit_Specular, v_TextureCoordinates).rgb;

    vec3 result = ambient + diffuse + specular;*/
    gl_FragColor = vec4(1.0,1.0,0.0, 1.0);
}