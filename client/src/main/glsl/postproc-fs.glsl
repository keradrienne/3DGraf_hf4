#version 300 es
precision highp float;

uniform struct{
    sampler2D rawTexture;
} material;

in vec4 texCoord;
in vec4 rayDir;
out vec4 fragmentColor;

void main() {
    //flip the coordinates to y;
    vec4 color = texture(material.rawTexture, vec2(1.0 - texCoord.x, 1.0 - texCoord.y));

    // Output the color
    fragmentColor = color;
}