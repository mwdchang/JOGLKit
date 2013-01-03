package examples;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;


import graphics.GraphicUtil;
import graphics.ShaderObj;

import base.JOGLBase;

////////////////////////////////////////////////////////////////////////////////
// Demonstrates order-independent-transparency
// We use Dual-Depth Peeling technique here, shamelessly ripped out 
// most of the source code (GLSL Shaders) from the JOGL demo package
////////////////////////////////////////////////////////////////////////////////
public class OIT extends JOGLBase {
   
   @Override
   public void render(GLAutoDrawable a) {
      GL2 gl2 = a.getGL().getGL2();
      
      if (this.useOIT == true) {
         this.ProcessDualPeeling(gl2, this.g_quadDisplayList);
         this.RenderDualPeeling(gl2, g_quadDisplayList);
         //this.renderScene(gl2);
      } else {
         this.renderScene2(gl2);
      }
      
   }
   
   public void draw(GL2 gl2) {
      gl2.glColor4d(1.0f, 0.0f, 0.0f, 0.5f);
      gl2.glBegin(GL2.GL_QUADS);
         gl2.glVertex3d(0, 0, -8);
         gl2.glVertex3d(1, 0, -8);
         gl2.glVertex3d(1, 1, -8);
         gl2.glVertex3d(0, 1, -8);
      gl2.glEnd();
      
      gl2.glColor4d(0.0f, 0.0f, 1.0f, 0.5f);
      gl2.glBegin(GL2.GL_QUADS);
         gl2.glVertex3d(0-0.4, 0+0.4, -10);
         gl2.glVertex3d(1-0.4, 0+0.4, -10);
         gl2.glVertex3d(1-0.4, 1+0.4, -10);
         gl2.glVertex3d(0-0.4, 1+0.4, -10);
      gl2.glEnd();        
      
      gl2.glColor4d(0.0f, 1.0f, 0.0f, 0.5f);
      gl2.glBegin(GL2.GL_QUADS);
         gl2.glVertex3d(0-0.2, 0+0.2, -9);
         gl2.glVertex3d(1-0.2, 0+0.2, -9);
         gl2.glVertex3d(1-0.2, 1+0.2, -9);
         gl2.glVertex3d(0-0.2, 1+0.2, -9);
      gl2.glEnd();      
   }
   
   
   public void renderScene(GL2 gl2) {
      GraphicUtil.setPerspectiveView(gl2, viewWidth/viewHeight, 30.0f, 1.0f, 1000.0f);
      
      gl2.glDisable(GL2.GL_DEPTH_TEST);
      gl2.glEnable(GL2.GL_BLEND);
      //gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
      
      g_shaderDualPeel.setUniformf(gl2, "Alpha", 0.5f);
      draw(gl2);
   }
   
   
   public void renderScene2(GL2 gl2) {
      gl2.glClearColor(this.g_clearColour[0], this.g_clearColour[1], this.g_clearColour[2], this.g_clearColour[3]);
      GraphicUtil.setPerspectiveView(gl2, viewWidth/viewHeight, 30.0f, 1.0f, 1000.0f);
      
      gl2.glDisable(GL2.GL_DEPTH_TEST);
      gl2.glEnable(GL2.GL_BLEND);
      gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
      
      draw(gl2);
      
   }
   
   
   public void init(GLAutoDrawable a) {
      super.init(a);    
      
      GL2 gl2 = a.getGL().getGL2();

      g_shaderDualInit = new ShaderObj();
      g_shaderDualInit.createShader(gl2, "src\\shader\\OIT\\dual_peeling_init_vertex.glsl", GL2.GL_VERTEX_SHADER);
      g_shaderDualInit.createShader(gl2, "src\\shader\\OIT\\dual_peeling_init_fragment.glsl", GL2.GL_FRAGMENT_SHADER);
      g_shaderDualInit.createProgram(gl2);
      g_shaderDualInit.linkProgram(gl2);

      g_shaderDualPeel = new ShaderObj();
      g_shaderDualPeel.createShader(gl2, "src\\shader\\OIT\\shade_vertex.glsl", GL2.GL_VERTEX_SHADER);
      g_shaderDualPeel.createShader(gl2, "src\\shader\\OIT\\dual_peeling_peel_vertex.glsl", GL2.GL_VERTEX_SHADER);
      g_shaderDualPeel.createShader(gl2, "src\\shader\\OIT\\shade_fragment.glsl", GL2.GL_FRAGMENT_SHADER);
      g_shaderDualPeel.createShader(gl2, "src\\shader\\OIT\\dual_peeling_peel_fragment.glsl", GL2.GL_FRAGMENT_SHADER);
      g_shaderDualPeel.createProgram(gl2);
      g_shaderDualPeel.linkProgram(gl2);

      g_shaderDualBlend = new ShaderObj();
      g_shaderDualBlend.createShader(gl2, "src\\shader\\OIT\\dual_peeling_blend_vertex.glsl", GL2.GL_VERTEX_SHADER);
      g_shaderDualBlend.createShader(gl2, "src\\shader\\OIT\\dual_peeling_blend_fragment.glsl", GL2.GL_FRAGMENT_SHADER);
      g_shaderDualBlend.createProgram(gl2);
      g_shaderDualBlend.linkProgram(gl2);

      g_shaderDualFinal = new ShaderObj();
      g_shaderDualFinal.createShader(gl2, "src\\shader\\OIT\\dual_peeling_final_vertex.glsl", GL2.GL_VERTEX_SHADER);
      g_shaderDualFinal.createShader(gl2, "src\\shader\\OIT\\dual_peeling_final_fragment.glsl", GL2.GL_FRAGMENT_SHADER);
      g_shaderDualFinal.createProgram(gl2);
      g_shaderDualFinal.linkProgram(gl2);      
      
      g_quadDisplayList = this.MakeFullScreenQuad(gl2);
      InitDualPeelingRenderTargets(gl2);
   }
   
