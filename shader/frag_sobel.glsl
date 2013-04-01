#version 150

////////////////////////////////////////////////////////////////////////////////
//
// This shader creates a 'magic' lens effect
// by texturing on top of an existing frame buffer.
// It draws a circle by default given a uniform radius length of magicLensRadius
// at location (smouseX, smouseY)
//
////////////////////////////////////////////////////////////////////////////////

precision highp float;


uniform sampler2D tex;
uniform float thresholdSobel;
uniform float width;
uniform float height;


in vec4 pass_colour;
in vec2 pass_texcoord; 


out vec4 outColour;

// gl_FragCoord is the screen coordinate !!!
// find out how to pass actual ST values
void main() {

   float ix = 1.0/width;
   float iy = 1.0/height;
  
   vec4 s00 = texture2D( tex, pass_texcoord.xy + vec2(-ix, -iy));
   vec4 s01 = texture2D( tex, pass_texcoord.xy + vec2(  0, -iy));
   vec4 s02 = texture2D( tex, pass_texcoord.xy + vec2( ix, -iy));
  
   vec4 s10 = texture2D( tex, pass_texcoord.xy + vec2(-ix, 0));
   vec4 s12 = texture2D( tex, pass_texcoord.xy + vec2( ix, 0));
  
   vec4 s20 = texture2D( tex, pass_texcoord.xy + vec2( -ix, iy));
   vec4 s21 = texture2D( tex, pass_texcoord.xy + vec2(   0, iy));
   vec4 s22 = texture2D( tex, pass_texcoord.xy + vec2(  ix, iy));
  
   vec4 sobelX = 3*s00 + 10*s10 + 3*s20 - 3*s02 - 10*s12 - 3*s22;
   vec4 sobelY = 3*s00 + 10*s01 + 3*s02 - 3*s20 - 10*s21 - 3*s22;
   
   
   outColour.rgb = sqrt(sobelX.rgb*sobelX.rgb + sobelY.rgb*sobelY.rgb);
   if (outColour.r + outColour.g + outColour.b < thresholdSobel) {
      outColour.rgb = vec3(0, 0, 0);
      //outColour.rgb = texture2D( tex, pass_texcoord.xy).rgb;
   } else {
      outColour.rgb = texture2D( tex, pass_texcoord.xy).rgb;
      //outColour.rgb = vec3(0, 1, 1);
   }
   outColour.a = 1.0;
  
}

