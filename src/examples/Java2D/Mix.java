package examples.Java2D;

import java.awt.Color;
import java.awt.FlowLayout;

import graphics.DrawUtil;
import graphics.GraphicUtil;
import graphics.SwingTexture;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;

import base.AWTWindow;
import base.JOGLBase;

public class Mix extends JOGLBase {
   
   public static void main(String args[]) {
      AWTWindow base = new AWTWindow();
      base.setProgram(new Mix());
      base.run("Demo Runner", 800, 800);
   }   

   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      GraphicUtil.setPerspectiveView(gl2, winWidth/winHeight, 30.0f, 1.0f, 1000.0f);
      gl2.glTranslatef(0, 0, -80);
      
      //gl2.glDisable(GL2.GL_TEXTURE_2D);
      //gl2.glColor4d(1, 1, 1, 1);
      //DrawUtil.drawPie(gl2, 0, 0, 0, 1, 0, 350, 35);
      
      texture.enable(gl2);
      texture.bind(gl2);
      TextureCoords tc = texture.getImageTexCoords();
      gl2.glBegin(GL2.GL_QUADS);
         gl2.glTexCoord2f(tc.left(), tc.bottom());
         gl2.glVertex3f(0.0f, 0.0f, 0.0f);
         
         gl2.glTexCoord2f(tc.right(), tc.bottom());
         gl2.glVertex3f(10.0f, 0.0f, 0.0f);
         
         gl2.glTexCoord2f(tc.right(), tc.top());
         gl2.glVertex3f(10.0f, 10.0f, 0.0f);
         
         gl2.glTexCoord2f(tc.left(), tc.top());
         gl2.glVertex3f(0.0f, 10.0f, 0.0f);
      gl2.glEnd();
      texture.disable(gl2);       
      
      /*
      gl2.glEnable(GL2.GL_TEXTURE_2D);
      swingTexture.texture.enable(gl2);
      swingTexture.texture.bind(gl2);
         gl2.glTexCoord2d(0, 0); gl2.glVertex2d(0, 0);
         gl2.glTexCoord2d(1, 0); gl2.glVertex2d(1, 0);
         gl2.glTexCoord2d(1, 1); gl2.glVertex2d(1, 1);
         gl2.glTexCoord2d(0, 1); gl2.glVertex2d(0, 1);      
      swingTexture.texture.disable(gl2);
      */
      
   }
   
   public void init(GLAutoDrawable a) {
      super.init(a);
      
      GL2 gl2 = a.getGL().getGL2();
      
      swingTexture = new SwingTexture();
      swingTexture.setLayout(new FlowLayout());
      swingTexture.setSize(100, 100);
      swingTexture.setBackground(Color.LIGHT_GRAY);
      
      swingTexture.add(new JButton("Test1"));
      swingTexture.add(new JButton("Test2"));
      swingTexture.add(new JButton("Test3"));
      
      swingTexture.setSize(100, 100);
      swingTexture.validate();
      swingTexture.revalidate();
      
      
      
      debug = new JFrame("");
      debug.setSize(400, 400);
      debug.add(swingTexture);
      debug.setVisible(true);
      debug.setVisible(false);
      
      
      
      try {
      texture = GraphicUtil.createTexture(gl2, GraphicUtil.convertToImage(swingTexture), false);
      } catch (Exception e) {}
   }

   
   public SwingTexture swingTexture;
   public JFrame debug;
   public Texture texture;
}
