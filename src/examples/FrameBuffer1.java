package examples;

import java.awt.event.KeyEvent;

import examples.danielstyle.Plasma;
import graphics.FrameBufferTexture;
import graphics.GraphicUtil;
import graphics.ShaderObj;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JFileChooser;

import com.jogamp.opengl.util.texture.Texture;

import base.JOGLBase;

public class FrameBuffer1 extends JOGLBase {

   @Override
   public void render(GLAutoDrawable a) {
      counter ++;
      GL2 gl2 = a.getGL().getGL2();   
      
      float buffer[] = new float[16];
      
      // Bootstrap the first shader into a texture before passing it into the 
      // second shader vao
      fbt1.startRecording(gl2);
         c.display(a);
      fbt1.stopRecording(gl2);   
      
      
      
      GraphicUtil.setOrthonormalView(gl2, 0, viewWidth, 0, viewHeight, -1, 1);
      gl2.glEnable(GL2.GL_TEXTURE_2D);
      gl2.glBindVertexArray(vaoSobel);
      shaderSobel.bind(gl2);
         gl2.glGetFloatv(GL2.GL_PROJECTION_MATRIX, buffer, 0);
         shaderSobel.setUniform4x4(gl2, "projection_matrix", buffer);
         gl2.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, buffer, 0);
         shaderSobel.setUniform4x4(gl2, "modelview_matrix", buffer);       
         
         shaderSobel.setUniformf(gl2, "thresholdSobel", thresholdSobel);
         shaderSobel.setUniformf(gl2, "width", viewWidth);
         shaderSobel.setUniformf(gl2, "height", viewHeight);
         
         // Get the texture binded to a target
         gl2.glActiveTexture(GL2.GL_TEXTURE0);
         gl2.glBindTexture(GL2.GL_TEXTURE_2D, fbt1.texture_ID);
         shaderSobel.setUniform1i(gl2, "tex", 0);
         
         gl2.glDrawArrays(GL2.GL_QUADS, 0, 4);
      shaderSobel.unbind(gl2);
      gl2.glBindVertexArray(0);
      
   }
   
   public void init(GLAutoDrawable a) {
      super.init(a);  
      
      GL2 gl2 = a.getGL().getGL2();
      
      fbt1 = new FrameBufferTexture();
      fbt1.TEXTURE_SIZE_W = viewWidth;
      fbt1.TEXTURE_SIZE_H = viewHeight;
      fbt1.init(gl2, viewWidth, viewHeight);
      
      shaderSobel = new ShaderObj();
      shaderSobel.createShader(gl2, "shader\\vert_imageProcessing.glsl", GL2.GL_VERTEX_SHADER);
      shaderSobel.createShader(gl2, "shader\\frag_sobel.glsl", GL2.GL_FRAGMENT_SHADER);
      shaderSobel.createProgram(gl2);
      shaderSobel.linkProgram(gl2);
      shaderSobel.bindFragColour(gl2, "outColour");        
      vaoSobel = GraphicUtil.createVAO(gl2, viewWidth, viewHeight);
      
      
      
      // Bootstrap another program's initialization
      c.reshape(a, 0, 0, viewWidth, viewHeight);
      c.init(a);
   }
   
   
   public void handleKeyPress(int key, char c) {
      super.handleKeyPress(key, c);
      if (c == 'f') {
      } else if( key == KeyEvent.VK_UP) {
         thresholdSobel += 0.1f; 
         System.out.println("Sobel threshold : " + thresholdSobel);
      } else if ( key == KeyEvent.VK_DOWN) {
         thresholdSobel -= 0.1f; 
         if (thresholdSobel <= 0) thresholdSobel = 0;
         System.out.println("Sobel threshold : " + thresholdSobel);
      } 
      this.c.handleKeyPress(key, c);
   }
   
   public void handleMouseClick() {
   }   
   
   
   
   
   public Plasma c = new Plasma();
   public FrameBufferTexture fbt1;
   public ShaderObj shaderSobel;
   public int vaoSobel;
   public Texture texture;
   public float thresholdBright = 0;;
   public float thresholdSobel = 1;;
   public long counter = 0;

}
