#ifdef GL_ES
precision mediump float;
#endif

//#extension GL_OES_standard_derivatives : enable
#define SPEED 1.95
//#define SPEED 2

//varying float time;
uniform int u_time_int;
uniform float u_time;
#define time u_time * SPEED
//#define time float(u_time_int) * SPEED

//uniform vec2 u_resolution;
const float PI = 3.1415926535897932384626433832795;




mat2 rotate2d(float angle){
    return mat2(cos(angle+time),0.,
                sin(angle+time),0.);
}

mat2 rotate2dFixed(float angle) {
	return mat2(cos(angle), 0.0,
		   sin(angle), 0.0);
}

float stripes(vec2 st){
	st = rotate2dFixed(PI*-0.202) * st * 5.0;
	//st = rotate2d(PI*-0.202) * st * 5.0;
    return step(0.5, 1.0-smoothstep(0.3, 1.0, abs(sin(st.x*PI))));
}

void main(){
	vec2 resolution = vec2(100.0, 100.0);
	vec2 st = gl_FragCoord.xy/resolution.xy + time;
    st.x *= resolution.x/resolution.y;

//	vec2 st = gl_FragCoord.xy/u_resolution.xy - time*1.95;
//    st.x *= u_resolution.x/u_resolution.y;

    vec3 color = vec3(stripes(st));
    gl_FragColor = vec4(color, 0.3);
}
