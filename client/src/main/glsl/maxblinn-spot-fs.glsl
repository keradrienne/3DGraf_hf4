#version 300 es
precision highp float;

out vec4 fragmentColor;
in vec4 tex;
//LABTODO: world space inputs
in vec4 worldNormal;
in vec4 worldPosition;

uniform struct {
    //LABTODO: uniform for environment
    sampler2D colorTexture;
} material;

uniform struct {
    mat4 viewProjMatrix;
    //LABTODO: uniform for computing view direction
    vec3 position;
} camera;

//LABTODO: uniforms for light source data
uniform struct {
    vec4 position;
    vec3 powerDensity;
    vec3 direction;
    vec2 exponentAndAngle;
} lights[8];

vec3 shade(
    vec3 normal, vec3 lightDir, vec3 viewDir,
    vec3 powerDensity, vec3 materialColor, vec3 specularColor, float shininess) {

    float cosa = clamp(dot(lightDir, normal), 0.0, 1.0);
    float cosb = clamp(dot(viewDir, normal), 0.0, 1.0);

    vec3 halfway = normalize(viewDir + lightDir);
    float cosDelta = clamp(dot(halfway, normal), 0.0, 1.0);

    return  powerDensity * materialColor * cosa +
    powerDensity * specularColor * pow(cosDelta, shininess) * cosa / max(cosa, cosb);
}

void main(void) {
    vec3 normal = normalize(worldNormal.xyz);
    vec3 viewDir = normalize(camera.position - worldPosition.xyz);

    fragmentColor = vec4(0, 0, 0, 1);
    for (int i = 0; i < 8; i++) {
        float intensity = 1.0;
        vec3 lightDir;
        vec3 position = lights[i].position.xyz;
        vec2 exponentAndAngle = lights[i].exponentAndAngle;
        vec3 powerDensity = lights[i].powerDensity;
        vec3 direction = lights[i].direction;

        if(lights[i].position.w == 0.0) {
            lightDir = normalize(position);
        } else {
            lightDir = normalize(position - worldPosition.xyz);
            float angle = degrees(acos(dot(-lightDir, normalize(position))));
            if( angle > exponentAndAngle.y) {
                intensity = pow(max(dot(normalize(worldNormal.xyz), lightDir), 0.0), exponentAndAngle.x);
            } else {
                intensity = 0.0;
            }
        }

        vec3 powerDens = powerDensity * intensity;

        fragmentColor.rgb += shade(normal, lightDir, viewDir, powerDens,
                                   texture(material.colorTexture, tex.xy / tex.w).rgb, vec3(1.0, 1.0, 1.0), 100.0);
    }

    fragmentColor.a = 1.0;
}
