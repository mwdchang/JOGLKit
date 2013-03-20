package examples;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Calendar;

import graphics.DrawUtil;
import graphics.GraphicUtil;
import graphics.TextureFont;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import base.JOGLBase;

////////////////////////////////////////////////////////////////////////////////
// Simple demo illustrating a clock like visualization
////////////////////////////////////////////////////////////////////////////////
public class Clock extends JOGLBase {
   
   
   public TextureFont tf; 
   public Font fontArial = new Font("Arial", Font.PLAIN, 28);
   public DecimalFormat dformat = new DecimalFormat("00");
   public boolean showText = true;

   public void init(GLAutoDrawable g) {
      super.init(g);
      tf = new TextureFont();
   }

   
   @Override
   public void render(GLAutoDrawable g) {
      GL2 gl2 = g.getGL().getGL2();
      
      //GraphicUtil.setPerspectiveView(gl2, (float)viewHeight/(float)viewWidth, 30.0f, 1.0f, 1000.0f, new float[]{0,0,60}, new float[]{0,0,0}, new float[]{0,1,0});
      GraphicUtil.setPerspectiveView(gl2, (float)viewWidth/(float)viewHeight, 30.0f, 1.0f, 1000.0f, new float[]{0,0,60}, new float[]{0,0,0}, new float[]{0,1,0});
      
      
      int hour   = Calendar.getInstance().get(Calendar.HOUR);
      int minute = Calendar.getInstance().get(Calendar.MINUTE);
      int second = Calendar.getInstance().get(Calendar.SECOND);
      int milli  = Calendar.getInstance().get(Calendar.MILLISECOND);
      
      //String str = hour+":"+minute+":"+second;
      String str = dformat.format(hour) + ":" + dformat.format(minute) + ":" + dformat.format(second);
      
      tf.clearMark();
      
      double dim[] = GraphicUtil.getFontDim(str, fontArial);
      tf.width = (float)dim[0];
      tf.height = (float)dim[1];
      tf.anchorX = (float)viewWidth/2.0f - tf.width/2.0f;
      tf.anchorY = (float)viewHeight/2.0f - tf.height/2.0f;
      tf.addMark(str, Color.WHITE, fontArial, 1, 1);
      
      
      // +90 because angles starts at X axis but clock starts at Y axis
      // negative because rotation is counter-clockwise in the graphics API
      double hourAngle   = -360*((double)hour)/12.0 + 90; 
      double minuteAngle = -360*((double)minute)/60.0 + 90;
      double secondAngle = -360*((double)second)/60.0 + 90;
      double milliAngle  = -360*((double)milli)/1000.0 + 90;
      
      
      gl2.glEnable(GL2.GL_BLEND);
      gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
      
      // Hour hand
      gl2.glColor4d(0.2, 0.8, 0.2, 0.5+0.5*(double)hour/12.0);
      DrawUtil.drawArc(gl2, 0, 0, 0, 4, 4.5, hourAngle+5, hourAngle-5, 100, 1);
      
      // minute  
      gl2.glColor4d(1, 0.5, 0, 0.5+0.5*(double)minute/60.0);
      DrawUtil.drawArc(gl2, 0, 0, 0, 5, 5.5, minuteAngle+5, minuteAngle-5, 100, 1);
      
      // secondd
      gl2.glColor4d(0, 0.2, 0.8, 0.5+0.5*((double)second)/60.0);
      DrawUtil.drawArc(gl2, 0, 0, 0, 6, 6.5, secondAngle+5, secondAngle-5, 100, 1);
      
      // milli
      //gl2.glColor4d(1, 1, 1, 0.5);
      //GraphicUtil.drawArc(gl2, 0, 0, 0, 7, 7.1, milliAngle+5, milliAngle-5, 72, 1);
      
      
      // Draw an inner circle
      for (double r=1; r <= 3.5; r+=0.2) {
         gl2.glColor4d(0, 0.4, 0.8, 1-r/4.0f);
         DrawUtil.drawPie(gl2, 0, 0, 0, r, 0, 360, 100);
      }
      
      // Render the text
      if (showText) {
         GraphicUtil.setOrthonormalView(gl2, 0, viewWidth, 0, viewHeight, -1, 1); 
         tf.render(gl2);
      }
      
   }


   @Override
   public void handleKeyPress(int key, char c) {
      super.handleKeyPress(key,c);
      System.out.println("Key Pressed is: " + key);
   }
   
   public void handleMouseClick() {
      showText = !showText;
   }

}
