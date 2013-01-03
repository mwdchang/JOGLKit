#version 150

////////////////////////////////////////////////////////////////////////////////
// This is a simple pass through shader that takes in
// vertex, normal, colour and texcoord information and pass them forward
// into the pipeline.
// 
// in_colour   - rgba colour
// in_texcoord - uv coord
// in_position - xyz
// in_normal   - xyz
//
// In addition colour can be over written via the uniform parameter
//
////////////////////////////////////////////////////////////////////////////////


in vec3 in_position;

uniform mat4 projection_matrix;
uniform mat4 modelview_matrix;
uniform float dnear;
uniform float dfar;

out float depth;

void main() {
   float posZ = (modelview_matrix * vec4(in_position, 1.0)).z;
   
   //depth = ( -posZ - 1) / (300 - 1);
   depth = ( -posZ - dnear) / (dfar - dnear);
   gl_Position = projection_matrix * modelview_matrix * vec4(in_position, 1.0);
   
   
}
