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
uniform int mx;
uniform int my;

in vec4 pass_colour;
in vec2 pass_texcoord; 


out vec4 outColour;

void main() {
   vec3 c = texture2D(tex, pass_texcoord.xy).rgb;
   outColour.rgb = c;
   outColour.a = 1.0;

   float dist = distance( gl_FragCoord.xy, vec2(mx, my)); 

   outColour.rgb *= (1.0-dist/200.0);
   

/*
   float dist = distance( gl_FragCoord.xy, vec2(mx, my)); 
   float dist2 = distance( gl_FragCoord.x, mx);
   if (dist2 > 50 && dist2 < 100) {
      if ( int(gl_FragCoord.x) % 20 < 5) outColour.rgb = vec3(0, 0, 0);
   } else if (dist2 >= 100 && dist2 < 200) {
      if ( int(gl_FragCoord.x) % 15 < 4) outColour.rgb = vec3(0, 0, 0);
   } else if (dist2 >= 200) {
      if ( int(gl_FragCoord.x) % 10 < 2) outColour.rgb = vec3(0, 0, 0);
   }

   dist2 = distance( gl_FragCoord.y, my);
   if (dist2 > 50 && dist2 < 100) {
      if ( int(gl_FragCoord.y) % 20 < 5) outColour.rgb = vec3(0, 0, 0);
   } else if (dist2 >= 100 && dist2 < 200) {
      if ( int(gl_FragCoord.y) % 15 < 4) outColour.rgb = vec3(0, 0, 0);
   } else if (dist2 >= 200) {
      if ( int(gl_FragCoord.y) % 10 < 2) outColour.rgb = vec3(0, 0, 0);
   }
*/


/*
   if ( int(gl_FragCoord.x) % int(sqrt(dist)) < 3) outColour.rgb = vec3(0, 0, 0);
   if ( int(gl_FragCoord.y) % int(sqrt(dist)) < 3) outColour.rgb = vec3(0, 0, 0);
*/

   /*
   if (dist < 300) {
      if ( int(gl_FragCoord.x) % 5 < 3) outColour.rgb = vec3(0, 0, 0);
      if ( int(gl_FragCoord.y) % 5 < 3) outColour.rgb = vec3(0, 0, 0);
   } else if (dist < 200) {
      if ( int(gl_FragCoord.x) % 10 < 3) outColour.rgb = vec3(0, 0, 0);
      if ( int(gl_FragCoord.y) % 10 < 3) outColour.rgb = vec3(0, 0, 0);
   } else if (dist < 300) {
      if ( int(gl_FragCoord.x) % 15 < 3) outColour.rgb = vec3(0, 0, 0);
      if ( int(gl_FragCoord.y) % 15 < 3) outColour.rgb = vec3(0, 0, 0);
   } 
   */
    


}
