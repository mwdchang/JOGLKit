package examples.danielstyle;

import java.awt.Color;
import java.awt.Font;

import graphics.GraphicUtil;
import graphics.ShaderObj;
import graphics.TextureFont;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.opengl.util.texture.Texture;

import base.JOGLBase;

////////////////////////////////////////////////////////////////////////////////
// Composing a single picture with a bunch of polaroid photos
////////////////////////////////////////////////////////////////////////////////
public class Polaroid extends JOGLBase {
   
   public class Frame{
      public float x, y, width, height, angle;   
   }
   
   public Texture texture;
   public String fileName = "img\\IMG_0917.jpg";
   public boolean needToFlipCoord = false;   

   public float tX = 300;
   public float tY = 300;
   
   public float angle = 0;
   
   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      angle++;
      
      GraphicUtil.setOrthonormalView(gl2, 0, viewWidth, 0, viewHeight, -10, 10);
      
      gl2.glColor4d(1, 1, 1, 1);
      gl2.glEnable(GL2.GL_TEXTURE_2D);
      gl2.glActiveTexture(GL2.GL_TEXTURE0);
      
      
      float D2R = (float)Math.PI/180.0f;
      
      for (int i=0; i < frameArray.length; i++) {
         tX = frameArray[i].x;
         tY = frameArray[i].y;
         angle = frameArray[i].angle;
         
         float tWidth  = frameArray[i].width;
         float tHeight = frameArray[i].height;
         float rX1 =  - tWidth;
         float rX2 =  + tWidth;
         float rY1 =  - tHeight;
         float rY2 =  + tHeight;
         
         float x1 = (float)(rX1*Math.cos(angle*D2R)) - (float)(rY1*Math.sin(angle*D2R));
         float y1 = (float)(rX1*Math.sin(angle*D2R)) + (float)(rY1*Math.cos(angle*D2R));
         
         float x2 = (float)(rX2*Math.cos(angle*D2R)) - (float)(rY1*Math.sin(angle*D2R));
         float y2 = (float)(rX2*Math.sin(angle*D2R)) + (float)(rY1*Math.cos(angle*D2R));
         
         float x3 = (float)(rX2*Math.cos(angle*D2R)) - (float)(rY2*Math.sin(angle*D2R));
         float y3 = (float)(rX2*Math.sin(angle*D2R)) + (float)(rY2*Math.cos(angle*D2R));
         
         float x4 = (float)(rX1*Math.cos(angle*D2R)) - (float)(rY2*Math.sin(angle*D2R));
         float y4 = (float)(rX1*Math.sin(angle*D2R)) + (float)(rY2*Math.cos(angle*D2R));
         
         // Draw the outer frame
         gl2.glDisable(GL2.GL_TEXTURE_2D);
         gl2.glColor4d(1, 1, 1, 1);
         gl2.glBegin(GL2.GL_QUADS);
            gl2.glVertex2d(1.1*x1+tX, 1.1*y1+tY);
            gl2.glVertex2d(1.1*x2+tX, 1.1*y2+tY);
            gl2.glVertex2d(1.1*x3+tX, 1.1*y3+tY);
            gl2.glVertex2d(1.1*x4+tX, 1.1*y4+tY);
         gl2.glEnd();
         gl2.glColor4d(0.7, 0.7, 0.7, 1);
         gl2.glBegin(GL2.GL_LINE_LOOP);
            gl2.glVertex2d(1.1*x1+tX, 1.1*y1+tY);
            gl2.glVertex2d(1.1*x2+tX, 1.1*y2+tY);
            gl2.glVertex2d(1.1*x3+tX, 1.1*y3+tY);
            gl2.glVertex2d(1.1*x4+tX, 1.1*y4+tY);
         gl2.glEnd();

         
         // Draw the text caption
         gl2.glPushMatrix();
            gl2.glTranslated(1.1*x1+tX, 1.1*y1+tY, 0);
            gl2.glRotated(angle, 0, 0, 1);
            textureFont.anchorX = 0;
            textureFont.anchorY = 0;
            textureFont.render(gl2);
         gl2.glPopMatrix();
        
         // Draw the picture
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
      
      
   }
   
   @Override
   public void init(GLAutoDrawable a) {
      super.init(a);
      GL2 gl2 = a.getGL().getGL2();
      texture = GraphicUtil.loadTexture(gl2, fileName);
      needToFlipCoord = texture.getMustFlipVertically();
      
      shader = new ShaderObj();
      shader.createShader(gl2, "shader\\vert_imageProcessing.glsl", GL2.GL_VERTEX_SHADER);
      shader.createShader(gl2, "shader\\frag_blur.glsl", GL2.GL_FRAGMENT_SHADER);
      shader.createProgram(gl2);
      shader.linkProgram(gl2);
      shader.bindFragColour(gl2, "outColour");  
      
      textureFont = new TextureFont();
      
      String str = "Coffee + Tea = \u263A";
      double dim[] = GraphicUtil.getFontDim(str, fontArial);
      textureFont.width = (float)dim[0]+30.0f;
      textureFont.height = (float)dim[1];
      textureFont.anchorX = (float)100;
      textureFont.anchorY = (float)100;
      textureFont.addMark(str, Color.MAGENTA, fontArial, 20, 2);
      
      for (int i=0; i < frameArray.length; i++) {
         frameArray[i] = new Frame();   
         frameArray[i].angle = (float)Math.random()*180;
         frameArray[i].width = 150;
         frameArray[i].height = 150;
         frameArray[i].x = (float)Math.random()*this.viewWidth;
         frameArray[i].y = (float)Math.random()*this.viewHeight;
      }
      
   }
   
   
   public boolean tempHack = false;
   public int sindex = -1;
   
   @Override
   public void handleMouseDown() {
      // Detection is the reverse order of rendering
      for (int i=frameArray.length-1; i >= 0; i--) {
         float fX = frameArray[i].x;
         float fY = frameArray[i].y;
         if (Math.sqrt( (this.posX - fX)*(this.posX - fX) + (this.posY - fY)*(this.posY - fY)) < 70) { 
            sindex = i;
            break;
         }
      }
   }
   
   @Override
   public void handleMouseUp() {
      sindex = -1;
   }
   
   @Override
   public void handleMouseDrag(int mouseX, int mouseY) {
      super.handleMouseDrag(mouseX, mouseY);
      
      if (sindex >= 0) {
         frameArray[sindex].x += (posX - this.oldPosX);   
         frameArray[sindex].y += (posY - this.oldPosY);   
      }
   }
   
   
   public Frame[] frameArray = new Frame[10];
   
   

   public Font fontArial = new Font("Arial", Font.PLAIN, 16);
   public ShaderObj shader;
   public TextureFont textureFont;
}
