precision highp float;

uniform sampler2D tex0;
uniform float border_size;
uniform float disc_radius;
uniform vec4 disc_color;
uniform vec2 disc_center;
varying vec2 vert_TexCoord;

void main (void)
{
  vec2 uv = vert_TexCoord.xy;

  vec4 bkg_color = texture2D(tex0,uv * vec2(1.0, -1.0));

  uv -= disc_center;

  float dist = sqrt(dot(uv, uv));
  float t = smoothstep(disc_radius + border_size, disc_radius - border_size, dist);
  gl_FragColor = mix(bkg_color, disc_color, t);
}