package examples.filters;

import graphics.GraphicUtil;
import graphics.ShaderObj;

import java.awt.event.KeyEvent;
import java.io.File;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JFileChooser;

import com.jogamp.opengl.util.texture.Texture;

import base.Config;
import base.JOGLBase;

public class Bright extends JOGLBase {

   public int textureID = -1;
   public Texture texture;
   public ShaderObj shader;
   public int vao;
   public float threshold = 0.0f;
   public boolean reload = false;
   public File image = new File(Config.SAMPLE_IMAGE);
   
   public long counter = 0;

   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      if (reload) { 
         texture = GraphicUtil.loadTexture(gl2, image.getAbsolutePath());
         reload = false;
      }
      
      counter++;
      
      //threshold = (float)Math.abs(Math.sin( 3.14*counter / 180.0f));
      
      
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
         
         shader.setUniformf(gl2, "thresholdBright", threshold);
         
         // Get the texture binded to a target
         gl2.glActiveTexture(GL2.GL_TEXTURE0);
         if (textureID > 0) {
            gl2.glBindTexture(GL2.GL_TEXTURE_2D, textureID);
            shader.setUniform1i(gl2, "tex", 0);
         } else {
            texture.enable(gl2);
            texture.bind(gl2);
            shader.setUniform1i(gl2, "tex", 0);
            texture.disable(gl2);
         }
         
         
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
      shader.createShader(gl2, "shader\\vert_imageProcessing.glsl", GL2.GL_VERTEX_SHADER);
      shader.createShader(gl2, "shader\\frag_bright.glsl", GL2.GL_FRAGMENT_SHADER);
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
      
   }
   
   
   public void handleKeyPress(int key, char c) {
      super.handleKeyPress(key, c);
      if (c == 'f') {
      } else if( key == KeyEvent.VK_UP) {
         threshold += 0.05; 
         if (threshold >= 1.0f) threshold = 1.0f;
         System.out.println("threshold: " + threshold);
      } else if ( key == KeyEvent.VK_DOWN) {
         threshold -= 0.05; 
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
