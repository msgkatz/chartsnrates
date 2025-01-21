attribute vec4 a_position;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;

varying vec4 position;
varying vec2 vTexCoord;
varying vec4 vFirstColor;
varying vec4 vSecondColor;

varying vec2 sTexCoord;

void main() {
    position = a_position;
    vFirstColor = vec4(25.0/255.0, 36.0/255.0, 49.0/255.0, 1);
    vSecondColor = vec4(25.0/255.0, 36.0/255.0, 49.0/255.0, 1);
//    vFirstColor = vec4(57.0/255.0, 150.0/255.0, 255.0/255.0, 0.3);
//    vSecondColor = vec4(57.0/255.0, 150.0/255.0, 255.0/255.0, 0.0);
    sTexCoord = a_texCoord0;
    gl_Position = u_projTrans  * a_position;
}