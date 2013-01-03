package examples;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import graphics.DCCamera;
import graphics.DCTriple;
import graphics.DrawUtil;
import graphics.GraphicUtil;
import graphics.QCamera;
import graphics.TextureFont;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import base.JOGLBase;

public class CameraExample extends JOGLBase {
   
   public QCamera qcamera = new QCamera();
   public TextureFont tf;
   public DecimalFormat dformat = new DecimalFormat("0.000");
   
   public void init(GLAutoDrawable g) {
      super.init(g);
      tf = new TextureFont();
      tf.width = 200;
      tf.height = 60;
   }
   

   @Override
   public void render(GLAutoDrawable g) {
      GL2 gl2 = g.getGL().getGL2();   
      
      GraphicUtil.setPerspectiveView(gl2, viewWidth/viewHeight, 30.0f, 1.0f, 1000.0f); 
      
      // Inverse transform the camera data
      double aa[] = qcamera.q.getAxisAngle();
      gl2.glRotated(aa[3]*180.0/Math.PI, aa[0], aa[1], aa[2]);
      gl2.glTranslated( -qcamera.eye.x, -qcamera.eye.y, -qcamera.eye.z);
      
      
      // Now back-fill the viewing vector
      double viewM[] = new double[16];
      gl2.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, viewM, 0);
      DCTriple look = new DCTriple( viewM[8], viewM[9], viewM[10]);
      
      String str = "X=" + dformat.format(look.x) + " Y=" + dformat.format(look.y) + " Z=" + dformat.format(look.z);
      String strPos = "X=" + dformat.format(qcamera.eye.x) + " Y=" + dformat.format(qcamera.eye.y) + " Z=" + dformat.format(qcamera.eye.z);
      tf.clearMark();
      tf.addMark(str, Color.WHITE, new Font("Serif", Font.PLAIN, 14), 1, 25);
      tf.addMark(strPos, Color.CYAN, new Font("Serif", Font.PLAIN, 14), 1, 1);
      
      //System.out.println("Look is: " + look);
      
      
      
      
      
      
      //gl2.glEnable(GL2.GL_DEPTH_TEST);
      //gl2.glDepthFunc(GL2.GL_LEQUAL);
      //gl2.glEnable(GL2.GL_CULL_FACE);
      GraphicUtil.drawAxis(gl2, 0, 0, 0);
      
      gl2.glColor4d(0.5, 0.5, 0.5, 1.0);
      DrawUtil.drawCubeQuad(gl2, 2.0f);
      
      GraphicUtil.setOrthonormalView(gl2, 0, viewWidth, 0, viewHeight, -1, 1);
      tf.render(gl2);
   }
   
   
   public void handleMouseDrag(int mouseX, int mouseY) {
      super.handleMouseDrag(mouseX, mouseY);
      
      if (oldPosX > posX) qcamera.yaw(0.5f);
      if (oldPosX < posX) qcamera.yaw(-0.5f);
      if (oldPosY > posY) qcamera.pitch(-0.5f);
      if (oldPosY < posY) qcamera.pitch(0.5f);
   }
   
   public void handleKeyPress(int key, char c) {
      super.handleKeyPress(key, c);
      System.out.println(key);
      if ( c == 'w') {
         qcamera.move(0.5);
      } else if ( c == 's' ) {
         qcamera.move(-0.5);
      }
      /*
      if (key == 87) {
         qcamera.pitch(-0.5);
      } else if (key == 83) {
         qcamera.pitch(0.5);
      } else if (key == 68 ) {
         qcamera.yaw(0.5);
      } else if (key == 65) {
         qcamera.yaw(-0.5);
      } else if (key == 81) {
         qcamera.roll(-0.5);
      } else if (key == 69) {
         qcamera.roll(0.5);
      } else if( key == 38) {
         qcamera.move(0.5); 
      } else if( key == 40) {
         qcamera.move(-0.5); 
      }
      */
      /*
      DCCamera.instance().yaw(9);
      System.out.println(DCCamera.instance().look + " " + DCCamera.instance().right);
      */
   }
   
   

}
