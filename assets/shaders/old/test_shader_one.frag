varying vec2 texcoord;
varying vec3 vBC;

uniform sampler2D smoke;
void main(){
//    gl_FragColor = texture2D(smoke, texcoord)*vec4(1.0, 1.0, 1.0, 0.7);
    gl_FragColor = vec4(1.0, 1.0, 1.0, 0.7);
}