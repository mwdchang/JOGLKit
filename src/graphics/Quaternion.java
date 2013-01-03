package graphics;

////////////////////////////////////////////////////////////////////////////////
// Quaternion for 3D rotation
//
// Shamelessly ripped off from gpwiki 
//    http://content.gpwiki.org/index.php/OpenGL:Tutorials:Using_Quaternions_to_represent_rotation
////////////////////////////////////////////////////////////////////////////////
public class Quaternion {
   public static final double QTOLERANCE = 0.0000f;
   public double w, x, y, z;
   
   // Default
   public Quaternion(double x, double y, double z, double w) {
      this.w = w;
      this.x = x;
      this.y = y;
      this.z = z;
   }
   
   
   // From axis-angle
   public Quaternion(DCTriple axis, double radian) {
      axis.normalize();
      radian /= 2.0;
      
      x = axis.x * Math.sin(radian);
      y = axis.y * Math.sin(radian);
      z = axis.z * Math.sin(radian);
      w = Math.cos(radian);
   }
   
   
   public Quaternion getConjugate() {
      return new Quaternion( -x, -y, -z, w);   
   }
   
   public void normalize() {
      double mag = Math.sqrt(w*w + x*x + y*y + z*z);
      w /= mag;
      x /= mag;
      y /= mag;
      z /= mag;
   }
   
   
   public Quaternion mult(Quaternion q) {
      return new Quaternion(w * q.x + x * q.w + y * q.z - z * q.y,
                            w * q.y + y * q.w + z * q.x - x * q.z,
                            w * q.z + z * q.w + x * q.y - y * q.x,
                            w * q.w - x * q.x - y * q.y - z * q.z);      
   }
   
   
   public DCTriple mult(DCTriple vec) {
      vec.normalize();
      Quaternion q = new Quaternion( vec.x, vec.y, vec.z, 0 );
      
      Quaternion result = q.mult( this.getConjugate() );
      result = this.mult( result );
      
      return new DCTriple(result.x, result.y, result.z);
   }
   
   
   
   public double[] getAxisAngle() {
      double mag = Math.sqrt( x*x + y*y + z*z);
      return new double[] {
         this.x / mag,
         this.y / mag,
         this.z / mag,
         Math.acos(this.w) * 2.0
      };
      
   }
   
   
   
}
