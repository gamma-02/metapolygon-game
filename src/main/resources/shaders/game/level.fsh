#version 130
#extension GL_EXT_gpu_shader4 : enable

in vec4 color;
in vec2 texCoord;

uniform sampler2D tex0;
uniform int flags;

out vec4 colorOut;

void main() {
    colorOut = color;
    if ((flags & 2) == 2) colorOut *= texture(tex0, texCoord.xy);
}
