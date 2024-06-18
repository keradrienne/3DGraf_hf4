#version 300 es

in vec4 vertexPosition;

uniform struct{
  mat4 viewProjMatrix;
} camera;

out vec4 texCoord;

void main(void) {
  gl_Position = vec4(vertexPosition.x, vertexPosition.z, vertexPosition.y, vertexPosition.w)* camera.viewProjMatrix;
  texCoord = vec4(vertexPosition.x, vertexPosition.z, vertexPosition.y, vertexPosition.w);
}