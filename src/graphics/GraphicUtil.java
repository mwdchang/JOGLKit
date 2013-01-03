package graphics;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.awt.TextureRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;


/////////////////////////////////////////////////////////////////////////////////
// 
// Graphic Utility to handle project independent functionalities
//
/////////////////////////////////////////////////////////////////////////////////
public class GraphicUtil {
   
   public static int createVAO(GL2 gl2, float width, float height) {
      float texcoord[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f
      };   
      return createVAO(gl2, width, height, texcoord);    
   }
   
   public static int createVAO(GL2 gl2, float width, float height, float[] t) {
      int vao[] = new int[1];
      int vbo[] = new int[3];        
      float square[] = {
            0.0f,   0.0f, 0.0f,      
            width,  0.0f, 0.0f,      
            width,  height, 0.0f,      
            0.0f,   height, 0.0f       
      };
        
      float color[] = {
            1.0f, 0.0f, 0.0f, 1.0f, 
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f 
      };
      
      float texcoord[] = t;
        
        
      FloatBuffer quadBuffer = (FloatBuffer) GLBuffers.newDirectGLBuffer(GL2.GL_FLOAT, 4*3);
      FloatBuffer colorBuffer   = (FloatBuffer) GLBuffers.newDirectGLBuffer(GL2.GL_FLOAT, 4*4);
      FloatBuffer texBuffer     = (FloatBuffer) GLBuffers.newDirectGLBuffer(GL2.GL_FLOAT, 4*2);      
      quadBuffer.put(square);
      colorBuffer.put(color);
      texBuffer.put(texcoord);
      
      // Reset ?
      quadBuffer.flip();
      colorBuffer.flip();
      texBuffer.flip();
      
      // Generate vertex array
      gl2.glGenVertexArrays(1, vao, 0);
      gl2.glBindVertexArray(vao[0]);
      
      // Generate vertex buffer
      gl2.glGenBuffers(3, vbo, 0);
      
      //System.out.println("FBT ... " + vbo[0] + " " + vbo[1] + " " + vbo[2]);
      
      gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo[0]);
      gl2.glBufferData(GL2.GL_ARRAY_BUFFER, 12*4, quadBuffer, GL2.GL_STATIC_DRAW);
      gl2.glVertexAttribPointer(0, 3, GL2.GL_FLOAT, false, 0, 0);
      gl2.glEnableVertexAttribArray(0);
      
      gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo[1]);
      gl2.glBufferData(GL2.GL_ARRAY_BUFFER, 16*4, colorBuffer, GL2.GL_STATIC_DRAW);
      gl2.glVertexAttribPointer(1, 4, GL2.GL_FLOAT, false, 0, 0);
      gl2.glEnableVertexAttribArray(1);
      
      gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, vbo[2]);
      gl2.glBufferData(GL2.GL_ARRAY_BUFFER, 8*4, texBuffer, GL2.GL_STATIC_DRAW);
      gl2.glVertexAttribPointer(2, 2, GL2.GL_FLOAT, false, 0, 0);
      gl2.glEnableVertexAttribArray(2);      
      gl2.glBindVertexArray(0);      
      
      return vao[0];
   }
   
   

   
   ////////////////////////////////////////////////////////////////////////////////
   // Draw an arc that is one single pixel wide
   ////////////////////////////////////////////////////////////////////////////////
   public static void drawArcLine(GL2 gl2, double x, double y, double z, double r, double start, double end, int seg, int step) {
      double ang = (end - start)/(double)seg;
      gl2.glBegin(GL2.GL_LINES);
      for (int i=0; i <= seg; i+=step ) {
         double rx = r*Math.cos( (start+ang*i)*Math.PI/180.0 );
         double ry = r*Math.sin( (start+ang*i)*Math.PI/180.0 );
         gl2.glVertex3d(x + rx, y + ry, z);         
      }
      gl2.glEnd();
   }
   
   ////////////////////////////////////////////////////////////////////////////////
   // Draws a grid
   ////////////////////////////////////////////////////////////////////////////////
   public static void drawGrid(GL2 gl2, double x1, double y1, double x2, double y2, double padding) {
      gl2.glColor4d(1, 0, 0, 1.0); 
      for (double idx = x1; idx <= x2; idx += padding) {
      gl2.glBegin(GL2.GL_LINES);
         gl2.glVertex2d(idx, y1);
         gl2.glVertex2d(idx, y2);
      gl2.glEnd();
      }
      for (double idx = y1; idx <= y2; idx += padding) {
      gl2.glBegin(GL2.GL_LINES);
         gl2.glVertex2d(x1, idx);
         gl2.glVertex2d(x2, idx);
      gl2.glEnd();
      }
   }
   
   ////////////////////////////////////////////////////////////////////////////////
   // Mostly taken from http://www.felixgers.de/teaching/jogl/imagingProg.html
   // especially the conversion from byte to argb int
   ////////////////////////////////////////////////////////////////////////////////
   public static void screenCap(GL2 gl2, String filename) {
      int height = gl2.getContext().getGLDrawable().getHeight();
      int width  = gl2.getContext().getGLDrawable().getWidth();
      
      System.out.println("Height is : " +height + "\t Width is " + width + "\t Buffer is " + (width*height*3));
      ByteBuffer buffer = (ByteBuffer)GLBuffers.newDirectGLBuffer(GL2.GL_BYTE, width*height*3);
      
      gl2.glReadBuffer(GL2.GL_BACK);
      gl2.glPixelStorei(GL2.GL_PACK_ALIGNMENT, 1);
      gl2.glReadPixels(0, 0, width, height, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, buffer);
      
      // Format to ints
      int pixels[] = new int[ width*height ];
       
      int p = width * height * 3;
      int q;
      int i = 0;
      int w3 = width * 3;
      for (int row = 0; row < height; row++) {
         p -= w3;   
         q = p;
         for (int col = 0; col < width; col++) {
            int R = buffer.get(q++);   
            int G = buffer.get(q++);   
            int B = buffer.get(q++);   
            pixels[i++] = 0xFF000000 | 
                 ((R & 0x0000FF) << 16) |
                 ((G & 0x0000FF) << 8) |
                 (B & 0x0000FF);
         }
      }
      BufferedImage bImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
      bImage.setRGB(0, 0, width, height, pixels, 0, width);
     
//      String path = "C:\\Users\\daniel\\temporary\\"  + filename;
      File test = new File(filename);
      try {
         ImageIO.write(bImage, "PNG", test);   
      } catch (Exception e) {
         e.printStackTrace();   
      }
   }
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Get the screen resolution (s)
   //
   // Idea/logic taken from stackoverflow: 
   // http://stackoverflow.com/questions/3680221/screen-resolution-java
   ////////////////////////////////////////////////////////////////////////////////
   public static int[][] getScreenResolution() {
   	GraphicsEnvironment ge      = GraphicsEnvironment.getLocalGraphicsEnvironment();
   	GraphicsDevice[]    gs      = ge.getScreenDevices();
   	
   	int hResolution = 0;
   	int vResolution = 0; 
   	
   	int result[][] = new int[gs.length][];
   	
   	for (int i=0; i < gs.length; i++) {
   	   int h = gs[i].getDisplayMode().getWidth();
   	   int v = gs[i].getDisplayMode().getHeight();
   	   
   	   result[i] = new int[2];
   	   result[i][0] = h;
   	   result[i][1] = v;
   	   
   	}
      
   	return result;
   }
   
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Just draws the axis
   ////////////////////////////////////////////////////////////////////////////////
   public static void drawAxis(GL2 gl2, float x, float y, float z) {
      gl2.glDisable(GL2.GL_LIGHTING);
      gl2.glDisable(GL2.GL_TEXTURE_2D);
      gl2.glDisable(GL2.GL_CULL_FACE);
      gl2.glEnable(GL2.GL_BLEND);
      gl2.glDisable(GL2.GL_DEPTH_TEST);
      
      gl2.glBegin(GL2.GL_LINES);
         // x-axis
         gl2.glColor4fv(new float[]{1.0f, 0.0f, 0.0f, 0.5f}, 0);
         gl2.glVertex3f(x-1000.0f, y, z);
         gl2.glVertex3f(x+1000.0f, y, z);
         
         // y-axis
         gl2.glColor4fv(new float[]{0.0f, 1.0f, 0.0f, 0.5f}, 0);
         gl2.glVertex3f(x, y-1000, z);
         gl2.glVertex3f(x, y+1000, z);
         
         // z-axis
         gl2.glColor4fv(new float[]{0.0f, 0.0f, 1.0f, 0.5f}, 0);
         gl2.glVertex3f(x,y,z-1000);
         gl2.glVertex3f(x,y,z+1000);
      gl2.glEnd();
   }
   
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Project a 3D point on to screen coordinate
   ////////////////////////////////////////////////////////////////////////////////
   public static float[] getProject(GL2 gl2, float x, float y, float z) {
      float buffer[];
      float viewBuffer[], projBuffer[];
      
      buffer = new float[16];   
      gl2.glGetFloatv(GL2.GL_PROJECTION_MATRIX, buffer, 0);
      projBuffer = buffer;
      
      buffer = new float[16];
      gl2.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, buffer, 0);
      viewBuffer = buffer;
      
      int viewport[] = new int[4];
      gl2.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);             
      
      float wincoord[] = new float[3];      
      
      glu.gluProject(
            x,
            y,
            z,
            viewBuffer, 0,
            projBuffer, 0,
            viewport, 0,
            wincoord, 0
            );               
      
      return wincoord;
   }
   
   ////////////////////////////////////////////////////////////////////////////////
   // Does the dirty works for unproject operations, 
   // including getting the screen z coordinate
   ////////////////////////////////////////////////////////////////////////////////
   public static double[] getUnProject(GL2 gl2, GLU glu, int winX, int winY) {
   	// Various buffers 
	   int viewport[] = new int[4];
	   double modelview_matrix[] = new double[16];
	   double proj_matrix[] = new double[16];
	   double coord[] = new double[4];
      FloatBuffer fz = (FloatBuffer)GLBuffers.newDirectGLBuffer(GL2.GL_FLOAT, 1);
      
      // Retrieve the viewport, projection and modelview information
      gl2.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
      gl2.glGetDoublev(GL2.GL_PROJECTION_MATRIX, proj_matrix, 0);
      gl2.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelview_matrix, 0);
      
      // Transform window coordinate
      winY = viewport[3] - winY;
      
      // Get the winZ coordinate
      gl2.glReadPixels(winX, winY, 1, 1, GL2.GL_DEPTH_COMPONENT, GL2.GL_FLOAT, fz);
      
      // Get the projected coord
      glu.gluUnProject(
            (double)winX, (double)winY, (double)fz.get(0),
            modelview_matrix, 0,
            proj_matrix, 0,
            viewport, 0,
            coord, 0
      );
      
      return coord;	
   }
   
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Set up the perspective viewing matrix
   //    aspect    - aspectRatio
   //    fov       - field of view
   //    near      - near plane
   //    far       - far plane
   //    eye       - eye position (for gluLookAt)
   //    look      - look position (for gluLookAt)
   //    up        - up vector (for gluLookAt)
   ////////////////////////////////////////////////////////////////////////////////
   public static void setPerspectiveView(GL2 gl2, float aspect, float fov, float near, float far, float[] eye, float[] look, float[] up) {
      gl2.glMatrixMode(GL2.GL_PROJECTION);
      gl2.glLoadIdentity();
      glu.gluPerspective(fov, aspect, near, far);
      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      gl2.glLoadIdentity();
      glu.gluLookAt(
         eye[0],
         eye[1],
         eye[2],
         look[0],
         look[1],
         look[2],
         up[0],
         up[1],
         up[2]
      );
   }
   public static void setPerspectiveView(GL2 gl2, float aspect, float fov, float near, float far) {
      gl2.glMatrixMode(GL2.GL_PROJECTION);
      gl2.glLoadIdentity();
      glu.gluPerspective(fov, aspect, near, far);
      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      gl2.glLoadIdentity();
   }   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Setup the orthonormal viewing matrix
   ////////////////////////////////////////////////////////////////////////////////
   public static void setOrthonormalView(GL2 gl2, float startX, float endX, float startY, float endY, float startZ, float endZ) {
      gl2.glMatrixMode(GL2.GL_PROJECTION);
      gl2.glLoadIdentity();
      gl2.glOrtho( startX, endX, startY, endY, startZ, endZ);
      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      gl2.glLoadIdentity();      
   }
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Start a perspective picking rendering
   //    mouseX, mouseY - window coordinate, top left is (0, 0)
   //    screenWidth, screenHeight - the size of the current context
   //    fov - field of view
   //    eye, look, up - unit vectors for gluLookAt
   ////////////////////////////////////////////////////////////////////////////////
   public static void startPickingPerspective(GL2 gl2, IntBuffer buffer, 
         int mouseX, int mouseY, 
         int screenWidth, int screenHeight, float fov,
         float[] eye, float[] look, float[] up) {
      startPickingPerspective(gl2, buffer, mouseX, mouseY, screenWidth, screenHeight, fov, 1.0f, 1000.0f, eye, look, up); 
   }
   
   public static void startPickingPerspective(GL2 gl2, IntBuffer buffer, 
                                              int mouseX, int mouseY, 
                                              int screenWidth, int screenHeight, float fov, float near, float far,
                                              float[] eye, float[] look, float[] up) {
      
      float aspect = (float)screenWidth/ (float)screenHeight;
      
      //IntBuffer buffer = GLBuffers.newDirectIntBuffer(512);
      IntBuffer viewport =  (IntBuffer) GLBuffers.newDirectGLBuffer(GL2.GL_UNSIGNED_INT, 4); 
      gl2.glGetIntegerv(GL2.GL_VIEWPORT, viewport);
      gl2.glSelectBuffer(512, buffer);      
      
      gl2.glRenderMode(GL2.GL_SELECT);
      gl2.glInitNames();
      gl2.glPushName(0); // At least one 
      
      // Set up the environment as if rendering
      gl2.glMatrixMode(GL2.GL_PROJECTION);
      gl2.glPushMatrix();
      gl2.glLoadIdentity();
      
      mouseY = viewport.get(3) - mouseY;
      glu.gluPickMatrix((float)mouseX, (float)mouseY, 1.0f, 1.0f, viewport);
      
      glu.gluPerspective(fov, aspect, near, far);
      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      gl2.glLoadIdentity();
      glu.gluLookAt(
         eye[0],
         eye[1],
         eye[2],
         look[0],
         look[1],
         look[2],
         up[0],
         up[1],
         up[2]
      );      
   }
   public static void startPickingPerspective(GL2 gl2, IntBuffer buffer, 
                                              int mouseX, int mouseY, 
                                              int screenWidth, int screenHeight, float fov) {
      
      float aspect = (float)screenWidth/ (float)screenHeight;
      
      //IntBuffer buffer = GLBuffers.newDirectIntBuffer(512);
      IntBuffer viewport =  (IntBuffer) GLBuffers.newDirectGLBuffer(GL2.GL_UNSIGNED_INT, 4); 
      gl2.glGetIntegerv(GL2.GL_VIEWPORT, viewport);
      gl2.glSelectBuffer(512, buffer);      
      
      gl2.glRenderMode(GL2.GL_SELECT);
      gl2.glInitNames();
      gl2.glPushName(0); // At least one 
      
      // Set up the environment as if rendering
      gl2.glMatrixMode(GL2.GL_PROJECTION);
      gl2.glPushMatrix();
      gl2.glLoadIdentity();
      
      mouseY = viewport.get(3) - mouseY;
      glu.gluPickMatrix((float)mouseX, (float)mouseY, 1.0f, 1.0f, viewport);
      
      glu.gluPerspective(fov, aspect, 1.0f, 1000.0f);
      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      gl2.glLoadIdentity();
   }
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Start a orthonormal picking rendering
   //   mouseX, mouseY - windows coordinate, top left is (0, 0)
   //   screenWidth, screenHeight - the size of the current context
   ////////////////////////////////////////////////////////////////////////////////
   public static void startPickingOrtho(GL2 gl2, IntBuffer buffer,
                                        int mouseX, int mouseY,
                                        int screenWidth, int screenHeight) {
      
      IntBuffer viewport =  (IntBuffer) GLBuffers.newDirectGLBuffer(GL2.GL_UNSIGNED_INT, 4); 
      gl2.glGetIntegerv(GL2.GL_VIEWPORT, viewport);
      gl2.glSelectBuffer(512, buffer);      
      
      gl2.glRenderMode(GL2.GL_SELECT);
      gl2.glInitNames();
      gl2.glPushName(0); // At least one 
      
      // Set up the environment as if rendering
      gl2.glMatrixMode(GL2.GL_PROJECTION);
      gl2.glPushMatrix();
      gl2.glLoadIdentity();
      
      mouseY = viewport.get(3) - mouseY;
      glu.gluPickMatrix((float)mouseX, (float)mouseY, 2.0f, 2.0f, viewport);
      gl2.glOrtho(0.0, screenWidth, 0, screenHeight, -10, 10);
      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      gl2.glLoadIdentity();         
   }
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Returns the picked ID, or null 
   ////////////////////////////////////////////////////////////////////////////////
   public static Integer finishPicking(GL2 gl2, IntBuffer buffer) {
      // End drawing stuff
      int hits;
      Integer pick = null;
      gl2.glMatrixMode(GL2.GL_PROJECTION);
      gl2.glPopMatrix();
      gl2.glMatrixMode(GL2.GL_MODELVIEW);
      hits = gl2.glRenderMode(GL2.GL_RENDER);
      
      if (hits > 0) {
         //System.out.println("Something got hit");
         int choose = buffer.get(3);
         int depth  = buffer.get(1);
         
         //System.out.println(choose + " " + depth);
         
         for (int idx = 1; idx < hits; idx++) {
            if(buffer.get(idx*4+1) < depth) {
               choose = buffer.get(idx*4+3);
               depth  = buffer.get(idx*4+1);
            }
         }
         //System.out.println( "ID :" + choose);
         pick = new Integer(choose);   
      }
      return pick;      
   }
   
   
   
   public static int gen1DTexture(GL2 gl2, int seg) {
      FloatBuffer fb = (FloatBuffer)GLBuffers.newDirectGLBuffer(GL2.GL_FLOAT, seg*3);
      for (int i=0; i < seg; i++) {
         fb.put( (float)i/(float)seg);   
         fb.put( (float)i/(float)seg);   
         fb.put( (float)i/(float)seg);   
      }
      fb.flip();
      return gen1DTexture(gl2, fb, seg);     
   }
   
   ////////////////////////////////////////////////////////////////////////////////
   // Generate a 1D openGL texture RGB component, no A
   ////////////////////////////////////////////////////////////////////////////////
   public static int gen1DTexture(GL2 gl2) {
      FloatBuffer fb = (FloatBuffer)GLBuffers.newDirectGLBuffer(GL2.GL_FLOAT, 96);
      for (int i=0; i < 32; i++) {
         fb.put( (float)i/(float)32.0f);   
         fb.put( (float)i/(float)32.0f);   
         fb.put( (float)i/(float)32.0f);   
      }
      /*
      for (int i=0; i < 16; i++) {
         fb.put( (float)i/(float)16.0f);   
         fb.put( (float)i/(float)16.0f);   
         fb.put( (float)i/(float)16.0f);   
      }
      for (int i=0; i < 16; i++) {
         fb.put( (float)(16-i)/(float)16.0f);   
         fb.put( (float)(16-i)/(float)16.0f);   
         fb.put( (float)(16-i)/(float)16.0f);   
      }
      */
      fb.flip();
      return gen1DTexture(gl2, fb, 32);     
   }
   
   public static int gen1DTextureToon(GL2 gl2) {
      FloatBuffer fb = (FloatBuffer)GLBuffers.newDirectGLBuffer(GL2.GL_FLOAT, 12);
      fb.put( 1.0f );
      fb.put( 0.0f );
      fb.put( 0.0f );
      
      fb.put( 1.0f );
      fb.put( 1.0f );
      fb.put( 0.0f );
      
      fb.put( 1.0f );
      fb.put( 0.0f );
      fb.put( 1.0f );
      
      fb.put( 1.0f );
      fb.put( 1.0f );
      fb.put( 0.0f );
      
      fb.flip();
      
      return gen1DTexture(gl2, fb, 4);     
   }
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Generates a 1 dimensional texture. For lookup values, or toon shading, or whatever
   ////////////////////////////////////////////////////////////////////////////////
   public static int gen1DTexture(GL2 gl2, FloatBuffer fb, int size) {
      int textId[] = new int[1];
      
      gl2.glGenTextures(1, textId, 0);
      gl2.glActiveTexture(GL2.GL_TEXTURE4);
      gl2.glBindTexture(GL2.GL_TEXTURE_1D, textId[0]);      
      gl2.glTexParameteri(GL2.GL_TEXTURE_1D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
      gl2.glTexParameteri(GL2.GL_TEXTURE_1D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
      gl2.glTexImage1D(GL2.GL_TEXTURE_1D, 0, GL2.GL_RGB, size, 0, GL.GL_RGB, GL.GL_FLOAT, fb);
      
      return textId[0];
   }
   
   
   
   /////////////////////////////////////////////////////////////////////////////////  
   // Create a Texture with AWTTextureIO factory
   /////////////////////////////////////////////////////////////////////////////////  
   public static Texture createTexture(GL2 gl2, BufferedImage buffer, boolean mipmap) {
      return AWTTextureIO.newTexture(gl2.getGLProfile(), buffer, mipmap);      
      //TextureRenderer renderer = new TextureRenderer(buffer.getWidth(), buffer.getHeight(), true);
   }
   
   
   /////////////////////////////////////////////////////////////////////////////////    
   // Convert the JComponent to (hopefully) a 2D texture that can be mapped to 3D
   // space in JOGL
   /////////////////////////////////////////////////////////////////////////////////    
   public static BufferedImage convertToImage(JComponent comp) throws Exception {
      int compH = comp.getHeight();
      int compW = comp.getWidth();
      
      BufferedImage buffer = new BufferedImage(compW, compH, BufferedImage.TYPE_INT_RGB);
      //if (comp instanceof JPanel) {
         Graphics2D g = buffer.createGraphics();
         comp.paint(g);
      //} else {
      //   throw new Exception("Conversion for " + comp.getClass().toString() + " is not supported");   
      //}
      return buffer;
   }
   
   public static BufferedImage convertJFrame(JFrame comp) throws Exception {
      int compH = comp.getHeight();
      int compW = comp.getWidth();
      
      BufferedImage buffer = new BufferedImage(compW, compH, BufferedImage.TYPE_INT_RGB);
      //if (comp instanceof JPanel) {
         Graphics2D g = buffer.createGraphics();
         comp.paint(g);
      //} else {
      //   throw new Exception("Conversion for " + comp.getClass().toString() + " is not supported");   
      //}
      return buffer;
   }
   
   
   
   //////////////////////////////////////////////////////////////////////////////// 
   // Get the font dimension
   //////////////////////////////////////////////////////////////////////////////// 
   public static double[] getFontDim(String str) {
      g2d.setFont(font);
      Rectangle2D rect = fm.getStringBounds(str, g2d);
      return new double[]{rect.getWidth(), rect.getHeight()};
   }
   
   //////////////////////////////////////////////////////////////////////////////// 
   // More accurate font dimension 
   //////////////////////////////////////////////////////////////////////////////// 
   public static double[] getFontDim(String str, Font ff) {
      GlyphVector gv = ff.createGlyphVector(g2d.getFontRenderContext(), str);
      Rectangle rec = gv.getPixelBounds(g2d.getFontRenderContext(), 0, 0);
      return new double[]{ rec.width*1.1, rec.height*1.1 };
   }
   
   
   public static Texture loadTexture(GL2 gl2, String imgName) {
      TextureData textureData;
      
      try {
         File file = new File(imgName);
        
         // Parse image file to texture data
         textureData = TextureIO.newTextureData(gl2.getGLProfile(),  file, false, null);
         
         /*
         if (textureData.getMustFlipVertically()) {
            System.out.println("Must flip texture");   
         }
         */
         
         // Get image attribute; height, width and channels
         ByteBuffer b = (ByteBuffer) textureData.getBuffer();
         return TextureIO.newTexture(textureData);
         
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }     
   
   
   
   public static GLU glu = new GLU();
   
   public static TextureRenderer texture = new TextureRenderer(100, 100, true, true);
   public static Graphics2D g2d = texture.createGraphics();
   public static FontMetrics fm = g2d.getFontMetrics();
   public static Font font = new Font("Arial", Font.PLAIN, 12);
  
   
}
 