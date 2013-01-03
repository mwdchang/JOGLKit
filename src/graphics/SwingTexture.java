package graphics;

import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.jogamp.opengl.util.awt.TextureRenderer;
import com.jogamp.opengl.util.texture.Texture;

import Jama.Matrix;

////////////////////////////////////////////////////////////////////////////////
// Encapsulates a java 2D gui
////////////////////////////////////////////////////////////////////////////////
public class SwingTexture extends JPanel {
   
   // Just a test now, extremely inefficient
   public void update2DTexture() {
      texture = renderer.getTexture();
   }
   
   public void initRenderer() {
      renderer = new TextureRenderer(getWidth(), getHeight(), false, true);   
      g = renderer.createGraphics();
      paint(g);
      System.err.println( getWidth() + " " + getHeight());
   }   
   
   private static final long serialVersionUID = 1L;

   // Matrix operations
   public void addTransform(Matrix t) { transform = MatrixUtil.mult(transform, t); }
   public void setTransform(Matrix t) { transform = t.copy(); }
   public Matrix getTransform() { return transform; }   
   
   public Matrix transform;
   public Texture texture;
   public TextureRenderer renderer;
   public Graphics2D g;   
}
