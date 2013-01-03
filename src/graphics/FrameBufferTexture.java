package graphics;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;


import com.jogamp.opengl.util.GLBuffers;


/////////////////////////////////////////////////////////////////////////////////
// This class can record the frame buffer into a texture 
// specified by texture_ID.
//
// Example:
//    .... <in init>
//    FrameBufferTexture fbt = new FrameBufferTexture();
//    fbt.init(gl2, width, height);
//
//    .... <in render>
//    fbt.startRecording(gl2);
//        <Things to render goes here>
//    fbt.stopRecording(gl2);
//    
//    shader.bind(gl2);
//    gl2.glActiveTexture(GL2.GL_TEXTURE1);
//    gl2.glBindTextre(GL2.GL_TEXTURE_2D, fbt.texture_ID);
//    shader.setUniform1i(gl2, "texture", 1);
//    shader.unbind(gl2);
//
/////////////////////////////////////////////////////////////////////////////////
public class FrameBufferTexture {
   
   public int texture_FBO; // Frame buffer
   public int texture_RB;  // Render buffer
   public int texture_ID;  // Texture id 
   
   public int TEXTURE_SIZE_W = 800;
   public int TEXTURE_SIZE_H = 800;   
   
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Constructor
   ////////////////////////////////////////////////////////////////////////////////
   public FrameBufferTexture() {
   }   
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Start recording into the FBO/RB
   ////////////////////////////////////////////////////////////////////////////////
   public void startRecording(GL2 gl2) {
      gl2.glBindFramebuffer(GL2.GL_FRAMEBUFFER, texture_FBO);
      gl2.glBindRenderbuffer(GL2.GL_RENDERBUFFER, texture_RB);
      gl2.glPushAttrib( GL2.GL_VIEWPORT_BIT);
         gl2.glViewport(0, 0, TEXTURE_SIZE_W, TEXTURE_SIZE_H);
         gl2.glClear(GL2.GL_COLOR_BUFFER_BIT);
         gl2.glClear(GL2.GL_DEPTH_BUFFER_BIT);
         gl2.glLoadIdentity();
   }   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Stop recording into the frame buffer
   ////////////////////////////////////////////////////////////////////////////////
   public void stopRecording(GL2 gl2) {
      gl2.glPopAttrib();
      
      gl2.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
      gl2.glBindRenderbuffer(GL2.GL_RENDERBUFFER, 0);
   }   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Initializes the frame buffer an rendering buffer for rendering to texture
   ////////////////////////////////////////////////////////////////////////////////
   public void initFrameBuffer(GL2 gl2) {
      int buffer1[] = new int[1];
      int buffer2[] = new int[1];
      int buffer3[] = new int[1];
      
      // Generate a FBO 
      gl2.glGenFramebuffers(1, buffer1, 0);
      texture_FBO = buffer1[0];
      gl2.glBindFramebuffer(GL2.GL_FRAMEBUFFER, texture_FBO);
      
      // Generate a Render Buffer
      gl2.glGenRenderbuffers(1, buffer2, 0);
      texture_RB = buffer2[0];
      gl2.glBindRenderbuffer(GL2.GL_RENDERBUFFER, texture_RB);
      
      // Generate a buffered texture
      gl2.glGenTextures(1, buffer3, 0);
      texture_ID = buffer3[0];
      gl2.glActiveTexture(GL2.GL_TEXTURE0);
      gl2.glBindTexture(GL2.GL_TEXTURE_2D, texture_ID);
      
      // Bind Texture - TODO: Is this really generating a MIPMAP ????
      gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
      gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR); 
      gl2.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA8, TEXTURE_SIZE_W, TEXTURE_SIZE_H, 0, GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, null);
      //gl2.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA8, TEXTURE_SIZE, TEXTURE_SIZE, 0, GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, null);
      gl2.glGenerateMipmap(GL2.GL_TEXTURE_2D);      
      
      // Bind Frame Buffer
      gl2.glFramebufferTexture2D( GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0, GL2.GL_TEXTURE_2D, texture_ID, 0);
      gl2.glDrawBuffer( GL2.GL_COLOR_ATTACHMENT0 );
      
      // Bind the Rendering Buffer
      gl2.glRenderbufferStorage(GL2.GL_RENDERBUFFER, GL.GL_DEPTH_COMPONENT32, TEXTURE_SIZE_W, TEXTURE_SIZE_H);
      gl2.glFramebufferRenderbuffer( GL2.GL_FRAMEBUFFER, GL2.GL_DEPTH_ATTACHMENT, GL2.GL_RENDERBUFFER, texture_RB);
      
      // Clean up
      gl2.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
      gl2.glBindRenderbuffer(GL2.GL_RENDERBUFFER, 0);
   }
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Initialize all the buffers
   //    w  - texture width
   //    h  - texture height
   ////////////////////////////////////////////////////////////////////////////////
   public void init(GL2 gl2, int w, int h) {
     TEXTURE_SIZE_W = w;
     TEXTURE_SIZE_H = h;
     initFrameBuffer(gl2);      
   }
   
   
   
}
