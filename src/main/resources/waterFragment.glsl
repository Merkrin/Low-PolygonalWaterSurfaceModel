#version 330

const vec3 waterColour = vec3(0.604, 0.867, 0.851);
const float fresnelReflective = 0.5;
const float edgeSoftness = 1;
const float minBlueness = 0.4;
const float maxBlueness = 0.8;
const float murkyDepth = 14;

out vec4 out_colour;

in vec4 pass_clipSpaceGrid;
in vec4 pass_clipSpaceReal;
in vec3 pass_normal;
in vec3 pass_toCameraVector;
in vec3 pass_specular;
in vec3 pass_diffuse;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D depthTexture;
uniform vec2 nearFarPlanes;

// Apply murkiness effect to the color.
vec3 applyMurkiness(vec3 refractColour, float waterDepth) {
    float murkyFactor = clamp(waterDepth / murkyDepth, 0.0, 1.0);
    float murkiness = minBlueness + murkyFactor * (maxBlueness - minBlueness);

    return mix(refractColour, waterColour, murkiness);
}

// Convert z depth to linear one.
float toLinearDepth(float zDepth){
    float near = nearFarPlanes.x;
    float far = nearFarPlanes.y;

    return 2.0 * near * far / (far + near - (2.0 * zDepth - 1.0) *
                                                        (far - near));
}

// Calculate depth of the water place.
float calculateWaterDepth(vec2 textureCoordinates) {
    float depth = texture(depthTexture, textureCoordinates).r;
    float floorDistance = toLinearDepth(depth);

    depth = gl_FragCoord.z;

    float waterDistance = toLinearDepth(depth);

    return floorDistance - waterDistance;
}

// Get value used for the Fresnel effect.
float calculateFresnel() {
    vec3 viewVector = normalize(pass_toCameraVector);
    vec3 normal = normalize(pass_normal);
    float refractiveFactor = dot(viewVector, normal);

    refractiveFactor = pow(refractiveFactor, fresnelReflective);

    return clamp(refractiveFactor, 0.0, 1.0);
}

// Clip space and texture coordinates.
vec2 clipSpaceToTextureCoordinates(vec4 clippingSpace) {
    vec2 ndc = (clippingSpace.xy / clippingSpace.w);
    vec2 textureCoordinates = ndc / 2.0 + 0.5;

    return clamp(textureCoordinates, 0.002, 0.998);
}

void main(void) {
    vec2 texCoordsReal = clipSpaceToTextureCoordinates(pass_clipSpaceReal);
    vec2 texCoordsGrid = clipSpaceToTextureCoordinates(pass_clipSpaceGrid);

    vec2 refractionTexCoords = texCoordsGrid;
    vec2 reflectionTexCoords = vec2(texCoordsGrid.x, 1.0 - texCoordsGrid.y);
    float waterDepth = calculateWaterDepth(texCoordsReal);

    vec3 refractColour = texture(refractionTexture, refractionTexCoords).rgb;
    vec3 reflectColour = texture(reflectionTexture, reflectionTexCoords).rgb;

    refractColour = applyMurkiness(refractColour, waterDepth);
    reflectColour = mix(reflectColour, waterColour, minBlueness);

    vec3 finalColour = mix(reflectColour, refractColour, calculateFresnel());
    finalColour = finalColour * pass_diffuse + pass_specular;

    out_colour = vec4(finalColour, 1.0);

    //apply soft edges
    out_colour.a = clamp(waterDepth / edgeSoftness, 0.0, 1.0);
}