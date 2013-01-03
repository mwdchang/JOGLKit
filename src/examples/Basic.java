package examples;

import graphics.DrawUtil;
import graphics.GraphicUtil;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import base.JOGLBase;

////////////////////////////////////////////////////////////////////////////////
// Pretty much the most basic setup with the toolkit environment
////////////////////////////////////////////////////////////////////////////////
public class Basic extends JOGLBase {
   @Override
   public void render(GLAutoDrawable g) {
      GL2 gl2 = g.getGL().getGL2();
      gl2.glColor4d(0.5, 0.5, 0, 1);
      DrawUtil.drawQuadFan(gl2, 0, 0, -20, 4, 2);
   }
}
