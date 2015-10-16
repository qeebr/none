#version 330 core

layout(location = 0) in vec3 vertexPosition_modelspace;
layout(location = 1) in vec3 vertexColor;
layout(location = 2) in vec2 vertexUV;
layout(location = 3) in vec2 vertexNorm;

out vec2 uvCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat4 mvpMatrix;

//Render normal
void render() {
    //Currently a hack, should use precomputated mvpMatrix.
    mat4 mvp = projectionMatrix * viewMatrix * modelMatrix;

    gl_Position = mvp * vec4(vertexPosition_modelspace, 1);
    uvCoords = vertexUV;
}

void main() {
    render();
}