package examples.danielstyle;

import graphics.GraphicUtil;
import graphics.ShaderObj;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.texture.Texture;

import base.JOGLBase;

public class Polaroid extends JOGLBase {
   
   public class POI {
      public float x, y, width, height, angle;   
   }
   
   public Texture texture;
   public String fileName = "C:\\Users\\Daniel\\Pictures\\IMG_0917.jpg";
   public boolean needToFlipCoord = false;   

   
   public float angle = 0;
   
   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      angle++;
      
      GraphicUtil.setOrthonormalView(gl2, 0, viewWidth, 0, viewHeight, -10, 10);
      
      gl2.glColor4d(1, 1, 1, 1);
      gl2.glEnable(GL2.GL_TEXTURE_2D);
      gl2.glActiveTexture(GL2.GL_TEXTURE0);
      
      
      float tX = 300;
      float tY = 300;
      float tWidth  = 150;
      float tHeight = 150;
      
      float rX1 =  - tWidth;
      float rX2 =  + tWidth;
      float rY1 =  - tHeight;
      float rY2 =  + tHeight;
      
      
      float D2R = (float)Math.PI/180.0f;
      
      
      float x1 = (float)(rX1*Math.cos(angle*D2R)) - (float)(rY1*Math.sin(angle*D2R));
      float y1 = (float)(rX1*Math.sin(angle*D2R)) + (float)(rY1*Math.cos(angle*D2R));
      
      float x2 = (float)(rX2*Math.cos(angle*D2R)) - (float)(rY1*Math.sin(angle*D2R));
      float y2 = (float)(rX2*Math.sin(angle*D2R)) + (float)(rY1*Math.cos(angle*D2R));
      
      float x3 = (float)(rX2*Math.cos(angle*D2R)) - (float)(rY2*Math.sin(angle*D2R));
      float y3 = (float)(rX2*Math.sin(angle*D2R)) + (float)(rY2*Math.cos(angle*D2R));
      
      float x4 = (float)(rX1*Math.cos(angle*D2R)) - (float)(rY2*Math.sin(angle*D2R));
      float y4 = (float)(rX1*Math.sin(angle*D2R)) + (float)(rY2*Math.cos(angle*D2R));
      
      
      gl2.glDisable(GL2.GL_TEXTURE_2D);
      gl2.glColor4d(1, 1, 1, 1);
      gl2.glBegin(GL2.GL_QUADS);
         gl2.glVertex2d(1.1*x1+tX, 1.1*y1+tY);
         gl2.glVertex2d(1.1*x2+tX, 1.1*y2+tY);
         gl2.glVertex2d(1.1*x3+tX, 1.1*y3+tY);
         gl2.glVertex2d(1.1*x4+tX, 1.1*y4+tY);
      gl2.glEnd();
      
      gl2.glEnable(GL2.GL_TEXTURE_2D);
      gl2.glDisable(GL2.GL_DEPTH_TEST);
      texture.enable(gl2);
      texture.bind(gl2);
      //gl2.glTranslated( tX, tY, 0);
      gl2.glBegin(GL2.GL_QUADS);
         if (needToFlipCoord) {
            gl2.glTexCoord2d((x1+tX)/(float)viewWidth,  1-(y1+tY)/(float)viewHeight); gl2.glVertex2d(x1+tX, y1+tY);
            gl2.glTexCoord2d((x2+tX)/(float)viewWidth,  1-(y2+tY)/(float)viewHeight); gl2.glVertex2d(x2+tX, y2+tY);
            gl2.glTexCoord2d((x3+tX)/(float)viewWidth,  1-(y3+tY)/(float)viewHeight); gl2.glVertex2d(x3+tX, y3+tY);
            gl2.glTexCoord2d((x4+tX)/(float)viewWidth,  1-(y4+tY)/(float)viewHeight); gl2.glVertex2d(x4+tX, y4+tY);
            /* 
            gl2.glTexCoord2d(0, 1); gl2.glVertex2d(0, 0);
            gl2.glTexCoord2d(1, 1); gl2.glVertex2d(viewWidth, 0);
            gl2.glTexCoord2d(1, 0); gl2.glVertex2d(viewWidth, viewHeight);
            gl2.glTexCoord2d(0, 0); gl2.glVertex2d(0, viewHeight);
            */
         } else {
            /*
            gl2.glTexCoord2d(0, 0); gl2.glVertex2d(0, 0);
            gl2.glTexCoord2d(1, 0); gl2.glVertex2d(viewWidth, 0);
            gl2.glTexCoord2d(1, 1); gl2.glVertex2d(viewWidth, viewHeight);
            gl2.glTexCoord2d(0, 1); gl2.glVertex2d(0, viewHeight);
            */
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
      
      shader = new ShaderObj();
      shader.createShader(gl2, "src\\shader\\vert_imageProcessing.glsl", GL2.GL_VERTEX_SHADER);
      shader.createShader(gl2, "src\\shader\\frag_blur.glsl", GL2.GL_FRAGMENT_SHADER);
      shader.createProgram(gl2);
      shader.linkProgram(gl2);
      shader.bindFragColour(gl2, "outColour");  
      
   }

   public ShaderObj shader;
}
