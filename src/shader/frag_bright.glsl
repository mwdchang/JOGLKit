#version 150

////////////////////////////////////////////////////////////////////////////////
//
// Shamelessly copied from
// https://bitbucket.org/paulwilson77/ogrerecast/src/88e3c1d5883b/media/materials/programs/BrightBloom2_ps20.glsl
//
////////////////////////////////////////////////////////////////////////////////

precision highp float;


uniform sampler2D tex;
uniform float thresholdBright;

in vec4 pass_colour;
in vec2 pass_texcoord; 


out vec4 outColour;

// gl_FragCoord is the screen coordinate !!!
// find out how to pass actual ST values
void main() {

   vec4 t = texture2D( tex, pass_texcoord);
   t -= 1.0;

   vec4 bright4 = -6.00 * t * t + 2.0;
   float bright = dot( bright4, vec4(0.33, 0.33, 0.33, 0.00));
   t += (bright + 0.6);

   outColour = (1-sqrt(thresholdBright))*t + texture2D(tex, pass_texcoord);



/*
   vec4 c = vec4(0, 0, 0, 0);
   int cc = 0;
   for (int x=-threshold; x <= threshold; x++) {
      for (int y=-threshold; y <= threshold; y++) {
         c += texture2D( tex, pass_texcoord.xy + vec2( x/width, y/height));
         cc ++;
      }
   }
   c /= cc;
   */

   //outColour = c;

  
  
}

