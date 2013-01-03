package examples.filters;

import graphics.DrawUtil;
import graphics.GraphicUtil;
import graphics.ShaderObj;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import base.JOGLBase;

////////////////////////////////////////////////////////////////////////////////
// Shows a bare minimum glsl shader setup
////////////////////////////////////////////////////////////////////////////////
public class DepthShader extends JOGLBase {
   
   public ShaderObj shader = new ShaderObj();

   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      gl2.glTranslated(0, 0, -100);
      gl2.glRotated(30, 0, 1, 0);
      gl2.glRotated(30, 1, 0, 0);
      //gl2.glTranslated(0, 0, -55);
      
      shader.bind(gl2);
         // Because they removed ftransform()
         float buffer[] = new float[16];
         gl2.glGetFloatv(GL2.GL_PROJECTION_MATRIX, buffer, 0);
         shader.setUniform4x4(gl2, "projection_matrix", buffer);
         gl2.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, buffer, 0);
         shader.setUniform4x4(gl2, "modelview_matrix", buffer);      
         
         shader.setUniformf(gl2, "dnear", (float)near+65);
         shader.setUniformf(gl2, "dfar", (float)(far/2.8));
         
         //shader.setUniformf(gl2, "myColour", 0.8f, 0.0f, 0.0f, 1.0f);
         DrawUtil.drawCubeQuad(gl2, 11);
         /*
         gl2.glBegin(GL2.GL_TRIANGLES);
            gl2.glVertex3d(0, 0, -90);
            gl2.glVertex3d(0, 10, -90);
            gl2.glVertex3d(10, 10, -30);
         gl2.glEnd();   
         */
      shader.unbind(gl2);
   }
   
   @Override
   public void init(GLAutoDrawable a) {
      super.init(a);
      GL2 gl2 = a.getGL().getGL2();
      
      
      shader.createShader(gl2, "src\\shader\\vert_depth.glsl", GL2.GL_VERTEX_SHADER);
      shader.createShader(gl2, "src\\shader\\frag_depth.glsl", GL2.GL_FRAGMENT_SHADER);
      shader.createProgram(gl2);
      shader.linkProgram(gl2);
      shader.bindFragColour(gl2, "outColour");
      
   }

}
