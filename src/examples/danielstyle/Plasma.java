package examples.danielstyle;

import graphics.GraphicUtil;
import graphics.ShaderObj;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import base.JOGLBase;



/////////////////////////////////////////////////////////////////////////////////
// Port of an old program that simulates a plasma like equation
// This java portion just initializes the parameters and
// send them off to a fragment shader, which does the heavy work
/////////////////////////////////////////////////////////////////////////////////
public class Plasma extends JOGLBase {
   

   @Override
   public void handleKeyPress(int key, char c) {
      if (key == KeyEvent.VK_SPACE) {
         this.resetData();
      }
   }


   @Override
   public void render(GLAutoDrawable drawable) {
      GL2 gl2 = drawable.getGL().getGL2();
      
      
      // Setup orthonormal projection
      /*
      gl2.glMatrixMode(GL2.GL_PROJECTION);
      gl2.glLoadIdentity();
      gl2.glOrtho(0, w, 0, h, -10, 10);
      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      gl2.glLoadIdentity();
      */
      
      GraphicUtil.setOrthonormalView(gl2, 0, 1, 0, 1, -10, 10);
      
      
      // Send a quad down the pipeline, we will use the
      // shader for the actual calculation
      shader.bind(gl2);
         shader.setUniformf(gl2, "width", viewWidth);
         shader.setUniformf(gl2, "height", viewHeight);
         shader.setUniformf(gl2, "a", a);
         shader.setUniformf(gl2, "b", b);
         shader.setUniformf(gl2, "c", c);
         
         gl2.glBegin(GL2.GL_QUADS);
            gl2.glVertex3d(0, 0, 0);
            gl2.glVertex3d(viewWidth, 0, 0);
            gl2.glVertex3d(viewWidth, viewHeight, 0);
            gl2.glVertex3d(0, viewHeight, 0);
         gl2.glEnd();
      shader.unbind(gl2);
      
      a += 0.0006f;
      b += 0.0006f;
   }
   
   
   @Override
   public void init(GLAutoDrawable a) {
      super.init(a);
      
      GL2 gl2 = a.getGL().getGL2();
      
      // Initialize the plasma shader
      shader = new ShaderObj();
      shader.createShader(gl2, "shader\\vert_plasma.glsl", GL2.GL_VERTEX_SHADER);
      shader.createShader(gl2, "shader\\frag_plasma.glsl", GL2.GL_FRAGMENT_SHADER);
      shader.createProgram(gl2);
      shader.linkProgram(gl2);      
      
   }
   
   
   public void resetData() {
      a = (float)(Math.random())*20.0f;
      b = (float)(Math.random())*10.0f-10.0f;
      c = (float)(Math.random())*40.0f;
   }
   
   
   public ShaderObj shader;
   
   public float a = 2.5f;
   public float b = 6.0f;
   public float c = 2.0f;

}
