precision mediump float;
uniform sampler2D u_TextureUnit;

varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
varying vec4 v_Position_Changed;

void main() {
    gl_FragColor=texture2D(u_TextureUnit,v_TextureCoordinates);
}
