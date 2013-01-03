package graphics;

import Jama.Matrix;

// Camera that supports pitch/yaw/roll
// Note: roll is not implemented
//
// Idea taken from : http://www.opengl.org/sdk/docs/tutorials/CodeColony/camera.php
public class DCCamera {
   
   
   public static int CAMERA_DEBUG = 0;
   
   protected DCCamera() {
      reset();
      System.out.println("Camera init");
   }
   
   
   public static void main(String[] args) {
      DCCamera.instance();
      System.out.println(DCCamera.instance().look + " " + DCCamera.instance().right);
      for (int i=0; i < 10; i++) {
         DCCamera.instance().yaw(9);
         System.out.println(DCCamera.instance().look + " " + DCCamera.instance().right);
      }
      System.out.println("");
      
      for (int i=0; i < 10; i++) {
         DCCamera.instance().pitch(9);
         System.out.println(DCCamera.instance().look + " " + DCCamera.instance().right);
      }
   }
   
   public void reset() {
      up = new DCTriple(0, 1, 0);
      eye = new DCTriple(0, 0, 40);
      //look = new DCTriple(0, 0, -1);
      look = new DCTriple(0,0,0).sub(eye);
      look.normalize();
      up.normalize();
      
      right = new DCTriple(1, 0, 0);
      pitchAngle = 0.0f;
      yawAngle = 0.0f;     
      System.err.println("Reset called");
   }
   
   
   public void move(float unit) {
      eye = eye.add(look.mult(unit));     
   }
   
   
   
   public void strafe(float unit) {
      eye = eye.add(right.mult(unit)); 
   }
   
   
   
   public void roll(float angle) {
      // Not supported   
   }
   
   // up remains constant
   public void yaw(float angle) {
      yawAngle = angle; 
      Matrix rot = MatrixUtil.rotationMatrix(yawAngle, "Y");
      double r[] = MatrixUtil.multVector(rot, right.toArrayd()); 
      right = new DCTriple(r);
      right.normalize();
      
      // redo look
      look = right.cross(up).mult(-1.0f);
      
      
      if (CAMERA_DEBUG == 1) {
         System.out.println("yaw: " + angle);
         System.out.println("Look: " + DCCamera.instance().look.toString());
         System.out.println("Up: " + DCCamera.instance().up.toString());
         System.out.println("Right: " + DCCamera.instance().right.toString());
      }
   }
   
   // right remains constant
   public void pitch(float angle) {
      pitchAngle = angle;
      
      Matrix rot = MatrixUtil.rotationMatrix(pitchAngle, "X");
      double r[] = MatrixUtil.multVector(rot, look.toArrayd()); 
      look = new DCTriple(r);
      look.normalize();
      
      // redo up
      up = look.cross(right).mult(-1.0f);
      
      if (CAMERA_DEBUG == 1) {
         System.out.println("Pitch : " + angle);
         System.out.println("Look: " + DCCamera.instance().look.toString());
         System.out.println("Up: " + DCCamera.instance().up.toString());
         System.out.println("Right: " + DCCamera.instance().right.toString());
      }
   }
   
   

   private static DCCamera inst = null;
   public static DCCamera instance() {
      if (inst == null) inst = new DCCamera();
      return inst;
   }
   
   public float pitchAngle;
   public float yawAngle;
   public DCTriple up;
   public DCTriple look;
   public DCTriple right;
   public DCTriple eye;
}
