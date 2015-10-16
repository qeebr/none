#version 330 core

in vec2 uvCoords;

out vec4 color;

uniform sampler2D textureSampler;
 
void main() {
    color = texture(textureSampler, uvCoords).rgba;
    //color = vec3(1,0,0);
}