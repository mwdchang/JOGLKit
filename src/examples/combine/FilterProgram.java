package examples.combine;

import java.util.Vector;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import examples.Annotate;
import examples.ImageLoader;
import examples.danielstyle.SqRot;
import examples.filters.Bright;
import examples.filters.BrightSobel;
import examples.filters.Sobel;
import graphics.DCTriple;

import base.JOGLBase;

public class FilterProgram extends JOGLBase {
   
   
   public FilterProgram(String name) {
      loader = new ImageLoader( name );
   }
   
   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      gl2.glDisable(GL2.GL_DEPTH_TEST);
      gl2.glDisable(GL2.GL_BLEND);
      gl2.glColor4d(1, 1, 1, 1);
      
      gl2.glViewport(0, 0, this.winWidth, this.winHeight);
      loader.render(a);
      
      gl2.glViewport(bright.viewX, bright.viewY, bright.viewWidth, bright.viewHeight);
      bright.render(a);
      
      gl2.glViewport(sobel.viewX, sobel.viewY, sobel.viewWidth, sobel.viewHeight);
      sobel.render(a);
      
      
      gl2.glViewport(brightSobel.viewX, brightSobel.viewY, brightSobel.viewWidth, brightSobel.viewHeight);
      brightSobel.render(a);
   }
   
   
   public void init(GLAutoDrawable g) {
      super.init(g);
      
      loader.init(g);
      
      bright= new Bright();
      bright.setView(50, 250, 250, 250);
      
      sobel = new Sobel();
      sobel.setView(520, 250, 250, 250);
      
      brightSobel = new BrightSobel();
      brightSobel.setView(950, 250, 250, 250);
      
      sobel.init(g);
      bright.init(g);
      brightSobel.init(g);
   }
   
   public void handleMouseUp() {
      super.handleMouseUp();
      sobel.handleMouseUp();
   }
   
   
   public void handleMouseDrag(int mouseX, int mouseY) {
      super.handleMouseDrag(mouseX, mouseY);
      sobel.handleMouseDrag(mouseX, mouseY);
   }
   
   public void handleKeyPress(int key, char c) {
      super.handleKeyPress(key, c);
      sobel.handleKeyPress(key, c);
      bright.handleKeyPress(key, c);
      brightSobel.handleKeyPress(key, c);
   }
      
   
   public ImageLoader loader; 
   public Sobel sobel;
   public Bright bright;
   public BrightSobel brightSobel;

}
