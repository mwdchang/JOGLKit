package base;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

////////////////////////////////////////////////////////////////////////////////
// AWT based window system and event handlers
////////////////////////////////////////////////////////////////////////////////
public class AWTWindow implements KeyListener, MouseListener, MouseMotionListener {
   
   public JFrame frame;
   public GLCanvas canvas;
   public JOGLBase prog;
   
   public static void main(String[] args) {
      AWTWindow base = new AWTWindow();
      base.setProgram(null);
      base.run("Test", 500, 500);
   }
   
   public void run(String title, int width, int height) {
      GLProfile profile = GLProfile.getDefault();
      GLCapabilities capabilities = new GLCapabilities(profile);
      
      canvas = new GLCanvas(capabilities);     
      canvas.setSize( width, height );
      canvas.addGLEventListener( prog );      
      canvas.addKeyListener(this);
      canvas.addMouseListener(this);
      canvas.addMouseMotionListener(this);
      
      frame = new JFrame(title);
      frame.getContentPane().add( canvas );
      frame.setSize( frame.getContentPane().getPreferredSize());
      
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            System.out.println("Shutting down");   
            System.exit(0);
         }
      });      
      
      frame.setVisible(true);
      canvas.requestFocusInWindow();
      canvas.requestFocus();
      
      
      FPSAnimator animator = new FPSAnimator( canvas, 30 );
      animator.start();
   }
   
   
   
   public void setProgram(JOGLBase g) {
      prog = g;    
   }

   
   @Override
   public void keyPressed(KeyEvent e) {
      prog.handleKeyPress(e.getKeyCode(), e.getKeyChar());   
   }

   @Override
   public void keyReleased(KeyEvent arg0) {
   }

   @Override
   public void keyTyped(KeyEvent arg0) {
   }

   
   @Override
   public void mouseDragged(MouseEvent e) {
      prog.handleMouseDrag(e.getX(), e.getY());
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      prog.handleMouseMove( e.getX(), e.getY());
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      prog.handleMouseClick();
   }

   
   @Override
   public void mouseEntered(MouseEvent arg0) {
   }

   @Override
   public void mouseExited(MouseEvent arg0) {
   }

   @Override
   public void mousePressed(MouseEvent arg0) {
      prog.handleMouseDown();
   }

   @Override
   public void mouseReleased(MouseEvent arg0) {
      prog.handleMouseUp();
   }
}
