#ifdef GL_ES
	precision mediump float;
#endif
varying vec4 v_pos_pixel;
varying vec4 v_pos_center;
varying mat4  u_projTrans;
uniform vec4  u_color;
uniform float u_radius;
uniform float u_scale;
	void main() {
		vec4 r = u_projTrans * vec4(u_radius, 0.0, 0.0, 0.0);
		vec4 p_pixel = v_pos_pixel,
		p_center = v_pos_center;
		p_pixel.x /= u_scale;
		p_center.x /= u_scale;
		float dist = distance(p_pixel.xy, p_center.xy);
		float alpha = smoothstep(r.x, r.x + 0.05, dist);
		vec4 col = mix(u_color, vec4(0.0), alpha);
		col.w = smoothstep(r.x + 0.005, r.x,dist);
		col.w *= u_color.w / 1.0;
		gl_FragColor = col;
	}