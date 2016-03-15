#version 330 core

in vec2 uvCoords;

out vec4 color;

uniform sampler2D textureSampler;
 
void main() {
    vec4 textureColor = texture(textureSampler, uvCoords);
    //vec4 textureColor = vec4(1,0,0,1);

    if(textureColor.a != 0.0) {
        color = textureColor;
    } else {
        //color = vec4(1,0,0,1);
    }
}