   public void RenderDualPeeling(GL2 gl2, int displayList) {
      // ---------------------------------------------------------------------
      // 3. Final Pass
      // ---------------------------------------------------------------------
      gl2.glDrawBuffer(GL2.GL_BACK);

      g_shaderDualFinal.bind(gl2);
      g_shaderDualFinal.bindTextureRECT(gl2,"FrontBlenderTex", g_dualFrontBlenderTexId[currId], 1);
      g_shaderDualFinal.bindTextureRECT(gl2,"BackBlenderTex", g_dualBackBlenderTexId[0], 2);
      gl2.glCallList(displayList);
      g_shaderDualFinal.unbind(gl2);
     
   }   
   
   void ProcessDualPeeling(GL2 gl, int displayList) {
      gl.glDisable(GL2.GL_DEPTH_TEST);
      gl.glEnable(GL2.GL_BLEND);

      // ---------------------------------------------------------------------
      // 1. Initialize Min-Max Depth Buffer
      // ---------------------------------------------------------------------
      gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, g_dualPeelingSingleFboId[0]);

      // Render targets 1 and 2 store the front and back colors
      // Clear to 0.0 and use MAX blending to filter written color
      // At most one front color and one back color can be written every pass
      gl.glDrawBuffers(2, g_drawBuffers, 1);
      gl.glClearColor(0, 0, 0, 0);
      gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

      // Render target 0 stores (-minDepth, maxDepth, alphaMultiplier)
      gl.glDrawBuffer(g_drawBuffers[0]);
      gl.glClearColor(-MAX_DEPTH, -MAX_DEPTH, 0, 0);
      gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
      gl.glBlendEquation(GL2.GL_MAX);

      g_shaderDualInit.bind(gl);
      renderScene(gl);
      g_shaderDualInit.unbind(gl);

      // ---------------------------------------------------------------------
      // 2. Dual Depth Peeling + Blending
      // ---------------------------------------------------------------------

