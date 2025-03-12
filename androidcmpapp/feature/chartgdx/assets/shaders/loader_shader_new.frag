#ifdef GL_ES
precision mediump float;
#endif

//#extension GL_OES_standard_derivatives : enable

uniform vec2 u_resolution;
uniform float u_time;

// Parameters
#define FREQUENCE 8.
#define TILT -.9
#define SPEED 1.95
#define THIKNESS .35
#define SMOOTHNESS .01
//



#define _Smooth(p,r,s) smoothstep(-s, s, p-(r))
#define time iTime * SPEED
#define _thikness THIKNESS * .5

//Compressed  by : FabriceNeyret2
/*
void mainImage( out vec4 O, in vec2 U )
{
	float s = fract( dot(U/iResolution.xy, vec2(FREQUENCE,TILT)) + time );   
    O = vec4( _Smooth(_thikness, abs(s - .5) ,SMOOTHNESS) ); // boldness
    
    vec3 c1 = vec3( 106., 45., 190. ) / 255.;
    vec3 c2 = vec3( 246., 231., 62. ) / 255.;
    
    vec3 color = mix( c1, c2, coeffColor );
    
    fragColor = vec4( color, 1.0 );
}
*/


void main()
{
	vec2 uv = gl_FragCoord.xy / u_resolution.xy;
	//vec2 uv = vec2((gl_FragCoord.x / u_resolution.x), (gl_FragCoord.y / u_resolution.y));
    
    
    //float stripes = fract(uv.x * FREQUENCE + uv.y * TILT + time); // frequence tilt and animation
	//float stripes = fract( dot(gl_FragCoord/u_resolution.xy, vec2(FREQUENCE,TILT)) + u_time );
	//float stripes = fract( dot(uv, vec2(FREQUENCE,TILT)) + u_time*SPEED );
    float stripes = fract(uv.x * FREQUENCE + uv.y * TILT + u_time*SPEED); // frequence tilt and animation

    stripes = smoothstep(-SMOOTHNESS, SMOOTHNESS, _thikness - abs(stripes - .5));

    //stripes = _Smooth(_thikness, abs(stripes - .5) ,SMOOTHNESS); // boldness
    
    vec3 c2 = vec3( 106., 45., 190. ) / 255.;
    vec3 c1 = vec3( 246., 231., 62. ) / 255.;
    
    vec3 color = mix( c1, c2, stripes );
    
	//fragColor = vec4(1.) * stripes;
    gl_FragColor = vec4( color, 1.0 );
}