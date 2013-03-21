package examples;

import java.awt.event.KeyEvent;
import java.io.File;

import graphics.FrameBufferTexture;
import graphics.GraphicUtil;
import graphics.ShaderObj;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JFileChooser;

import com.jogamp.opengl.util.texture.Texture;

import base.JOGLBase;

public class FrameBuffer2 extends JOGLBase {

   @Override
   public void render(GLAutoDrawable a) {
      counter ++;
      GL2 gl2 = a.getGL().getGL2();   
      
      if (reload) { 
         texture = GraphicUtil.loadTexture(gl2, image.getAbsolutePath());
         reload = false;
      }      
      
      
      thresholdBright = (float)Math.abs(Math.sin( 3.14*counter / 180.0f));
      
      float buffer[] = new float[16];
      
      // Bootstrap the first shader into a texture before passing it into the 
      // second shader vao
      fbt1.startRecording(gl2);
         basicClear(gl2);
         GraphicUtil.setOrthonormalView(gl2, 0, viewWidth, 0, viewHeight, -1, 1);
         
         gl2.glEnable(GL2.GL_TEXTURE_2D);
         
         gl2.glBindVertexArray(vaoBright);
         shaderBright.bind(gl2);
            gl2.glGetFloatv(GL2.GL_PROJECTION_MATRIX, buffer, 0);
            shaderBright.setUniform4x4(gl2, "projection_matrix", buffer);
            gl2.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, buffer, 0);
            shaderBright.setUniform4x4(gl2, "modelview_matrix", buffer);       
            
            shaderBright.setUniformf(gl2, "thresholdBright", thresholdBright);
            
            // Get the texture binded to a target
            gl2.glActiveTexture(GL2.GL_TEXTURE0);
            texture.enable(gl2);
            texture.bind(gl2);
            shaderBright.setUniform1i(gl2, "tex", 0);
            texture.disable(gl2);
            
            gl2.glDrawArrays(GL2.GL_QUADS, 0, 4);
         shaderBright.unbind(gl2);
         gl2.glBindVertexArray(0);         
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
      shaderSobel.createShader(gl2, "src\\shader\\vert_imageProcessing.glsl", GL2.GL_VERTEX_SHADER);
      shaderSobel.createShader(gl2, "src\\shader\\frag_sobel.glsl", GL2.GL_FRAGMENT_SHADER);
      shaderSobel.createProgram(gl2);
      shaderSobel.linkProgram(gl2);
      shaderSobel.bindFragColour(gl2, "outColour");        
      vaoSobel = GraphicUtil.createVAO(gl2, viewWidth, viewHeight);
      
      
      texture = GraphicUtil.loadTexture(gl2, image.getAbsolutePath());
      shaderBright = new ShaderObj();
      shaderBright.createShader(gl2, "src\\shader\\vert_imageProcessing.glsl", GL2.GL_VERTEX_SHADER);
      shaderBright.createShader(gl2, "src\\shader\\frag_bright.glsl", GL2.GL_FRAGMENT_SHADER);
      shaderBright.createProgram(gl2);
      shaderBright.linkProgram(gl2);
      shaderBright.bindFragColour(gl2, "outColour");      
      
      if (texture.getMustFlipVertically()) {
         vaoBright = GraphicUtil.createVAO(gl2, viewWidth, viewHeight, new float[] {
               0.0f, 1.0f,
               1.0f, 1.0f,
               1.0f, 0.0f,
               0.0f, 0.0f
         });
      } else {
         vaoBright = GraphicUtil.createVAO(gl2, viewWidth, viewHeight);
      }      
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
   }
   
   public void handleMouseClick() {
      JFileChooser jfc = new JFileChooser("C://users//daniel//Pictures");   
      jfc.showOpenDialog(null);
      image = jfc.getSelectedFile();
      if (image == null) return;
      reload = true;
   }   
   
   
   
   
   public FrameBufferTexture fbt1;
   public ShaderObj shaderSobel;
   public ShaderObj shaderBright;
   public int vaoSobel;
   public int vaoBright;
   public File image = new File("C:\\Users\\Daniel\\DropBox\\temp\\Toolkit\\cap.PNG");
   public boolean reload = false;
   public Texture texture;
   public float thresholdBright = 0;;
   public float thresholdSobel = 1;;
   public long counter = 0;

}
