package examples.combine;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import examples.ImageLoader;
import examples.Segment;

import base.JOGLBase;

public class CombineDemo extends JOGLBase {
   
   public CombineDemo(String f) { fname = f; }

   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      gl2.glViewport(0, 0, this.winWidth, this.winHeight);
      loader.render(a);
      
      gl2.glEnable(GL2.GL_BLEND);
      gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
      gl2.glDisable(GL2.GL_DEPTH_TEST);
      
      gl2.glViewport(segment1.viewX, segment1.viewY, segment1.viewWidth, segment1.viewHeight);
      segment1.posX = posX;
      segment1.posY = posY;
      segment1.render(a);
      
      gl2.glViewport(segment2.viewX, segment2.viewY, segment2.viewWidth, segment2.viewHeight);
      segment2.posX = posX;
      segment2.posY = posY;
      segment2.render(a);
   }
   
   public void init(GLAutoDrawable a) {
      super.init(a);   
      
      loader = new ImageLoader( fname );
      segment1 = new Segment("C:\\Users\\Daniel\\Dropbox\\temp\\Toolkit\\IMG_0917.jpg");
      segment1.setView(50, 50, 450, 450);
      
      segment2 = new Segment("C:\\Users\\Daniel\\Dropbox\\temp\\Toolkit\\cap.PNG");
      segment2.setView(550, 50, 700, 350);
      
      loader.init(a);
      segment1.init(a);
      segment2.init(a);
   }
   
   
   public Segment segment1;
   public Segment segment2;
   public ImageLoader loader;
   public String fname;

}