      // Since we cannot blend the back colors in the geometry passes,
      // we use another render target to do the alpha blending
      //glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, g_dualBackBlenderFboId);
      gl.glDrawBuffer(g_drawBuffers[6]);
      //gl.glClearColor(g_backgroundColor[0], g_backgroundColor[1], g_backgroundColor[2], 0);
      //gl.glClearColor(1.0f, 1.0f, 1.0f, 0);
      gl.glClearColor(this.g_clearColour[0], this.g_clearColour[1], this.g_clearColour[2], this.g_clearColour[3]);
      gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

      

      currId = 0;
      for (int pass = 1; g_useOQ || pass < g_numPasses; pass++) {
         currId = pass % 2;
         int prevId = 1 - currId;
         int bufId = currId * 3;

         //glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, g_dualPeelingFboId[currId]);

         gl.glDrawBuffers(2, g_drawBuffers, bufId+1);
         gl.glClearColor(0, 0, 0, 0);
         gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

         gl.glDrawBuffer(g_drawBuffers[bufId+0]);
         gl.glClearColor(-MAX_DEPTH, -MAX_DEPTH, 0, 0);
         gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

         // Render target 0: RG32F MAX blending
         // Render target 1: RGBA MAX blending
         // Render target 2: RGBA MAX blending
         gl.glDrawBuffers(3, g_drawBuffers, bufId+0);
         gl.glBlendEquation(GL2.GL_MAX);

         g_shaderDualPeel.bind(gl);
         g_shaderDualPeel.bindTextureRECT(gl,"DepthBlenderTex", g_dualDepthTexId[prevId], 0);
         g_shaderDualPeel.bindTextureRECT(gl,"FrontBlenderTex", g_dualFrontBlenderTexId[prevId], 1);
         //g_shaderDualPeel.setUniform(gl,"Alpha", g_opacity, 1);
         // Hackhack
         g_shaderDualPeel.setUniform1fv(gl, "Alpha", g_opacity);
         
         renderScene(gl);
         g_shaderDualPeel.unbind(gl);

         // Full screen pass to alpha-blend the back color
         gl.glDrawBuffer(g_drawBuffers[6]);

         gl.glBlendEquation(GL2.GL_FUNC_ADD);
         gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

         if (g_useOQ) {
            gl.glBeginQuery(GL2.GL_SAMPLES_PASSED, g_queryId[0]);
         }

         g_shaderDualBlend.bind(gl);
         g_shaderDualBlend.bindTextureRECT(gl,"TempTex", g_dualBackTempTexId[currId], 0);
         gl.glCallList(displayList);
         g_shaderDualBlend.unbind(gl);

         if (g_useOQ) {
            gl.glEndQuery(GL2.GL_SAMPLES_PASSED);
            int[] sample_count = new int[]{0};
            gl.glGetQueryObjectuiv(g_queryId[0], GL2.GL_QUERY_RESULT, sample_count, 0);
            if (sample_count[0] == 0) {
               break;
            }
         }
      }
      
      

