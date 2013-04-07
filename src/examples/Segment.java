package examples;

import java.io.File;

import graphics.GraphicUtil;
import graphics.ShaderObj;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JFileChooser;

import com.jogamp.opengl.util.texture.Texture;

import base.JOGLBase;

////////////////////////////////////////////////////////////////////////////////
// Basic Edge Detector
////////////////////////////////////////////////////////////////////////////////
public class Segment extends JOGLBase {
   
   public Texture texture;
   public ShaderObj shader;
   public int vao;
   public float threshold = 1.0f;
   public boolean reload = false;
   public File image = new File("C:\\Users\\Daniel\\Dropbox\\temp\\Toolkit\\IMG_0917.jpg");
   public FrameBuffer2 clock = new FrameBuffer2();
   
   public Segment(String fname) {
      image = new File(fname);   
   }

   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      //gl2.glViewport(200, 200, 300, 300);
      
      if (reload) { 
         texture = GraphicUtil.loadTexture(gl2, image.getAbsolutePath());
         reload = false;
      }
      
      //gl2.glTranslated(-10, -10,  -40);
      GraphicUtil.setOrthonormalView(gl2, 0, 1, 0, 1, -1, 1);
      
      gl2.glEnable(GL2.GL_TEXTURE_2D);
      
      gl2.glBindVertexArray(vao);
      shader.bind(gl2);
         float buffer[] = new float[16];
         gl2.glGetFloatv(GL2.GL_PROJECTION_MATRIX, buffer, 0);
         shader.setUniform4x4(gl2, "projection_matrix", buffer);
         gl2.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, buffer, 0);
         shader.setUniform4x4(gl2, "modelview_matrix", buffer);       
         shader.setUniform1i(gl2, "mx", this.posX);
         shader.setUniform1i(gl2, "my", this.posY);
         
         // Get the texture binded to a target
         gl2.glActiveTexture(GL2.GL_TEXTURE0);
         texture.enable(gl2);
         texture.bind(gl2);
         shader.setUniform1i(gl2, "tex", 0);
         texture.disable(gl2);
         
         gl2.glDrawArrays(GL2.GL_QUADS, 0, 4);
      shader.unbind(gl2);
      gl2.glBindVertexArray(0);
      
      /* 
      gl2.glViewport(clock.viewX, clock.viewY, clock.viewWidth, clock.viewHeight); 
      gl2.glDisable(GL2.GL_TEXTURE_2D);
      gl2.glDisable(GL2.GL_DEPTH_TEST);
      clock.render(a);
      */
   }
   
   
   @Override
   public void init(GLAutoDrawable a) {
      super.init(a);
      GL2 gl2 = a.getGL().getGL2();
      
      texture = GraphicUtil.loadTexture(gl2, image.getAbsolutePath());
      
      
      shader = new ShaderObj();
      shader.createShader(gl2, "shader\\vert_imageProcessing.glsl", GL2.GL_VERTEX_SHADER);
      shader.createShader(gl2, "shader\\frag_segment.glsl", GL2.GL_FRAGMENT_SHADER);
      shader.createProgram(gl2);
      shader.linkProgram(gl2);
      shader.bindFragColour(gl2, "outColour");      
      
      if (texture.getMustFlipVertically()) {
         vao = GraphicUtil.createVAO(gl2, 1, 1, new float[] {
               0.0f, 1.0f,
               1.0f, 1.0f,
               1.0f, 0.0f,
               0.0f, 0.0f
         });
      } else {
         vao = GraphicUtil.createVAO(gl2, viewWidth, viewHeight);
      }
      
      clock.setView(400, 400, 400, 400);
      clock.init(a);
      System.out.println("threshod: " + threshold);
   }
   
   
   public void handleKeyPress(int key, char c) {
      super.handleKeyPress(key, c);
      clock.handleKeyPress(key, c);
   }
   
   public void handleMouseClick() {
      JFileChooser jfc = new JFileChooser("C://users//daniel//Pictures");   
      jfc.showOpenDialog(null);
      image = jfc.getSelectedFile();
      if (image == null) return;
      threshold = 1.0f;
      reload = true;
   }

}
