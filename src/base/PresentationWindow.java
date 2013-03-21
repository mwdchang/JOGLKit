package base;

import java.util.Vector;

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

import examples.Basic;
import examples.Clock;
import examples.ImageLoader;
import examples.combine.AnnotateProgram;
import examples.combine.CombineDemo;
import examples.combine.FilterProgram;
import examples.danielstyle.Particle;
import examples.filters.Blur;
import examples.filters.Bright;
import examples.filters.Sobel;

////////////////////////////////////////////////////////////////////////////////
// JOGL native window container
////////////////////////////////////////////////////////////////////////////////
public class PresentationWindow implements KeyListener, MouseListener {
   
   public static void main(String[] args) {
      PresentationWindow p = new PresentationWindow();
      /*
      p.progList.add(new Basic());
      p.progList.add(new Blur());
      p.progList.add(new Bright());
      p.progList.add(new Clock());
      p.progList.add(new Sobel());
      */
      /*
      for (int i=1; i <= 43; i++) {
         p.progList.add(new ImageLoader("C:\\Users\\daniel\\Pictures\\New Folder\\seminar_DNPR\\Slide" + i + ".PNG"));    
      }
      */
      
      p.progList.add( new ImageLoader("C:\\Users\\daniel\\Dropbox\\temp\\Toolkit\\Slide1.PNG", true));
      p.progList.add( new ImageLoader("C:\\Users\\daniel\\Dropbox\\temp\\Toolkit\\Slide2.PNG", true));
      p.progList.add( new ImageLoader("C:\\Users\\daniel\\Dropbox\\temp\\Toolkit\\Slide3.PNG", true));
      p.progList.add( new ImageLoader("C:\\Users\\daniel\\Dropbox\\temp\\Toolkit\\Slide4.PNG", true));
      p.progList.add( new Clock());
      p.progList.add( new Particle());
      p.progList.add( new CombineDemo("C:\\Users\\daniel\\Dropbox\\temp\\Toolkit\\Slide6.PNG"));
      p.progList.add( new FilterProgram("C:\\Users\\daniel\\Dropbox\\temp\\Toolkit\\Slide5.PNG"));
      p.progList.add( new AnnotateProgram());
      p.progList.add( new ImageLoader("C:\\Users\\daniel\\Dropbox\\temp\\Toolkit\\Slide7.PNG", true));
      
      
      
      p.run("test", 800, 800);
      
   }
   
   public JOGLBase prog;
   public GLWindow window;
   public Vector<JOGLBase> progList = new Vector<JOGLBase>();
   public int progIndex = -1;
   
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
      
      nextProg();
     
      window.addGLEventListener(prog);
      window.addKeyListener(this);
      window.addMouseListener(this);
      window.requestFocus();
      
      window.setUndecorated(true);
      window.setFullscreen(true);
     
      
      FPSAnimator animator = new FPSAnimator( window, 500 );
      animator.start();
   }
   
   public void nextProg() {
      progIndex++;   
      if (progIndex > progList.size()-1) progIndex = progList.size()-1;
      
      window.removeGLEventListener(prog);
      prog = progList.elementAt(progIndex);
      prog.exit();
      window.addGLEventListener(prog);
   }
   
   public void prevProg() {
      progIndex--;   
      if (progIndex < 0) progIndex = 0;
      window.removeGLEventListener(prog);
      prog = progList.elementAt(progIndex);
      prog.exit();
      window.addGLEventListener(prog);
   }
   
   
   
   @Override
   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
         prog.exit();
         nextProg(); return;    
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT){
         prog.exit();
         prevProg(); return;    
      }
      

      //prog.handleKeyPress( e.getKeyCode(), e.getKeyChar() );
      prog.handleKeyPress( e.getKeyCode(), (char)e.getKeyCode() );
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
