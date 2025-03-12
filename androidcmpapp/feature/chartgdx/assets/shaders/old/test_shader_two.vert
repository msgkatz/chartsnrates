attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;

uniform mat4 projectionMatrix;
varying vec4 position;

varying vec4 vColor;

varying vec2 vTexCoord;

void main() {
//    position = cross(a_position, vec4(1.0, -1.0, 1.0, 1.0));
    position = inverse(u_projTrans) * a_position;
    vColor = a_color;
    vTexCoord = a_texCoord0;
//    temp_position = vec4(a_position.x, 1.0 - a_position.y, a_position.z, 1);
    gl_Position = u_projTrans  * a_position;
}