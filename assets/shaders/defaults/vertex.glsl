#version 330 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;

uniform mat4 uProjMatrix;
uniform mat4 uview;

out vec4 fColor;

void main()
{
    fColor = aColor;
    gl_Position = uProjMatrix * uview * vec4(aPos, 1.0);
}


