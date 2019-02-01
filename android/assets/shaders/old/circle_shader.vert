attribute vec4 a_position;
varying mat4 u_projTrans;
uniform vec2 u_pos_center;
varying vec4 v_pos_pixel;
varying vec4 v_pos_center;
	void main() {
		v_pos_pixel  =  u_projTrans * a_position;
		v_pos_center = u_projTrans * vec4(u_pos_center.xy, 0.0 , 1.0);
		gl_Position = v_pos_pixel;
	}