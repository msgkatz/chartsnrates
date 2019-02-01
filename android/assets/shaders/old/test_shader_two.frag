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
varying highp vec2 vTexCoord;

varying vec4 position;

void main()
{
    //Sample the texture at the interpolated coordinate
    lowp vec4 col = texture2D( u_texture, vTexCoord ) * vColor;

    float x,y, dash_x, dash_y;
    x=fract(vTexCoord.x * u_lineCount);
    y=fract(vTexCoord.y * u_lineCount);
    dash_x = fract(vTexCoord.x * u_dash_count);
    dash_y = fract(vTexCoord.y * u_dash_count);

    // Draw a black and white grid.
//    if(x > 0.98 || y > 0.98) {
//        gl_FragColor = vec4(1,1,1,1);
//    }
//    else
//    {
//        gl_FragColor = vec4(0,0,0,0);
//    }

    if(y < 0.02) {
        if(dash_x > 0.5){
            gl_FragColor = vec4(1,1,1,0.1);
        } else {
            gl_FragColor = vec4(0,0,0,0);
        }
    } else if(x > 0.98){
        if(dash_y > 0.5){
            gl_FragColor = vec4(1,1,1,0.1);
        } else {
            gl_FragColor = vec4(0,0,0,0);
        }
    }
    else
    {
        gl_FragColor = vec4(0,0,0,0);
    }
}