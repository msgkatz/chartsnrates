//Default precision qualifier
precision highp float;

varying vec4 position;
varying vec2 vTexCoord;
varying vec4 vFirstColor;
varying vec4 vSecondColor;

varying highp vec2 sTexCoord;

// from wikipedia on bilinear interpolation on unit square:
// f(x,y) = f(0,0)(1-x)(1-y) + f(1,0)x(1-y) + f(0,1)(1-x)y + f(1,1) xy.
// applied here:
// gl_FragColor = color0 * ((1-x)*(1-y) + x*y) + color1*(x*(1-y) + (1-x)*y)
// gl_FragColor = color0 * (1 - x - y + 2 * x * y) + color1 * (x + y - 2 * x * y)
// after simplification:
// float temp = (x + y - 2 * x * y);
// gl_FragColor = color0 * (1-temp) + color1 * temp;
void main() {
//    gl_FragColor = mix(vFirstColor, vSecondColor, sTexCoord.x + sTexCoord.y - 2.0 * sTexCoord.x * sTexCoord.y);
    gl_FragColor = mix(vFirstColor, vSecondColor, sTexCoord.y);
}