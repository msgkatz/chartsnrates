uniform mat4 u_modelViewProjectionMatrix;
uniform mat4 mv;
attribute vec4 a_position;
attribute vec4 a_color;
varying vec4 v_color;
varying vec4 position;

//combined projection and view matrix
uniform mat4 u_projTrans;

void main() {
    v_color = a_color;
    gl_Position = u_projTrans * a_position;
    position = a_position;
}