//Default precision qualifier
precision highp float;

//This represents the current texture on the mesh
uniform lowp sampler2D u_texture;

//count of dash deffault 100
uniform float u_dash_count;

//count of line deffault 11
uniform float u_lineCount;

//The interpolated vertex color for this fragment
varying lowp vec4 vColor;

//The interpolated texture coordinate for this fragment
varying highp vec2 vert_TexCoord;

varying vec4 position;

//отношение длины линие к пробелу
uniform float u_quotient;

uniform highp vec4 dotted_color;

void main()
{
    //Sample the texture at the interpolated coordinate
    lowp vec4 col = texture2D( u_texture, vert_TexCoord ) * vColor;

    float dash_y;
    dash_y = fract(vert_TexCoord.y * u_dash_count);
    if(dash_y < u_quotient){
        gl_FragColor = vec4(dotted_color);
    } else {
        gl_FragColor = vec4(0,0,0,0);
    }
}