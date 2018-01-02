precision mediump float;

varying vec2 v_TextureCoordinates;
uniform sampler2D u_TextureUnit;

varying vec4 v_Ambient; //环境光
varying vec4 v_Diffuse; //散射光
varying vec4 v_Specular;//镜面光

void main()
{
    //将计算出的颜色给此片元
    vec4 finalColor=texture2D(u_TextureUnit, v_TextureCoordinates);
    //给此片元颜色值
    gl_FragColor = finalColor*v_Ambient+finalColor*v_Diffuse+finalColor*v_Specular;
}