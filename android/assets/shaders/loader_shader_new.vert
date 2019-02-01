attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
//varying vec4 position;
//varying vec4 vColor;
//varying vec2 vert_TexCoord;
//
//varying vec4 v_pos_center;
//varying float time;

void main() {
//    position = a_position;
//    vColor = a_color;
//    vert_TexCoord = a_texCoord0;
    gl_Position = u_projTrans  * a_position;
}