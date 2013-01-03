package graphics;

import javax.media.opengl.GL2;


////////////////////////////////////////////////////////////////////////////////
// Provide some basic drawing functions that I use
// over and over
////////////////////////////////////////////////////////////////////////////////
public class DrawUtil {

   ////////////////////////////////////////////////////////////////////////////////
   // Draw a pie on the XY plane
   //    x, y, z    - center of the circle
   //    r          - radius  
   //    start      - starting angle in degrees
   //    end        - ending angle in degrees
   //    seg        - number of triangles used for approximation
   ////////////////////////////////////////////////////////////////////////////////
   public static void drawPie(GL2 gl2, double x, double y, double z, double r, double start, double end, int seg) {
      double ang = (end - start)/(double)seg;
      gl2.glBegin(GL2.GL_TRIANGLE_FAN);
         gl2.glVertex3d(x, y, z);
         
         for (int i=0; i <= seg; i++) {
            double rx = r*Math.cos( (start+ang*i)*3.1416/180.0 );
            double ry = r*Math.sin( (start+ang*i)*3.1416/180.0 );
            gl2.glVertex3d(x + rx, y + ry, z);         
         }
      gl2.glEnd();
   }
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Draw an arc on the XY plane
   //    x, y, z     - centre of the circle
   //    r1, r2      - inner and outer radii
   //    start       - starting angle in degrees
   //    end         - ending angle in degrees
   //    seg         - number of segments to approximate the arc
   //    step        - ??? Don't remember
   ////////////////////////////////////////////////////////////////////////////////
   public static void drawArc(GL2 gl2, double x, double y, double z, double r1, double r2, double start, double end, int seg, int step) {
      double ang; 
      if (start <= end) 
         ang = (end - start)/(double)seg;
      else
         ang = (360 - (start-end))/(double)seg;
      
      gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
         for (int i=0; i <= seg; i+=step ) {
            double rx = r1*Math.cos( (start+ang*i)*Math.PI/180.0 );
            double ry = r1*Math.sin( (start+ang*i)*Math.PI/180.0 );
            gl2.glVertex3d(x + rx, y + ry, z);         
            
            double rx2 = r2*Math.cos( (start+ang*i)*Math.PI/180.0 );
            double ry2 = r2*Math.sin( (start+ang*i)*Math.PI/180.0 );
            gl2.glVertex3d(x + rx2, y + ry2, z);         
         }
     gl2.glEnd();
   }      
   public static void drawArc(GL2 gl2, double x, double y, double z, double r1, double r2, double start, double end, int seg) {
      drawArc(gl2, x, y, z, r1, r2, start, end, seg, 1);    
   }
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Draws a quad as a composition of triangle fans
   //    x, y, z - center
   //    w       - width/2
   //    h       - height/2
   ////////////////////////////////////////////////////////////////////////////////
   public static void drawQuadFan(GL2 gl2, double x, double y, double z, double w, double h, float c1[], float c2[]) {
      gl2.glBegin(GL2.GL_TRIANGLE_FAN);
         if (c1 != null) gl2.glColor4fv(c1, 0);
         gl2.glVertex3d(x, y, z); // center
         
         if (c2 != null) gl2.glColor4fv(c2, 0);
         gl2.glVertex3d(x-w, y-h, z); // lower left
         gl2.glVertex3d(x+w, y-h, z); // lower right
         gl2.glVertex3d(x+w, y+h, z); // upper right
         gl2.glVertex3d(x-w, y+h, z); // upper left
         
         gl2.glVertex3d(x-w, y-h, z); // bring it back
      gl2.glEnd();
   }
   public static void drawQuadFan(GL2 gl2, double x, double y, double z, double w, double h) {
       drawQuadFan(gl2, x, y, z, w, h, null, null);
   }
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Cube with quads
   //   Draw a cube with 2*d length
   ////////////////////////////////////////////////////////////////////////////////
   public static void drawCubeQuad(GL2 gl2, float d) {
      gl2.glBegin(GL2.GL_QUADS);
          // Front Face
         gl2.glNormal3d(0, 0, 1);
         gl2.glColor4d(1.0, 0.0, 0.0, 1.0);
         gl2.glVertex3d(-d, -d, d);
         gl2.glVertex3d( d, -d, d);
         gl2.glVertex3d( d,  d, d);
         gl2.glVertex3d(-d,  d, d);
         
         // Back Face
         gl2.glNormal3d(0, 0, -1);
         gl2.glColor4d(1.0, 1.0, 0.0, 1.0);
         gl2.glVertex3d(-d, -d, -d);
         gl2.glVertex3d( d, -d, -d);
         gl2.glVertex3d( d,  d, -d);
         gl2.glVertex3d(-d,  d, -d);
         
         // Left Face
         gl2.glNormal3d(-1, 0, 0);
         gl2.glColor4d(0.0, 1.0, 0.0, 1.0);
         gl2.glVertex3d(-d, -d, -d);
         gl2.glVertex3d(-d, -d,  d);
         gl2.glVertex3d(-d,  d,  d);
         gl2.glVertex3d(-d,  d, -d);
         
         // Right Face
         gl2.glNormal3d(1, 0, 0);
         gl2.glColor4d(0.0, 1.0, 1.0, 1.0);
         gl2.glVertex3d(d, -d, -d);
         gl2.glVertex3d(d, -d,  d);
         gl2.glVertex3d(d,  d,  d);
         gl2.glVertex3d(d,  d, -d);
         
         // Top Face
         gl2.glNormal3d(0, 1, 0);
         gl2.glColor4d(1.0, 0.0, 1.0, 1.0);
         gl2.glVertex3d(-d, d, -d);
         gl2.glVertex3d(-d, d,  d);
         gl2.glVertex3d( d, d,  d);
         gl2.glVertex3d( d, d, -d);
         
         // Bottom Face
         gl2.glNormal3d(0, -1, 0);
         gl2.glColor4d(0.0, 0.0, 1.0, 1.0);
         gl2.glVertex3d(-d,  -d, -d);
         gl2.glVertex3d(-d,  -d,  d);
         gl2.glVertex3d( d,  -d,  d);
         gl2.glVertex3d( d,  -d, -d);
      gl2.glEnd();
   }
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Cube drawn with triangle fans
   //   Draw a cube with 2*d length
   ////////////////////////////////////////////////////////////////////////////////
   public static void drawCube(GL2 gl2, float d) {
      gl2.glBegin(GL2.GL_QUADS);
      gl2.glEnd();
      
      
      // Front Face
      gl2.glBegin(GL2.GL_TRIANGLE_FAN);
         gl2.glNormal3d(0, 0, 1);
         gl2.glColor4d(0.5, 0.5, 0.5, 0.5);
         gl2.glVertex3f( 0,  0, d);
         gl2.glColor4d(0.0, 0.0, 1.0, 1.0);
         gl2.glVertex3d(-d, -d, d);
         gl2.glVertex3d( d, -d, d);
         gl2.glVertex3d( d,  d, d);
         gl2.glVertex3d(-d,  d, d);
         gl2.glVertex3d(-d, -d, d);
      gl2.glEnd();
      
      // Back Face
       gl2.glBegin(GL2.GL_TRIANGLE_FAN);
         gl2.glNormal3d(0, 0, -1);
         gl2.glColor4d(0.5, 0.5, 0.5, 0.5);
         gl2.glVertex3f( 0,  0, -d);
         gl2.glColor4d(0.0, 0.0, 1.0, 1.0);
         gl2.glVertex3d(-d, -d, -d);
         gl2.glVertex3d( d, -d, -d);
         gl2.glVertex3d( d,  d, -d);
         gl2.glVertex3d(-d,  d, -d);
         gl2.glVertex3d(-d, -d, -d);
      gl2.glEnd();
     
      // Left face
      gl2.glBegin(GL2.GL_TRIANGLE_FAN); 
         gl2.glNormal3d(-1, 0, 0);
         gl2.glColor4d(0.5, 0.5, 0.5, 0.5);
         gl2.glVertex3d(-d,  0,  0);
         gl2.glColor4d(0.0, 0.0, 1.0, 1.0);
         gl2.glVertex3d(-d, -d, -d);
         gl2.glVertex3d(-d, -d,  d);
         gl2.glVertex3d(-d,  d,  d);
         gl2.glVertex3d(-d,  d, -d);
         gl2.glVertex3d(-d, -d, -d);
      gl2.glEnd();    
      
      // Right face
      gl2.glBegin(GL2.GL_TRIANGLE_FAN); 
         gl2.glNormal3d(1, 0, 0);
         gl2.glColor4d(0.5, 0.5, 0.5, 0.5);
         gl2.glVertex3d(d,  0,  0);
         gl2.glColor4d(0.0, 0.0, 1.0, 1.0);
         gl2.glVertex3d(d, -d, -d);
         gl2.glVertex3d(d, -d,  d);
         gl2.glVertex3d(d,  d,  d);
         gl2.glVertex3d(d,  d, -d);
         gl2.glVertex3d(d, -d, -d);
      gl2.glEnd();    
     
      // Top Face
      gl2.glBegin(GL2.GL_TRIANGLE_FAN);
         gl2.glNormal3d(0, 1, 0);
         gl2.glColor4d(0.5, 0.5, 0.5, 0.5);
         gl2.glVertex3d( 0, d,  0);
         gl2.glColor4d(0.0, 0.0, 1.0, 1.0);
         gl2.glVertex3d(-d, d, -d);
         gl2.glVertex3d(-d, d,  d);
         gl2.glVertex3d( d, d,  d);
         gl2.glVertex3d( d, d, -d);      
         gl2.glVertex3d(-d, d, -d);
      gl2.glEnd();
      
      // Bottom Face
      gl2.glBegin(GL2.GL_TRIANGLE_FAN);
         gl2.glNormal3d(0, -1, 0);
         gl2.glColor4d(0.5, 0.5, 0.5, 0.5);
         gl2.glVertex3d( 0, -d,  0);
         gl2.glColor4d(0.0, 0.0, 1.0, 1.0);
         gl2.glVertex3d(-d, -d, -d);
         gl2.glVertex3d(-d, -d,  d);
         gl2.glVertex3d( d, -d,  d);
         gl2.glVertex3d( d, -d, -d);      
         gl2.glVertex3d(-d, -d, -d);
      gl2.glEnd();
   }   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Draw a rounded rectangle
   //    x, y, z       - center point
   //    width, height - rectangle width and height (div 2)
   //    offset        - the amount reserved for the arc
   //    argSeg        - number of triangles for approximate an arc
   ////////////////////////////////////////////////////////////////////////////////
   
