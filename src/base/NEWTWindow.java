package base;

import javax.media.opengl.*;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

////////////////////////////////////////////////////////////////////////////////
// JOGL native window container
////////////////////////////////////////////////////////////////////////////////
public class NEWTWindow implements KeyListener, MouseListener {
   
   public JOGLBase prog;
   public GLWindow window;
   
   public void run(String title, int width, int height) {
      GLProfile profile = GLProfile.getDefault();
      GLCapabilities capabilities = new GLCapabilities(profile);
      
      window = GLWindow.create(capabilities);
      window.setTitle( title );
      window.setSize( width, height );
      window.setVisible(true);
      
      window.addWindowListener(new WindowAdapter() {
          public void windowDestroyNotify(WindowEvent arg0) {
              System.exit(0);
          };
      });
     
      window.addGLEventListener(prog);
      window.addKeyListener(this);
      window.addMouseListener(this);
      window.requestFocus();
      
      
      
      FPSAnimator animator = new FPSAnimator( window, 500 );
      animator.start();
   }
   
   public void setProgram(JOGLBase g) {
      prog = g;    
   }
   
   @Override
   public void keyPressed(KeyEvent e) {
      prog.handleKeyPress( e.getKeyCode(), e.getKeyChar() );
   }

   @Override
   public void keyReleased(KeyEvent arg0) {
   }

   @Override
   public void keyTyped(KeyEvent arg0) {
   }

   @Override
   public void mouseClicked(MouseEvent arg0) {
      prog.handleMouseClick();
   }

   @Override
   public void mouseDragged(MouseEvent e) {
      prog.handleMouseDrag( e.getX(), e.getY());
   }

   @Override
   public void mouseEntered(MouseEvent arg0) {
   }

   @Override
   public void mouseExited(MouseEvent arg0) {
   }

   @Override
   public void mouseMoved(MouseEvent e) {
      prog.handleMouseMove(e.getX(), e.getY());   
   }

   @Override
   public void mousePressed(MouseEvent arg0) {
   }

   @Override
   public void mouseReleased(MouseEvent arg0) {
      prog.handleMouseUp();
   }

   @Override
   public void mouseWheelMoved(MouseEvent arg0) {
   }
   
}
