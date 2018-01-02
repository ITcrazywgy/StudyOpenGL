precision mediump float;

varying vec2 v_TextureCoordinates;
uniform sampler2D u_TextureUnit;

varying vec4 v_Ambient; //������
varying vec4 v_Diffuse; //ɢ���
varying vec4 v_Specular;//�����

void main()
{
    //�����������ɫ����ƬԪ
    vec4 finalColor=texture2D(u_TextureUnit, v_TextureCoordinates);
    //����ƬԪ��ɫֵ
    gl_FragColor = finalColor*v_Ambient+finalColor*v_Diffuse+finalColor*v_Specular;
}