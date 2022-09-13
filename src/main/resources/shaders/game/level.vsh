#version 130
#extension GL_EXT_gpu_shader4 : enable

in vec4 Position;
in vec4 Color;
in vec3 Normal;
in vec2 Tex;

out vec4 color;
out vec2 texCoord;

uniform int flags;
uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform sampler2D tex0;

void main() {
    float amt = 1;
    gl_Position = projectionMatrix * modelViewMatrix * Position;
    if ((flags & 1) == 1) {
        amt = dot(Normal.xyz, normalize(vec3(0.5, 0.0, 0.75)));
        amt = mod(amt, 1.0);
        amt = mod((amt / 3.0) + 0.96, 1.0);
        amt = abs(amt);
    }
    color = Color * vec4(amt, amt, amt, 1) + texture(tex0, Tex);
    texCoord = Tex;
}