      gl.glDisable(GL2.GL_BLEND);
      gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
    
   }    
   
   
   int MakeFullScreenQuad(GL2 gl) {
      GLU glu = GLU.createGLU(gl);

      //g_quadDisplayList = gl.glGenLists(1);
      int listId = gl.glGenLists(1);
      gl.glNewList(listId, GL2.GL_COMPILE);

      gl.glMatrixMode(GL2.GL_MODELVIEW);
      gl.glPushMatrix();
      gl.glLoadIdentity();
      glu.gluOrtho2D(0.0f, 1.0f, 0.0f, 1.0f);
      gl.glBegin(GL2.GL_QUADS);
      {
         gl.glVertex2f(0.0f, 0.0f); 
         gl.glVertex2f(1.0f, 0.0f);
         gl.glVertex2f(1.0f, 1.0f);
         gl.glVertex2f(0.0f, 1.0f);
      }
      gl.glEnd();
      gl.glPopMatrix();

      gl.glEndList();
      
      return listId;
   }      
   
   
   // Ported from JOGL demo pakcage 
   // Translated from C++ Version see below:
   //--------------------------------------------------------------------------------------
   // Order Independent Transparency with Dual Depth Peeling
   //
   // Author: Louis Bavoil
   // Email: sdkfeedback@nvidia.com
   //
   // Depth peeling is traditionally used to perform order independent transparency (OIT)
   // with N geometry passes for N transparency layers. Dual depth peeling enables peeling
   // N transparency layers in N/2+1 passes, by peeling from the front and the back
   // simultaneously using a min-max depth buffer. This sample performs either normal or
   // dual depth peeling and blends on the fly.
   //
   // Copyright (c) NVIDIA Corporation. All rights reserved.
   //--------------------------------------------------------------------------------------
     public void InitDualPeelingRenderTargets(GL2 gl) {
        
        gl.glGenTextures(2, g_dualDepthTexId, 0);
        gl.glGenTextures(2, g_dualFrontBlenderTexId, 0);
        gl.glGenTextures(2, g_dualBackTempTexId, 0);
        gl.glGenFramebuffers(1, g_dualPeelingSingleFboId, 0);
        for (int i = 0; i < 2; i++) {
           gl.glBindTexture( GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualDepthTexId[i]);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_WRAP_S,  GL2.GL_CLAMP);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_WRAP_T,  GL2.GL_CLAMP);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_MIN_FILTER,  GL2.GL_NEAREST);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_MAG_FILTER,  GL2.GL_NEAREST);
           
           //gl.glEnable( GL2.GL_PIXEL_UNPACK_BUFFER );
           gl.glTexImage2D( GL2.GL_TEXTURE_RECTANGLE_ARB, 0,  GL2.GL_FLOAT_RG32_NV, 
                 viewWidth, viewHeight,
                 0,  GL2.GL_RGB,  GL2.GL_FLOAT, null);

           gl.glBindTexture( GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualFrontBlenderTexId[i]);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_MIN_FILTER,  GL2.GL_NEAREST);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_MAG_FILTER,  GL2.GL_NEAREST);
           gl.glTexImage2D( GL2.GL_TEXTURE_RECTANGLE_ARB, 0,  GL2.GL_RGBA, 
                 viewWidth, viewHeight,
                 0,  GL2.GL_RGBA,  GL2.GL_FLOAT, null);

           gl.glBindTexture( GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualBackTempTexId[i]);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_MIN_FILTER,  GL2.GL_NEAREST);
           gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_MAG_FILTER,  GL2.GL_NEAREST);
           gl.glTexImage2D( GL2.GL_TEXTURE_RECTANGLE_ARB, 0,  GL2.GL_RGBA, 
                 viewWidth, viewHeight,
                 0,  GL2.GL_RGBA,  GL2.GL_FLOAT, null);
        }

        gl.glGenTextures(1, g_dualBackBlenderTexId, 0);
        gl.glBindTexture( GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualBackBlenderTexId[0]);
        gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
        gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
        gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_MIN_FILTER,  GL2.GL_NEAREST);
        gl.glTexParameteri( GL2.GL_TEXTURE_RECTANGLE_ARB,  GL2.GL_TEXTURE_MAG_FILTER,  GL2.GL_NEAREST);
        gl.glTexImage2D( GL2.GL_TEXTURE_RECTANGLE_ARB, 0, GL2.GL_RGB, 
              viewWidth, viewHeight,
              0, GL2.GL_RGB, GL2.GL_FLOAT, null);

        gl.glGenFramebuffers(1, g_dualBackBlenderFboId, 0);
        gl.glBindFramebuffer( GL2.GL_FRAMEBUFFER, g_dualBackBlenderFboId[0]);
        gl.glFramebufferTexture2D( GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0,
              GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualBackBlenderTexId[0], 0);

        gl.glBindFramebuffer( GL2.GL_FRAMEBUFFER, g_dualPeelingSingleFboId[0]);

        int j = 0;
        gl.glFramebufferTexture2D( GL2.GL_FRAMEBUFFER,  GL2.GL_COLOR_ATTACHMENT0,
              GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualDepthTexId[j], 0);
        gl.glFramebufferTexture2D( GL2.GL_FRAMEBUFFER,  GL2.GL_COLOR_ATTACHMENT1,
              GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualFrontBlenderTexId[j], 0);
        gl.glFramebufferTexture2D( GL2.GL_FRAMEBUFFER,  GL2.GL_COLOR_ATTACHMENT2,
              GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualBackTempTexId[j], 0);

        j = 1;
        gl.glFramebufferTexture2D( GL2.GL_FRAMEBUFFER,  GL2.GL_COLOR_ATTACHMENT3,
              GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualDepthTexId[j], 0);
        gl.glFramebufferTexture2D( GL2.GL_FRAMEBUFFER,  GL2.GL_COLOR_ATTACHMENT4,
              GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualFrontBlenderTexId[j], 0);
        gl.glFramebufferTexture2D( GL2.GL_FRAMEBUFFER,  GL2.GL_COLOR_ATTACHMENT5,
              GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualBackTempTexId[j], 0);

        gl.glFramebufferTexture2D( GL2.GL_FRAMEBUFFER,  GL2.GL_COLOR_ATTACHMENT6,
              GL2.GL_TEXTURE_RECTANGLE_ARB, g_dualBackBlenderTexId[0], 0);
        
        
        // Back to normal ???? - DC
        gl.glBindFramebuffer( GL2.GL_FRAMEBUFFER, 0);
     }

     
     void DeleteDualPeelingRenderTargets(GL2 gl) {
        gl.glDeleteFramebuffers(1, g_dualBackBlenderFboId, 0);
        gl.glDeleteFramebuffers(1, g_dualPeelingSingleFboId, 0);
        gl.glDeleteTextures(2, g_dualDepthTexId, 0);
        gl.glDeleteTextures(2, g_dualFrontBlenderTexId, 0);
        gl.glDeleteTextures(2, g_dualBackTempTexId, 0);
        gl.glDeleteTextures(1, g_dualBackBlenderTexId, 0);
   }   
     
   
   public void handleKeyPress(int key, char c) {
      super.handleKeyPress(key, c);
      if (key == KeyEvent.VK_SPACE) {
         this.useOIT = ! this.useOIT;
         System.out.println("OIT : " + this.useOIT);
      }
   }
   
   
   
   
   public ShaderObj g_shaderDualInit;
   public ShaderObj g_shaderDualPeel;
   public ShaderObj g_shaderDualBlend;
   public ShaderObj g_shaderDualFinal;    
   
   public int currId;
   public int[]  g_dualBackBlenderFboId = new int[1];
   public int[]  g_dualPeelingSingleFboId = new int[1];
   public int[]  g_dualDepthTexId = new int[2];
   public int[]  g_dualFrontBlenderTexId = new int[2];
   public int[]  g_dualBackTempTexId = new int[2];
   public int[]  g_dualBackBlenderTexId = new int[1]; 
   
   public int g_drawBuffers[] = {GL2.GL_COLOR_ATTACHMENT0,
         GL2.GL_COLOR_ATTACHMENT1,
         GL2.GL_COLOR_ATTACHMENT2,
         GL2.GL_COLOR_ATTACHMENT3,
         GL2.GL_COLOR_ATTACHMENT4,
         GL2.GL_COLOR_ATTACHMENT5,
         GL2.GL_COLOR_ATTACHMENT6
   };    
   
   public int g_quadDisplayList;
   public final static float MAX_DEPTH = 1.0f;     
   public boolean g_useOQ = true;
   public int[] g_queryId = new int[1];
   public float[] g_opacity = new float[]{0.5f};      
   public int g_numPasses = 10;
   public float[] g_clearColour = new float[]{1, 1, 1, 0};
   public boolean useOIT = false;
}
