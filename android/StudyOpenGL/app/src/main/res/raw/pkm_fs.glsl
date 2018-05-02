precision mediump float;
varying vec2 v_TextureCoordinates;
uniform sampler2D u_TextureUnit;
uniform sampler2D u_TextureUnit2;

void main() {
    vec4 color=texture2D(u_TextureUnit, v_TextureCoordinates);
    color.a=texture2D(u_TextureUnit2,v_TextureCoordinates).r;
    gl_FragColor = color;
}