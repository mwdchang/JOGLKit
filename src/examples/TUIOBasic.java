package examples;

import java.util.Vector;

import graphics.GraphicUtil;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import base.AWTWindow;
import base.JOGLBase;
import base.TPoint;
import base.TUIOBase;
import TUIO.TuioClient;

public class TUIOBasic extends JOGLBase {
   
   public static void main(String args[]) {
      AWTWindow base = new AWTWindow();
      base.setProgram(new TUIOBasic());
      base.run("Demo Runner", 800, 800);      
   }

   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();   
      
      GraphicUtil.setOrthonormalView(gl2, 0, viewWidth, 0, viewHeight, -10, 10);
      
      for (Vector<TPoint> v : base.table.values()) {
         gl2.glPointSize(5.0f);
         gl2.glBegin(GL2.GL_POINTS);
         for (int i= ( v.size()-1); i >=0; i--) {
            if ( i == v.size()-1 ) {
               gl2.glColor4d(1, 0, 1, 1);               
            } else {
               gl2.glColor4d(1, 0.5, 0, 1);               
            }
            gl2.glVertex3d( v.elementAt(i).pos.x, v.elementAt(i).pos.y, 0);      
         }
         gl2.glEnd();
      }
      
   }
   
   public void init(GLAutoDrawable a) {
      super.init(a);
      base = new TUIOBase(viewWidth, viewHeight);
      client.addTuioListener( base );
      client.connect();      
   }
   
   public TuioClient client = new TuioClient();
   public TUIOBase base;
   

}
