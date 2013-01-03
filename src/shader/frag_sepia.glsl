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
uniform float threshold;

in vec4 pass_colour;
in vec2 pass_texcoord; 


out vec4 outColour;

void main() {
   vec3 c = texture2D(tex, pass_texcoord.xy).rgb;

   vec3 sepia;

   sepia.r = dot(c, vec3(0.393, 0.769, 0.189));
   sepia.g = dot(c, vec3(0.349, 0.686, 0.168));
   sepia.b = dot(c, vec3(0.272, 0.534, 0.131));

   outColour.r = (1-threshold)*c.r + threshold*sepia.r;
   outColour.g = (1-threshold)*c.g + threshold*sepia.g;
   outColour.b = (1-threshold)*c.b + threshold*sepia.b;
   
   /*
   float cGray = 0.333*(c.r+c.g+c.b);

   //outColour.rgb = vec3(cGray, cGray, cGray);
   outColour.rgb = vec3(0, 0, 0);

   if (c.r > c.g && c.r > c.b) {
      outColour.r += c.r;
   } else if (c.g > c.r && c.g > c.b) {
      outColour.g += c.g;
   } else if (c.b > c.r && c.b > c.g) {
      outColour.b += c.b;
   }
   */
  
}
