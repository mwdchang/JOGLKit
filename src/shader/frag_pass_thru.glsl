#version 150

////////////////////////////////////////////////////////////////////////////////
// This is a simple pass thru fragment shader/
////////////////////////////////////////////////////////////////////////////////

precision highp float;

uniform vec4 myColour;

out vec4 outColour;

void main() {
   outColour = myColour;
   //outColour = vec4(0, 1, 1, 1);
}

