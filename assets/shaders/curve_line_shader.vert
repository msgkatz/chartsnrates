attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform vec2 a_firstPosition;
uniform vec2 a_secondPosition;

uniform mat4 u_projTrans;

varying vec4 position;

varying vec4 vColor1;
varying vec4 vColor2;

varying vec4 u_borderColor;

varying vec2 v_texCoord;

varying vec4 firstLinePosition;
varying vec4 secondLinePosition;

void main(){
    position = a_position;

//    vColor1 = vec4(56.0/255.0, 150.0/255.0, 255.0/255.0, 0.1);
//    vColor2 = vec4(56.0/255.0, 150.0/255.0, 255.0/255.0, 0.0);
//    u_borderColor = vec4(3.0/255.0, 169.0/255.0, 244.0/255.0, 1.0);

    vColor1 = vec4(35.0/255.0, 245.0/255.0, 141.0/255.0, 0.1);
    vColor2 = vec4(35.0/255.0, 245.0/255.0, 141.0/255.0, 0.0);

    //u_borderColor = vec4(7.0/255.0, 217.0/255.0, 63.0/255.0, 1.0);
    u_borderColor = vec4(35.0/255.0, 245.0/255.0, 141.0/255.0, 0.0);

    v_texCoord = a_texCoord0;
    gl_Position = u_projTrans  * a_position;
//    position = u_projTrans  * a_position;
    firstLinePosition = u_projTrans * vec4(a_firstPosition.xy, 0, 0);
    secondLinePosition = u_projTrans * vec4(a_secondPosition.xy, 0, 0);
}