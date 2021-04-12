#version 330

const vec3 waterColor = vec3(0.60, 0.87, 0.85);

const float fresnelReflectiveCoefficient = 0.5;
const float edgeSoftness = 0.5;
const float minimalBlueness = 0.4;
const float maximalBlueness = 0.8;
const float murkinessDepth = 14;

out vec4 out_color;

in vec4 pass_clippingSpaceGrid;
in vec4 pass_clippingSpaceReal;
in vec3 pass_normal;
in vec3 pass_toCameraVector;
in vec3 pass_specular;
in vec3 pass_diffuse;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D depthTexture;

uniform vec2 nearFarPlanes;

vec3 applyMurkiness(vec3 refractColour, float waterDepth) {
    float murkyFactor = clamp(waterDepth / murkinessDepth, 0.0, 1.0);
    float murkiness = minimalBlueness + murkyFactor *
                                (maximalBlueness - minimalBlueness);

    return mix(refractColour, waterColor, murkiness);
}

float toLinearDepth(float zDepth){
    float near = nearFarPlanes.x;
    float far = nearFarPlanes.y;

    return 2.0 * near * far / (far + near - (2.0 * zDepth - 1.0) *
                                                        (far - near));
}

float calculateWaterDepth(vec2 textureCoordinates) {
    float depth = texture(depthTexture, textureCoordinates).r;
    float floorDistance = toLinearDepth(depth);

    depth = gl_FragCoord.z;

    float waterDistance = toLinearDepth(depth);

    return floorDistance - waterDistance;
}

float calculateFresnel() {
    vec3 viewVector = normalize(pass_toCameraVector);
    vec3 normal = normalize(pass_normal);
    float refractiveFactor = dot(viewVector, normal);

    refractiveFactor = pow(refractiveFactor, fresnelReflectiveCoefficient);

    return clamp(refractiveFactor, 0.0, 1.0);
}

vec2 clipSpaceToTextureCoordinates(vec4 clippingSpace) {
    vec2 ndc = (clippingSpace.xy / clippingSpace.w);
    vec2 textureCoordinates = ndc / 2.0 + 0.5;

    return clamp(textureCoordinates, 0.002, 0.998);
}

void main(void) {
    vec2 textureCoordinatesReal =
                    clipSpaceToTextureCoordinates(pass_clippingSpaceReal);
    vec2 textureCoordinatesGrid =
                    clipSpaceToTextureCoordinates(pass_clippingSpaceGrid);

    vec2 refractionTextureCoordinates = textureCoordinatesGrid;
    vec2 reflectionTextureCoordinates =
                vec2(textureCoordinatesGrid.x, 1.0 - textureCoordinatesGrid.y);
    float waterDepth = calculateWaterDepth(textureCoordinatesReal);

    vec3 refractionColor = texture(refractionTexture, refractionTextureCoordinates).rgb;
    vec3 reflectionColor = texture(reflectionTexture, reflectionTextureCoordinates).rgb;

    refractionColor = applyMurkiness(refractionColor, waterDepth);
    reflectionColor = mix(reflectionColor, waterColor, minimalBlueness);

    vec3 finalColor = mix(reflectionColor, refractionColor, calculateFresnel());
    finalColor = finalColor * pass_diffuse + pass_specular;

    out_color = vec4(finalColor, 1.0);

    out_color.a = clamp(waterDepth / edgeSoftness, 0.0, 1.0);
}