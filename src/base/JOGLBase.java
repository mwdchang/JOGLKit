package base;


import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

/////////////////////////////////////////////////////////////////////////////////
// Base class for JOGL based demos, provides windows/platform agnostic API for
// graphics rendering and event management.
//
// This is meant for demos and proofs of concept, a full fledged JOGL project
// should likely use a framework that provides more features and is more robust.
/////////////////////////////////////////////////////////////////////////////////
public abstract class JOGLBase implements GLEventListener {

   public void display(GLAutoDrawable a)  {
      GL2 gl2 = a.getGL().getGL2();   
      basicClear(gl2);
      
      // Delegate
      render(a);
   }
   
   
   public abstract void render(GLAutoDrawable a);
   
   
   @Override
   public void dispose(GLAutoDrawable arg0) {
      System.out.println("All Done...........");
   }

   @Override
   public void init(GLAutoDrawable glDrawable) {
       GL2 gl2 = glDrawable.getGL().getGL2();      
       winHeight = glDrawable.getHeight();
       winWidth  = glDrawable.getWidth();
       
       // Default the viewport if it is unset
       if (viewX == -1 || viewY == -1 || viewWidth == -1 || viewHeight == -1) {
          viewX = viewY = 0;
          viewWidth  = winWidth;
          viewHeight = winHeight;
       }
       
       gl2.glShadeModel(GL2.GL_SMOOTH);
       gl2.glClearColor(0, 0, 0, 0);
       gl2.glEnable(GL2.GL_DEPTH_TEST);
       gl2.glDepthFunc(GL2.GL_LESS);
       gl2.glClearDepth(1.0f);
   }

   
   @Override
   public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) {
      GL2 gl2 = glDrawable.getGL().getGL2();   
      
      winHeight = glDrawable.getHeight();
      winWidth  = glDrawable.getWidth();
      
      //float aspect = (float)width / (float)height;
      float aspect = (float)viewWidth/ (float)viewHeight;
      gl2.glMatrixMode(GL2.GL_PROJECTION); 
      gl2.glLoadIdentity();
      glu.gluPerspective(30.0f, aspect, near, far);
      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      gl2.glLoadIdentity();
   }
   
   
   // Basic clearing wrapper for depth and colour buffer
   public void basicClear(GL2 gl2) {
      gl2.glClear(GL2.GL_COLOR_BUFFER_BIT);
      gl2.glClear(GL2.GL_DEPTH_BUFFER_BIT);   
      gl2.glLoadIdentity();
   }
   
   
   public void handleKeyPress(int key, char c) {
      if ( key == ESCAPE) {
         System.out.println("Shutting down");
         System.exit(0);
      }
   }
   
   public void handleMouseMove(int mouseX, int mouseY) {
      oldPosX = posX;
      oldPosY = posY;
      posX = mouseX;
      posY = winHeight - mouseY;
      //log("posX:"+ posX + " posY:"+posY);
   }
   
   public void handleMouseClick() {
   }
   
   public void handleMouseUp() {
   }
   
   public void handleMouseDrag(int mouseX, int mouseY) {
      oldPosX = posX;
      oldPosY = posY;
      posX = mouseX;
      posY = winHeight - mouseY;
      log("posX:"+ posX + " posY:"+posY + " oldX:"+oldPosX + " oldY:"+ oldPosY);
   }
   
   public void log(String s) {
      System.err.println(s);
   }
   
   public void setView(int x, int y, int w, int h) {
      viewX = x;
      viewY = y;
      viewWidth = w;
      viewHeight = h;
   }

   
   // Control
   public static GLU glu = new GLU();
   public int winWidth, winHeight;   // The size of the container window
   
   public int viewWidth=-1, viewHeight=-1; // The size of the view port
   public int viewX=-1, viewY=-1;          // The anchor of the view port
   public int posX, posY;
   public int oldPosX, oldPosY;
   
   // Scene control
   public double near = 1.0f;
   public double far = 300.0f;
   
   
   
   // Constants 
   public static final int ESCAPE = 27;
}
