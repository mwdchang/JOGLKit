package examples;

import graphics.DCTriple;
import graphics.DrawUtil;

import java.util.Vector;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;



public class Annotate extends ImageLoader{
   
   public Annotate() {
      fileName = "C:\\Users\\Daniel\\Pictures\\IMG_0917.jpg";
   }
   public Annotate(String file) {
      fileName = file;
   }
   
   
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      gl2.glColor4d(1.0, 1.0, 1.0, 1.0);
      super.render(a);     
      
      
      gl2.glDisable(GL2.GL_TEXTURE_2D);
      gl2.glDisable(GL2.GL_DEPTH_TEST);
      gl2.glEnable(GL2.GL_BLEND);
      gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
      gl2.glColor4d(1.0, 0.0, 0.0, 0.5);
      gl2.glLineWidth(3.0f);
      
      
      gl2.glBegin(GL2.GL_LINE_STRIP);
      for (int i=0; i < marks.size(); i++) {
         DCTriple p = marks.elementAt(i);
         gl2.glVertex2d(p.x, p.y);
      }
      gl2.glEnd();
      
      if (annotation.size() > 0) {
         //for (Vector<DCTriple> m : annotation) {
         for (int idx = 0; idx < annotation.size(); idx++) {
            Vector<DCTriple> m = annotation.elementAt(idx);
            gl2.glBegin(GL2.GL_LINE_STRIP);
            for (int i=0; i < m.size(); i++) {
               DCTriple p = m.elementAt(i);
               gl2.glVertex2d(p.x, p.y);
            }
            gl2.glEnd();
         }
      }
      
   }
   
   public void handleMouseUp() {
      super.handleMouseUp();
      if (marks.size() > 1) annotation.add( marks ); 
      marks = new Vector<DCTriple>();
   }
   
   
   public void handleMouseDrag(int mouseX, int mouseY) {
      super.handleMouseDrag(mouseX, mouseY);
      marks.add(new DCTriple(this.posX, this.posY, 0));
   }
   
   public void handleKeyPress(int key, char c) {
      super.handleKeyPress(key, c);
      if (c == 'r') {
         marks.clear();   
         annotation.clear();
      }
   }
   
   
   public Vector<Vector<DCTriple>> annotation = new Vector<Vector<DCTriple>>();
   public Vector<DCTriple> marks = new Vector<DCTriple>();
}
