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


void main() {
   gl_Position = projection_matrix * modelview_matrix * vec4(in_position, 1.0);

/*
   pass_colour = vec4(1, 1, 1, 1);
   pass_texcoord = in_texcoord; 
   pass_normal   = in_normal;
   pass_position = in_position;
   */
}
