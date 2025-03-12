precision mediump float;
uniform vec2 sourcePoint;
varying vec4 v_color;
varying vec4 position;

void main() {
    if (cos(0.1*abs(distance(sourcePoint.xy, position.xy))) + 0.5 > 0.0) {
        gl_FragColor = vec4(1.0, 0.0, 1.0, 1.0);
    } else {
        gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
    }
}