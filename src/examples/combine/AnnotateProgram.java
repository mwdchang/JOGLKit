package examples.combine;

import java.util.Vector;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import examples.Annotate;
import examples.danielstyle.SqRot;
import graphics.DCTriple;

import base.JOGLBase;

public class AnnotateProgram extends JOGLBase {
   
   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      
      gl2.glViewport(program.viewX, program.viewY, program.viewWidth, program.viewHeight);
      program.render(a);
      
      gl2.glViewport(annotate.viewX, annotate.viewY, annotate.viewWidth, annotate.viewHeight);
      annotate.render(a);
   }
   
   public void init(GLAutoDrawable g) {
      super.init(g);
      annotate = new Annotate();
      program  = new SqRot();
      
      program.setView(100, 100, 300, 300);
      program.doPause = false;
      
      annotate.init(g);
      program.init(g);
   }
   
   public void handleMouseUp() {
      super.handleMouseUp();
      annotate.handleMouseUp();
   }
   
   
   public void handleMouseDrag(int mouseX, int mouseY) {
      super.handleMouseDrag(mouseX, mouseY);
      annotate.handleMouseDrag(mouseX, mouseY);
   }
   
   public void handleKeyPress(int key, char c) {
System.out.println("blah........................." + c);      
      super.handleKeyPress(key, c);
      annotate.handleKeyPress(key, c);
      program.handleKeyPress(key, c);
   }
      
   
   
   public Annotate annotate;
   public SqRot    program;

}
