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
uniform float thresholdRafa;


in vec4 pass_colour;
in vec2 pass_texcoord; 


out vec4 outColour;

// gl_FragCoord is the screen coordinate !!!
// find out how to pass actual ST values
void main() {

   /* Test 1 */
   /*
   outColour.rgb = texture2D( tex, pass_texcoord.xy).rgb;
   
   float avg = ( outColour.r + outColour.g + outColour.b ) * 0.333;
   
   if ( avg > thresholdRafa) {
      outColour.rgb = vec3(1.0, 0.5, 0.0);
   } 
   */


   /* Test 2 */
   /*
   outColour.rgb = texture2D( tex, pass_texcoord.xy).rgb;
   float avg = ( outColour.r + outColour.g + outColour.b ) * 0.333;
   if (avg > 0.8 && avg < 0.5) {
      outColour.rgb = vec3(1, 1, 1) - outColour.rgb;
      outColour.rgb += vec3(1, 0.5, 0);
   } else if (avg >= 0.5) {
      outColour.rgb = vec3(1, 0.5, 0);
   }
   */


   /* Test 3 */
   /*
   vec3 newback = vec3(1, 0.5, 0);
   vec3 foreground = vec3(0, 0, 0);
   vec3 background = vec3(1, 1, 1);
   vec3 c = texture2D(tex, pass_texcoord.xy).rgb;

   // How close is it to the background?
   float distance = distance( c, background );

   if ( distance < thresholdRafa ) {
      // Because white is saturated, we need to invert for abs 
      //outColour.rgb = 0.5*(((vec3(1, 1, 1) - c) + newback) + background);   
      //outColour.rgb =  (c + newback)/2.0;   
      outColour.rgb = (1-distance) * newback + distance*background;
   } else {
      outColour.rgb = c;
   }
   */

   vec3 newback = vec3(1, 0.5, 0);
   vec3 foreground = vec3(0, 0, 0);
   vec3 background = vec3(1, 1, 1);
   vec3 c = texture2D(tex, pass_texcoord.xy).rgb;

   // How close is it to the background?
   float distance = distance( c, background );

   if ( distance < thresholdRafa ) {
      // Because white is saturated, we need to invert for abs 
      //outColour.rgb = 0.5*(((vec3(1, 1, 1) - c) + newback) + background);   
      //outColour.rgb =  (c + newback)/2.0;   
      outColour.rgb =  newback; 
   } else {
      outColour.rgb = c + (1-distance)*newback;
   }




  
}

