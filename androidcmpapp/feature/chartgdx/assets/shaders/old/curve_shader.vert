attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;

varying vec4 position;

varying vec4 vColor1;
varying vec4 vColor2;

varying vec2 vert_TexCoord;

void main(){
    position = a_position;
    vColor1 = vec4(56.0/255.0, 150.0/255.0, 255.0/255.0, 0.1);
    vColor2 = vec4(56.0/255.0, 150.0/255.0, 255.0/255.0, 0.0);
//    vColor1 = vec4(1.0, 1.0, 1.0, 1.0);
//    vColor2 = vec4(1.0, 1.0, 1.0, 1.0);
    vert_TexCoord = a_texCoord0;
    gl_Position = u_projTrans  * a_position;
}