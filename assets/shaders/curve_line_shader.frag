precision mediump float;

varying vec2 v_texCoord;
uniform sampler2D u_texture;

uniform float u_width;
uniform float u_height;

//The interpolated vertex color for this fragment
varying vec4 vColor1;
varying vec4 vColor2;

varying vec4 position;

varying vec4 u_borderColor;

varying vec4 firstLinePosition;
varying vec4 secondLinePosition;

uniform vec2 u_resolution;

float line(vec2 P, vec2 A, vec2 B, float r) {
    vec2 g = B - A;
//    float d = abs(dot(normalize(vec2(g.y, -g.x)), P - A));
    float d = abs(dot(normalize(vec2(g.y, -g.x)), A));
    return smoothstep(r, 0.5 * r, d);
}

//float drawLine(vec2 p1, vec2 p2) {
//  vec2 uv = gl_FragCoord.xy / u_resolution.xy;
//
//  float a = abs(distance(p1, uv));
//  float b = abs(distance(p2, uv));
//  float c = abs(distance(p1, p2));
//
//  if ( a >= c || b >=  c ) return 0.0;
//
//  float p = (a + b + c) * 0.5;
//
//  // median to (p1, p2) vector
//  float h = 2.0 / c * sqrt( p * ( p - a) * ( p - b) * ( p - c));
//
//  return mix(1.0, 0.0, smoothstep(0.5 * 0.3, 1.5 * 0.3, h));
//}

void main()
{
    vec4 col = mix(vColor1, vColor2, v_texCoord.y);
    float indensity = 0.0;
    indensity = line (position.xy, firstLinePosition.xy, secondLinePosition.xy, 0.16);
//    if ((indensity > 0.0) && (indensity <= 1.0)) {
//        col = mix(col, u_borderColor, indensity);
//    }
    gl_FragColor = col;
//    gl_FragColor = vec4(
//      max(
//        max(
//          drawLine(vec2(0.1, 0.1), vec2(0.1, 0.9)),
//          drawLine(vec2(0.1, 0.9), vec2(0.7, 0.5))),
//        drawLine(vec2(0.1, 0.1), vec2(0.7, 0.5))));
}