package graphics;

////////////////////////////////////////////////////////////////////////////////
// Quaternion based camera
////////////////////////////////////////////////////////////////////////////////
public class QCamera {
   
   public QCamera() {
      // Default
      eye = new DCTriple(0, 0, 60);   
      q  = new Quaternion(new DCTriple(0, 0, -1), 90*D2R);  // Q is in radians eh?  
   }
   
   
   public void fromRadian(double roll, double pitch, double yaw) {
      double cr, cp, cy, sr, sp, sy, cpcy, spsy;
      // calculate trig identities
      cr = Math.cos(roll/2);
      cp = Math.cos(pitch/2);
      cy = Math.cos(yaw/2);
      sr = Math.sin(roll/2);
      sp = Math.sin(pitch/2);
      sy = Math.sin(yaw/2);
      cpcy = cp * cy;
      spsy = sp * sy;
      q.w = cr * cpcy + sr * spsy;
      q.x = sr * cpcy - cr * spsy;
      q.y = cr * sp * cy + sr * cp * sy;
      q.z = cr * cp * sy - sr * sp * cy;      
   }
   
   public void pitch(double angle) {
      /*
      pitch += angle;
      fromRadian(roll*D2R, pitch*D2R, yaw*D2R);
      */
      
      Quaternion rot = new Quaternion( new DCTriple(1, 0, 0), angle*D2R); 
      q = rot.mult(q);
   }
   
   public void yaw(double angle) {
      /*
      yaw += angle;
      fromRadian(roll*D2R, pitch*D2R, yaw*D2R);
      */
      Quaternion rot = new Quaternion( new DCTriple(0, 1, 0), angle*D2R); 
      q = rot.mult(q);
   }
   
   public void roll(double angle) {
      /*
      roll += angle;
      fromRadian(roll*D2R, pitch*D2R, yaw*D2R);
      */
      Quaternion rot = new Quaternion( new DCTriple(0, 0, 1), angle*D2R); 
      q = rot.mult(q);
   }
   
   
   public void move(double amt) {
      eye = eye.add( q.mult(new DCTriple(0, 0, -1)).mult((float)amt));
   }
   
   
   
   public String toString() {
      return eye.toString();
   }
   
   
   
   public double roll = 0;
   public double pitch = 0;
   public double yaw = 0;
   public DCTriple eye;
   public Quaternion q;
   public static final double D2R = Math.PI/180.0;
   public DCTriple look  = new DCTriple(0, 0, -1);
   public DCTriple up    = new DCTriple(0, 1, 0);
   public DCTriple right = new DCTriple(1, 0, 0);
}
