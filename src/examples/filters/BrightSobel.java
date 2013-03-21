package examples.filters;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.texture.Texture;

import graphics.FrameBufferTexture;
import graphics.GraphicUtil;
import base.JOGLBase;

public class BrightSobel extends JOGLBase {
   
   @Override
   public void render(GLAutoDrawable a) {
      // TODO Auto-generated method stub
      GL2 gl2 = a.getGL().getGL2();
      fbt1.startRecording(gl2);
         bright.render(a);
      fbt1.stopRecording(gl2); 
      
      sobel.textureID = fbt1.texture_ID;
      sobel.render(a);
      
   }
   
   
   
   public void init(GLAutoDrawable a) {
      super.init(a);    
      
      GL2 gl2 = a.getGL().getGL2();
      
      fbt1 = new FrameBufferTexture();
      fbt1.TEXTURE_SIZE_W = viewWidth;
      fbt1.TEXTURE_SIZE_H = viewHeight;
      fbt1.init(gl2, viewWidth, viewHeight);
      vao = GraphicUtil.createVAO(gl2, viewWidth, viewHeight);      
      
      sobel = new Sobel();
      sobel.setView(viewX, viewY, viewWidth, viewHeight);
      sobel.init(a);
      
      
      bright = new Bright();
      bright.setView(viewX, viewY, viewWidth, viewHeight);
      bright.init(a);
      
      // Little hack 
      sobel.vao = GraphicUtil.createVAO(gl2, viewWidth, viewHeight);
      
      
   }
   
   
   public void handleKeyPress(int key, char c) {
      super.handleKeyPress(key, c);
      sobel.handleKeyPress(key, c);
      bright.handleKeyPress(key, c);
   }   

   
   public Sobel sobel;
   public FrameBufferTexture fbt1;
   public Bright bright;
   public Texture texture;
   
   
   public int vao;



}
