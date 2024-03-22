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

void main()
{
    //Sample the texture at the interpolated coordinate
    lowp vec4 col = texture2D( u_texture, vert_TexCoord ) * vColor;

    float x, dash_y;
    x=fract(vert_TexCoord.x * u_lineCount);
    dash_y = fract(vert_TexCoord.y * u_dash_count);

//    if(dash_y > 0.5){
//        gl_FragColor = vec4(1,1,1,0.1);
//    } else {
//        gl_FragColor = vec4(0,0,0,0);
//    }

    gl_FragColor = vec4(1,1,1,0.1);
}