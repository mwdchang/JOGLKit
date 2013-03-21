package examples.filters;

import graphics.GraphicUtil;
import graphics.ShaderObj;

import java.awt.event.KeyEvent;
import java.io.File;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JFileChooser;

import com.jogamp.opengl.util.texture.Texture;

import base.JOGLBase;

public class Blur extends JOGLBase {

   public Texture texture;
   public ShaderObj shader;
   public int vao;
   public int threshold = 1;
   public int texture1D;
   public boolean reload = false;
   public File image = new File("C:\\Users\\Daniel\\dropbox\\temp\\Toolkit\\IMG_0917.jpg");
   

   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      if (reload) { 
         texture = GraphicUtil.loadTexture(gl2, image.getAbsolutePath());
         reload = false;
      }
      
      //gl2.glTranslated(-10, -10,  -40);
      GraphicUtil.setOrthonormalView(gl2, 0, viewWidth, 0, viewHeight, -1, 1);
      
      gl2.glEnable(GL2.GL_TEXTURE_2D);
      
      gl2.glBindVertexArray(vao);
      shader.bind(gl2);
         float buffer[] = new float[16];
         gl2.glGetFloatv(GL2.GL_PROJECTION_MATRIX, buffer, 0);
         shader.setUniform4x4(gl2, "projection_matrix", buffer);
         gl2.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, buffer, 0);
         shader.setUniform4x4(gl2, "modelview_matrix", buffer);       
         
         shader.setUniform1i(gl2, "threshold", threshold);
         shader.setUniformf(gl2, "width", viewWidth);
         shader.setUniformf(gl2, "height", viewHeight);
         
         // Get the texture binded to a target
         gl2.glActiveTexture(GL2.GL_TEXTURE0);
         texture.enable(gl2);
         texture.bind(gl2);
         shader.setUniform1i(gl2, "tex", 0);
         texture.disable(gl2);
         
         gl2.glActiveTexture(GL2.GL_TEXTURE1);
         gl2.glBindTexture(GL2.GL_TEXTURE_1D, texture1D);
         shader.setUniform1i(gl2, "tex1D", 1);
         
         gl2.glDrawArrays(GL2.GL_QUADS, 0, 4);
      shader.unbind(gl2);
      gl2.glBindVertexArray(0);
      
      
   }
   
   
   @Override
   public void init(GLAutoDrawable a) {
      super.init(a);
      GL2 gl2 = a.getGL().getGL2();
      
      texture = GraphicUtil.loadTexture(gl2, image.getAbsolutePath());
      
      
      shader = new ShaderObj();
      shader.createShader(gl2, "src\\shader\\vert_imageProcessing.glsl", GL2.GL_VERTEX_SHADER);
      shader.createShader(gl2, "src\\shader\\frag_blur.glsl", GL2.GL_FRAGMENT_SHADER);
      shader.createProgram(gl2);
      shader.linkProgram(gl2);
      shader.bindFragColour(gl2, "outColour");      
      
      if (texture.getMustFlipVertically()) {
         vao = GraphicUtil.createVAO(gl2, viewWidth, viewHeight, new float[] {
               0.0f, 1.0f,
               1.0f, 1.0f,
               1.0f, 0.0f,
               0.0f, 0.0f
         });
      } else {
         vao = GraphicUtil.createVAO(gl2, viewWidth, viewHeight);
      }
      
      texture1D = GraphicUtil.gen1DTexture(gl2, 10);
   }
   
   
   public void handleKeyPress(int key, char c) {
      super.handleKeyPress(key, c);
      if (c == 'f') {
      } else if( key == KeyEvent.VK_UP) {
         threshold += 1; 
         if (threshold >= 10) threshold = 10;
         System.out.println("threshold: " + threshold);
      } else if ( key == KeyEvent.VK_DOWN) {
         threshold -= 1; 
         if (threshold <= 0) threshold = 0;
         System.out.println("threshold: " + threshold);
      }
   }
   
   public void handleMouseClick() {
      JFileChooser jfc = new JFileChooser("C://users//daniel//Pictures");   
      jfc.showOpenDialog(null);
      image = jfc.getSelectedFile();
      if (image == null) return;
      threshold = 1;
      reload = true;
   }

}
