#version 130
#extension GL_EXT_gpu_shader4 : enable

in vec4 Position;
in vec4 Color;
in vec2 Tex;

out vec4 color;
out vec2 texCoord;

uniform int flags;
uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform sampler2D tex0;

void main() {
    gl_Position = projectionMatrix * modelViewMatrix * Position;
    color = Color + texture(tex0, Tex);
    texCoord = Tex;
}
