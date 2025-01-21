attribute vec4 a_position;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;

varying vec2 texcoord;
varying vec3 vBC;

attribute vec4 last, current, next;
attribute vec3 barycentric;
attribute float texoff;

uniform mat4 proj, view;
uniform vec2 viewport;
uniform float width;
float pi = 3.141592653589793;

    vec4 transform(vec3 coord){
        return u_projTrans * view * vec4(coord, 1.0);
    }

    vec2 project(vec4 device){
        vec3 device_normal = device.xyz/device.w;
        vec2 clip_pos = (device_normal*0.5+0.5).xy;
        return clip_pos * a_texCoord0;
    }

    vec4 unproject(vec2 screen, float z, float w){
        vec2 clip_pos = screen/a_texCoord0;
        vec2 device_normal = clip_pos*2.0-1.0;
        return vec4(device_normal*w, z, w);
    }

    float estimateScale(vec3 position, vec2 sPosition){
        vec4 view_pos = view * vec4(position, 1.0);
        vec4 scale_pos = view_pos - vec4(normalize(view_pos.xy)*width, 0.0, 0.0);
        vec2 screen_scale_pos = project(u_projTrans * scale_pos);
        return distance(sPosition, screen_scale_pos);
    }

    float curvatureCorrection(vec2 a, vec2 b){
        float p = a.x*b.y - a.y*b.x;
        float c = atan(p, dot(a,b))/pi;
        return clamp(c, -1.0, 1.0);
    }

    void main(){

//        viewport = a_texCoord0;
//        proj = u_projTrans;

        vec2 sLast = project(transform(last.xyz));
        vec2 sNext = project(transform(next.xyz));

        vec4 dCurrent = transform(current.xyz);
        vec2 sCurrent = project(dCurrent);

        vec2 normal1 = normalize(sLast - sCurrent);
        vec2 normal2 = normalize(sCurrent - sNext);
        vec2 normal = normalize(normal1 + normal2);
        float off = current.w;
//        texcoord = a_texCoord0;
        texcoord = vec2(texoff*0.7, off*0.5+0.5);
        vBC = barycentric;
        float angle = atan(normal.x, normal.y)+pi*0.5;
        vec2 dir = vec2(sin(angle), cos(angle))*off;
        float scale = estimateScale(current.xyz, sCurrent);
        vec2 pos = sCurrent + dir*scale;

        gl_Position = unproject(pos, dCurrent.z, dCurrent.w);
    }

//void main() {
//	gl_Position =  u_projTrans * a_position;
//	open = a_open;
//	close = a_close;
//}