package leapmotion;


import graphics.DrawUtil;
import graphics.GraphicUtil;
import graphics.DCTriple;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;


/////////////////////////////////////////////////////////////////////////////////
// Rotate a polygon in 2D space
/////////////////////////////////////////////////////////////////////////////////
public class LeapTest2 extends base.JOGLBase {
   
   public SampleListener listener = new SampleListener();
   public Controller controller = new Controller();
   
   public volatile ConcurrentHashMap<String, Vector<DCTriple>> motionList = new ConcurrentHashMap<String, Vector<DCTriple>>();
   
   public long count = 0;

   public double D2R( double degree ) {
      return degree * Math.PI / 180.0;   
   }
   
   @Override
   public void dispose(GLAutoDrawable a) {
      super.dispose(a);
      controller.removeListener(listener);
   }
   
   
   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      GraphicUtil.setPerspectiveView(gl2, viewWidth/viewHeight, 30.0f, 1.0f, 2200.0f, 
            new float[]{600, 400, 600},
            new float[]{0, 0, 0},
            new float[]{0, 1, 0}); 
      
      
      
      gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);   
      gl2.glRotated(yRot, 0, 1, 0);
      
      // Draw a grid on XZ
      gl2.glLineWidth(1.0f);
      gl2.glBegin(GL2.GL_LINES);
      gl2.glColor4d(1, 0.5, 0, 0.5);
      for (int i=-400; i < 400; i+=10) {
         gl2.glVertex3d(-400, 0, i);
         gl2.glVertex3d( 400,  0, i);
      }
      for (int i=-400; i < 400; i+=10) {
         gl2.glVertex3d(i, 0, -400);
         gl2.glVertex3d(i,  0, 400);
      }
 
      gl2.glEnd();
      
      gl2.glLineWidth(2.0f);
      GraphicUtil.drawAxis(gl2, 0.0f, 0.0f, 0.0f);
      
      
      Vector<DCTriple> v  = motionList.get("Hand");
      count++;
      if (v != null) {
         for (DCTriple d : v) {
            gl2.glPushMatrix();
               gl2.glColor4d(1.0, 1.0, 0.0, 1.0);
               gl2.glBegin(GL2.GL_LINES);
                  gl2.glVertex3d( d.x, d.y, d.z);
                  gl2.glVertex3d( d.x, 0, d.z);
               gl2.glEnd();
               
               gl2.glTranslated(d.x, d.y, d.z);
               DrawUtil.drawCube(gl2, 5.0f);
            gl2.glPopMatrix();
            
            float w = 5.0f;
            
            gl2.glPushMatrix(); 
               gl2.glBegin(GL2.GL_QUADS);
                  gl2.glVertex3d(d.x-w, 0, d.z-w);
                  gl2.glVertex3d(d.x-w, 0, d.z+w);
                  gl2.glVertex3d(d.x+w, 0, d.z+w);
                  gl2.glVertex3d(d.x+w, 0, d.z-w);
               gl2.glEnd();
            gl2.glPopMatrix(); 
         }
      }
      count = 0;
      motionList.clear();
      
         
   }
  
   
   @Override
   public void init(GLAutoDrawable a) {
      super.init(a);   
      this.resetStage();
      
      GL2 gl2 = a.getGL().getGL2();
      
      gl2.glEnable(GL2.GL_LINE_SMOOTH);
      gl2.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);
      
      gl2.glEnable(GL2.GL_BLEND);
      //gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
      gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);
      
      gl2.glDisable(GL2.GL_DEPTH_TEST);
      gl2.glDisable(GL2.GL_TEXTURE_2D);
      
      
      gl2.glLineWidth(2.0f);
      
      
 

      // Have the sample listener receive events from the controller
      controller.addListener(listener);      
   }
   
   
   @Override
   public void handleKeyPress(int key, char c) {
      super.handleKeyPress(key, c);
      
      if (key == KeyEvent.VK_SPACE) {
         this.resetStage();
      }
           
      //wasd
      if (c == 'a') yRot += 5;
      if (c == 'd') yRot -= 5;
      if (c == 'w') xRot += 5; 
      if (c == 's') xRot -= 5;
      
   }
   
   public float yRot = 0;
   public float xRot = 0;

   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Reset the vertices and rotation parameters
   ////////////////////////////////////////////////////////////////////////////////
   public void resetStage() {
   }
   
   
   
   
   public int blendMode = 0;
   public double zpos = 0;
   
   public double xvec, yvec, zvec; // random rotation vectors, let the api normalize them, we just generate random numbers

   
   
   class SampleListener extends Listener {
      public void onInit(Controller controller) {
          System.out.println("Initialized");
      }

      public void onConnect(Controller controller) {
          System.out.println("Connected");
      }

      public void onDisconnect(Controller controller) {
          System.out.println("Disconnected");
      }

      public void onExit(Controller controller) {
          System.out.println("Exited");
      }

      public void onFrame(Controller controller) {
          // Get the most recent frame and report some basic information
          Frame frame = controller.frame();
//          System.out.println("Frame id: " + frame.id()
//                           + ", timestamp: " + frame.timestamp()
//                           + ", hands: " + frame.hands().count()
//                           + ", fingers: " + frame.fingers().count()
//                           + ", tools: " + frame.tools().count());

          if (!frame.hands().empty()) {
              // Get the first hand
              Hand hand = frame.hands().get(0);

              // Check if the hand has any fingers
              FingerList fingers = hand.fingers();
              if (!fingers.empty() ) {
                  // Calculate the hand's average finger tip position
                  Vector<DCTriple> newVec = new Vector<DCTriple>();
                  com.leapmotion.leap.Vector avgPos = com.leapmotion.leap.Vector.zero();
                  for (Finger finger : fingers) {
                      newVec.add(new DCTriple( finger.tipPosition().getX(),
                            finger.tipPosition().getY(),
                            finger.tipPosition().getZ())); 
                      
                      avgPos = avgPos.plus(finger.tipPosition());
                  }
                  motionList.put("Hand", newVec);
                  
                  
                  avgPos = avgPos.divide(fingers.count());
//                  System.out.println("Hand has " + fingers.count()
//                                   + " fingers, average finger tip position: " + avgPos);
              }

              // Get the hand's sphere radius and palm position
//              System.out.println("Hand sphere radius: " + hand.sphereRadius()
//                               + " mm, palm position: " + hand.palmPosition());

              // Get the hand's normal vector and direction
              com.leapmotion.leap.Vector normal = hand.palmNormal();
              com.leapmotion.leap.Vector direction = hand.direction();

              // Calculate the hand's pitch, roll, and yaw angles
//              System.out.println("Hand pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
//                               + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
//                               + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees\n");
          }
      }
  }   
   
   
}