   public static void drawRoundedRect(GL2 gl2, double x, double y, double z, double width, double height, double offset, int arcSeg, float c1[], float c2[]) {
      double wsize = width;
      double hsize = height;
      
      // draw center
      drawQuadFan(gl2, x, y, z, wsize-offset, hsize-offset, c1, c2);
      /*
      gl2.glBegin(GL2.GL_QUADS);
         gl2.glVertex3d(x-wsize+offset, y-hsize+offset, z);
         gl2.glVertex3d(x+wsize-offset, y-hsize+offset, z);
         gl2.glVertex3d(x+wsize-offset, y+hsize-offset, z);
         gl2.glVertex3d(x-wsize+offset, y+hsize-offset, z);
      gl2.glEnd();
      */
      
      // draw upper
      if (c2 != null) gl2.glColor4fv(c2, 0);
      gl2.glBegin(GL2.GL_QUADS);
         gl2.glVertex3d(x-wsize+offset, y+hsize-offset, z);
         gl2.glVertex3d(x+wsize-offset, y+hsize-offset, z);
         gl2.glVertex3d(x+wsize-offset, y+hsize, z);
         gl2.glVertex3d(x-wsize+offset, y+hsize, z);
      gl2.glEnd();
      
      // draw lower
      gl2.glBegin(GL2.GL_QUADS);
         gl2.glVertex3d(x-wsize+offset, y-hsize+offset, z);
         gl2.glVertex3d(x+wsize-offset, y-hsize+offset, z);
         gl2.glVertex3d(x+wsize-offset, y-hsize, z);
         gl2.glVertex3d(x-wsize+offset, y-hsize, z);
      gl2.glEnd();
      
      // draw west
      gl2.glBegin(GL2.GL_QUADS);
         gl2.glVertex3d( x-wsize, y-hsize+offset, z);
         gl2.glVertex3d( x-wsize+offset, y-hsize+offset, z);
         gl2.glVertex3d( x-wsize+offset, y+hsize-offset, z);
         gl2.glVertex3d( x-wsize,        y+hsize-offset, z);
      gl2.glEnd();
      
      // draw east
      gl2.glBegin(GL2.GL_QUADS);
         gl2.glVertex3d( x+wsize-offset, y-hsize+offset, z);
         gl2.glVertex3d( x+wsize, y-hsize+offset, z);
         gl2.glVertex3d( x+wsize, y+hsize-offset, z);
         gl2.glVertex3d( x+wsize-offset,   y+hsize-offset, z);
      gl2.glEnd();
      
      drawPie(gl2, x+wsize-offset, y+hsize-offset, z, offset,  0.0,  90.0, arcSeg);    // upper-right
      drawPie(gl2, x-wsize+offset, y+hsize-offset, z, offset, 90.0, 180.0, arcSeg);    // upper-left
      drawPie(gl2, x-wsize+offset, y-hsize+offset, z, offset, 180.0, 270.0, arcSeg);   // lower-right
      drawPie(gl2, x+wsize-offset, y-hsize+offset, z, offset, 270, 360.0, arcSeg);     // lower-left
   }   
   
}
