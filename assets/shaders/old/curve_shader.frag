precision highp float;

//This represents the current texture on the mesh
uniform lowp sampler2D u_texture;

uniform float u_candle_count;

uniform float u_candle_width;

//The interpolated vertex color for this fragment
varying vec4 vColor1;
varying vec4 vColor2;

//The interpolated texture coordinate for this fragment
varying vec2 vert_TexCoord;

varying vec4 position;

uniform float border_width;
uniform float aspect;

void main(){

//   float maxX = 1.0 - border_width;
//   float minX = border_width;
//   float maxY = maxX / aspect;
//   float minY = minX / aspect;
//
//   if (vert_TexCoord.x < maxX && vert_TexCoord.x > minX &&
//       vert_TexCoord.y < maxY && vert_TexCoord.y > minY) {
//       gl_FragColor = mix(vColor1, vColor2, vert_TexCoord.y);
//   } else {
//       gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
//   }

    gl_FragColor = mix(vColor1, vColor2, vert_TexCoord.y);
}