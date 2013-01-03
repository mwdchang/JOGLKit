package examples;

import graphics.GraphicUtil;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.texture.Texture;

import base.JOGLBase;

////////////////////////////////////////////////////////////////////////////////
// Simple example of displaying an image as a texture
////////////////////////////////////////////////////////////////////////////////
public class ImageLoader extends JOGLBase {
   
   public Texture texture;
   public String fileName = "C:\\Users\\Daniel\\Pictures\\IMG_0917.jpg";
   public boolean needToFlipCoord = false;

   public ImageLoader() {
      fileName = "C:\\Users\\Daniel\\Pictures\\IMG_0917.jpg";
   }
   public ImageLoader(String file) {
      fileName = file;
   }
   
   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      GraphicUtil.setOrthonormalView(gl2, 0, viewWidth, 0, viewHeight, -10, 10);
      
      gl2.glEnable(GL2.GL_TEXTURE_2D);
      texture.enable(gl2);
      gl2.glBegin(GL2.GL_QUADS);
         if (needToFlipCoord) {
            gl2.glTexCoord2d(0, 1); gl2.glVertex2d(0, 0);
            gl2.glTexCoord2d(1, 1); gl2.glVertex2d(viewWidth, 0);
            gl2.glTexCoord2d(1, 0); gl2.glVertex2d(viewWidth, viewHeight);
            gl2.glTexCoord2d(0, 0); gl2.glVertex2d(0, viewHeight);
         } else {
            gl2.glTexCoord2d(0, 0); gl2.glVertex2d(0, 0);
            gl2.glTexCoord2d(1, 0); gl2.glVertex2d(viewWidth, 0);
            gl2.glTexCoord2d(1, 1); gl2.glVertex2d(viewWidth, viewHeight);
            gl2.glTexCoord2d(0, 1); gl2.glVertex2d(0, viewHeight);
         }
      gl2.glEnd();
      texture.disable(gl2);
   }
   
   
   @Override
   public void init(GLAutoDrawable a) {
      super.init(a);
      GL2 gl2 = a.getGL().getGL2();
      texture = GraphicUtil.loadTexture(gl2, fileName);
      needToFlipCoord = texture.getMustFlipVertically();
   }
   

}