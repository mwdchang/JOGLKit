package examples;

import graphics.DrawUtil;
import graphics.GraphicUtil;
import graphics.DCColour;
import graphics.TextureFont;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Vector;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import base.JOGLBase;

public class SequenceChart extends JOGLBase {
   
   public double offsetX = 45;

   @Override
   public void render(GLAutoDrawable g) {
      GL2 gl2 = g.getGL().getGL2();
      gl2.glClearColor(1, 1, 1, 1);
      gl2.glDisable(GL2.GL_DEPTH_TEST);
      
      GraphicUtil.setOrthonormalView(gl2, 0, winWidth, 0, winHeight, -1, 1);
      float mulX = 3.0f;
      
      
      for (int barIdx = 0; barIdx < bar.size(); barIdx++) {
         Vector<SCData> thebar = bar.elementAt(barIdx);
         double offset = 0;
         
         
         for (int i = 0; i < thebar.size(); i++) {
            /*
            if (thebar.elementAt(i).category.equals("Time"))       gl2.glColor4d(166.0/255.0, 206.0/255.0, 227.0/255.0, 1);
            else if (thebar.elementAt(i).category.equals("Hier"))  gl2.glColor4d( 31.0/255.0, 120.0/255.0, 180.0/255.0, 1);
            else if (thebar.elementAt(i).category.equals("Scene")) gl2.glColor4d(178.0/255.0, 223.0/255.0, 138.0/255.0, 1);
            else if (thebar.elementAt(i).category.equals("Lens"))  gl2.glColor4d( 51.0/255.0, 160.0/255.0,  44.0/255.0, 1);
            else if (thebar.elementAt(i).category.equals("Comp"))  gl2.glColor4d(251.0/255.0, 154.0/255.0, 153.0/255.0, 1);
            else if (thebar.elementAt(i).category.equals("Idle"))  gl2.glColor4d(1, 1, 1, 1); 
            else gl2.glColor4d(0.6, 0.6, 0.6, 0.8);
            */
            
            gl2.glColor4fv( lookup.get(thebar.elementAt(i).category).toArray(), 0);
            
            gl2.glBegin(GL2.GL_QUADS);
               gl2.glVertex2d(offsetX+offset, winHeight-(10+spacer*barIdx));
               gl2.glVertex2d(offsetX+offset+thebar.elementAt(i).value*mulX, winHeight-(10+spacer*barIdx));
               gl2.glVertex2d(offsetX+offset+thebar.elementAt(i).value*mulX, winHeight-(10+height+spacer*barIdx));
               gl2.glVertex2d(offsetX+offset, winHeight-(10+height+spacer*barIdx));
            gl2.glEnd();
            offset += thebar.elementAt(i).value * mulX;
         }
         gl2.glColor4d(0.6, 0.6, 0.6, 1);
         gl2.glBegin(GL2.GL_LINE_LOOP);
            gl2.glVertex2d(offsetX+0, winHeight-(10+spacer*barIdx));
            gl2.glVertex2d(offsetX+offset, winHeight-(10+spacer*barIdx));
            gl2.glVertex2d(offsetX+offset, winHeight-(10+height+spacer*barIdx));
            gl2.glVertex2d(offsetX+0, winHeight-(10+height+spacer*barIdx));
         gl2.glEnd();
         
         
      }
      
      double lx = 750;
      double ly = 280;
      
            
      gl2.glColor4fv( lookup.get("Time").toArray(), 0);
      DrawUtil.drawQuadFan(gl2, lx, ly, 0, 10, 10);
      
      gl2.glColor4fv( lookup.get("Hier").toArray(), 0);
      DrawUtil.drawQuadFan(gl2, lx, ly-30, 0, 10, 10);
      
      gl2.glColor4fv( lookup.get("Scene").toArray(), 0);
      DrawUtil.drawQuadFan(gl2, lx, ly-60, 0, 10, 10);
      
      gl2.glColor4fv( lookup.get("Lens").toArray(), 0);
      DrawUtil.drawQuadFan(gl2, lx, ly-90, 0, 10, 10);
      
      gl2.glColor4fv( lookup.get("Comp").toArray(), 0);
      DrawUtil.drawQuadFan(gl2, lx, ly-120, 0, 10, 10);
        
      
      tf.anchorX = (float)lx;
      tf.anchorY = (float)(ly-120) - 5.0f;
      tf.render(gl2);
      
      tf2.anchorX = (float)0;
      tf2.anchorY = (float)(winHeight - bar.size()*spacer - 10);
      tf2.render(gl2);
      
   }
   
   
   
   public void init(GLAutoDrawable g) {
      super.init(g);
      GL2 gl2 = g.getGL().getGL2();
      
      tf = new TextureFont();
      tf.height = 180;
      tf.width = 150;
      tf.addMark("Time", Color.BLACK, new Font("Arial", Font.PLAIN, 16), 20, 120);
      tf.addMark("Hierarchy", Color.BLACK, new Font("Arial", Font.PLAIN, 16), 20, 120-30);
      tf.addMark("3D Scene", Color.BLACK, new Font("Arial", Font.PLAIN, 16), 20, 120-60);
      tf.addMark("Lens", Color.BLACK, new Font("Arial", Font.PLAIN, 16), 20, 120-90);
      tf.addMark("Selection", Color.BLACK, new Font("Arial", Font.PLAIN, 16), 20, 120-120);
      
      // Colour brewer set3
      lookup.put("Time", DCColour.fromInt(102, 194, 165));
      lookup.put("Hier", DCColour.fromInt(252, 141, 98));
      lookup.put("Scene", DCColour.fromInt(141, 160, 203));
      lookup.put("Lens", DCColour.fromInt(231, 138, 195));
      lookup.put("Comp", DCColour.fromInt(166, 216, 84));
      lookup.put("DComp", DCColour.fromInt(255, 217, 47));
      lookup.put("Idle", DCColour.fromInt(255, 255, 255));
     
      
      
      Vector<SCData> p1 = new Vector<SCData>();
      p1.add(new SCData(1, "Time"));
      p1.add(new SCData(2, "Scene"));
      p1.add(new SCData(6, "Idle"));
      p1.add(new SCData(27, "Hier"));
      p1.add(new SCData(5, "Idle"));
      p1.add(new SCData(1, "Scene"));
      p1.add(new SCData(1, "Scene"));
      p1.add(new SCData(15, "Scene"));
      p1.add(new SCData(3, "Idle"));
      p1.add(new SCData(5, "Scene"));
      p1.add(new SCData(3, "Idle"));
      p1.add(new SCData(2, "Scene"));
      p1.add(new SCData(2, "Idle"));
      p1.add(new SCData(1, "Scene"));
      p1.add(new SCData(21, "Idle"));
      p1.add(new SCData(3, "Scene"));
      p1.add(new SCData(2, "Scene"));
      p1.add(new SCData(7, "Idle"));
      
      
      Vector<SCData> p2 = new Vector<SCData>();
      p2.add(new SCData(1, "Time"));
      p2.add(new SCData(16, "Idle"));
      p2.add(new SCData(20, "Hier"));
      p2.add(new SCData(22, "Idle"));
      p2.add(new SCData(8, "Lens"));
      p2.add(new SCData(1, "Comp"));
      p2.add(new SCData(50, "Idle"));
      p2.add(new SCData(5, "Lens"));
      p2.add(new SCData(39, "Idle"));
      p2.add(new SCData(3.5, "Lens"));
      p2.add(new SCData(56, "Idle"));
      p2.add(new SCData(7, "Lens"));
      p2.add(new SCData(29, "Idle"));
      p2.add(new SCData(10, "Lens"));
      p2.add(new SCData(17, "Idle"));
      p2.add(new SCData(1, "Lens"));
      p2.add(new SCData(6, "Idle"));
      
      Vector<SCData> p3 = new Vector<SCData>();
      /*
      p3.add(new SCData(1, "Time"));
      p3.add(new SCData(6, "Idle"));
      p3.add(new SCData(1, "Time"));
      */
      p3.add(new SCData(8, "Time"));
      p3.add(new SCData(20, "Idle"));
      p3.add(new SCData(11, "Hier"));
      p3.add(new SCData(6, "Idle"));
      p3.add(new SCData(0.5, "Lens"));
      p3.add(new SCData(1, "Idle"));
      p3.add(new SCData(0.5, "Lens"));
      p3.add(new SCData(1, "Idle"));
      p3.add(new SCData(0.5, "Lens"));
      p3.add(new SCData(4, "Idle"));
      p3.add(new SCData(2.0, "Lens"));
      p3.add(new SCData(1, "Idle"));
      p3.add(new SCData(1.0, "Lens"));
      p3.add(new SCData(17, "Idle"));
      
      /*
      p3.add(new SCData(1, "Time"));
      p3.add(new SCData(42, "Idle"));
      p3.add(new SCData(1, "Time"));
      p3.add(new SCData(58, "Idle"));
      p3.add(new SCData(1, "Time"));
      */
      p3.add(new SCData(103, "Time")); 
      p3.add(new SCData(6, "Idle"));
      p3.add(new SCData(22, "Hier"));
      p3.add(new SCData(7, "Idle"));
      p3.add(new SCData(4.5, "Scene"));
      p3.add(new SCData(7, "Idle"));
      p3.add(new SCData(10, "Scene"));
      p3.add(new SCData(10, "Idle"));
      p3.add(new SCData(21, "Scene"));
      p3.add(new SCData(1, "Idle"));
      p3.add(new SCData(1, "Comp"));
      p3.add(new SCData(2, "Idle"));
      p3.add(new SCData(1, "Comp"));
      p3.add(new SCData(1, "Idle"));
      p3.add(new SCData(1, "Comp"));
      p3.add(new SCData(4, "Idle"));
      p3.add(new SCData(1, "Comp"));
      p3.add(new SCData(2, "Idle"));
      p3.add(new SCData(1, "Comp"));
      p3.add(new SCData(1, "Comp"));
      p3.add(new SCData(1, "Scene"));
      p3.add(new SCData(2, "Idle"));
      p3.add(new SCData(0.5, "Scene"));
      p3.add(new SCData(1, "Idle"));
      p3.add(new SCData(3, "Scene"));
      p3.add(new SCData(1, "Idle"));
      p3.add(new SCData(1, "Comp"));
      p3.add(new SCData(3, "Idle"));
      p3.add(new SCData(5, "Scene"));
      p3.add(new SCData(2, "Idle"));
      
      Vector<SCData> p4 = new Vector<SCData>();
      /*
      p4.add(new SCData(1, "Time"));
      p4.add(new SCData(5, "Idle"));
      p4.add(new SCData(1, "Time"));
      */
      p4.add(new SCData(7, "Time"));
      p4.add(new SCData(20, "Idle"));
      p4.add(new SCData(64, "Hier"));
      p4.add(new SCData(15, "Idle"));
      p4.add(new SCData(0.5, "Scene"));
      p4.add(new SCData(1, "Idle"));
      p4.add(new SCData(0.5, "Scene"));
      p4.add(new SCData(1, "Idle"));
      p4.add(new SCData(0.5, "Scene"));
      p4.add(new SCData(6, "Idle"));
      p4.add(new SCData(1, "Scene"));
      p4.add(new SCData(1, "Idle"));
      p4.add(new SCData(0.5, "Scene"));
      p4.add(new SCData(25, "Idle"));
      
      
      Vector<SCData> p5 = new Vector<SCData>();
      p5.add(new SCData(12, "Time"));
      p5.add(new SCData(9, "Idle"));
      p5.add(new SCData(77, "Hier"));
      p5.add(new SCData(4, "Idle"));
      p5.add(new SCData(5, "Scene"));
      p5.add(new SCData(30, "Idle"));
      
      
      Vector<SCData> p6 = new Vector<SCData>();
      p6.add(new SCData(1, "Time"));
      p6.add(new SCData(10, "Idle"));
      p6.add(new SCData(1, "Hier"));
      p6.add(new SCData(4, "Idle"));
      p6.add(new SCData(0.5, "Lens"));
      p6.add(new SCData(1.5, "Idle"));
      p6.add(new SCData(1.5, "Lens"));
      p6.add(new SCData(28, "Hier"));
      p6.add(new SCData(11, "Idle"));
      p6.add(new SCData(1, "Scene"));
      p6.add(new SCData(6, "Idle"));
      p6.add(new SCData(1, "Lens"));
      p6.add(new SCData(1.5, "Idle"));
      p6.add(new SCData(0.5, "Lens"));
      p6.add(new SCData(10, "Idle"));
      p6.add(new SCData(1, "Lens"));
      p6.add(new SCData(4.5, "Idle"));
      p6.add(new SCData(0.5, "Lens"));
      p6.add(new SCData(1.0, "Idle"));
      p6.add(new SCData(2.0, "Lens"));
      p6.add(new SCData(0.5, "Idle"));
      p6.add(new SCData(1.5, "Lens"));
      p6.add(new SCData(6, "Idle"));
      p6.add(new SCData(1, "Scene"));
      p6.add(new SCData(4, "Idle"));
      p6.add(new SCData(1.5, "Scene"));
      p6.add(new SCData(3, "Idle"));
      p6.add(new SCData(1.5, "Scene"));
      p6.add(new SCData(4, "Idle"));
      p6.add(new SCData(2, "Scene"));
      p6.add(new SCData(1, "Idle"));
      p6.add(new SCData(1.5, "Scene"));
      p6.add(new SCData(3, "Idle"));
      p6.add(new SCData(1, "Scene"));
      p6.add(new SCData(1.5, "Idle"));
      p6.add(new SCData(4.5, "Scene"));
      p6.add(new SCData(5, "Idle"));
      p6.add(new SCData(3, "Scene"));
      p6.add(new SCData(1.5, "Idle"));
      p6.add(new SCData(1.5, "Scene"));
      p6.add(new SCData(2, "Idle"));
      p6.add(new SCData(2, "Scene"));
      p6.add(new SCData(56, "Idle"));
      
      Vector<SCData> p7 = new Vector<SCData>();
      p7.add(new SCData(1, "Time"));
      p7.add(new SCData(17, "Idle"));
      p7.add(new SCData(21, "Hier"));
      p7.add(new SCData(7, "Idle"));
      p7.add(new SCData(1, "Lens"));
      p7.add(new SCData(43, "Idle"));
      p7.add(new SCData(2, "Lens"));
      p7.add(new SCData(6, "Idle"));
      p7.add(new SCData(1, "Lens"));
      p7.add(new SCData(2, "Idle"));
      p7.add(new SCData(3, "Lens"));
      p7.add(new SCData(5, "Idle"));
      p7.add(new SCData(1, "Lens"));
      p7.add(new SCData(1, "Idle"));
      p7.add(new SCData(2, "Lens"));
      p7.add(new SCData(12, "Idle"));
      p7.add(new SCData(2, "Lens"));
      p7.add(new SCData(21, "Idle"));
      
      
      Vector<SCData> p8 = new Vector<SCData>();
      p8.add(new SCData(1, "Time"));
      p8.add(new SCData(8, "Idle"));
      p8.add(new SCData(12, "Hier"));
      p8.add(new SCData(11, "Idle"));
      p8.add(new SCData(5, "Scene"));
      p8.add(new SCData(20, "Idle"));
      p8.add(new SCData(8, "Hier"));
      p8.add(new SCData(9, "Idle"));
      p8.add(new SCData(2, "Scene"));
      p8.add(new SCData(5, "Idle"));
      p8.add(new SCData(4, "Lens"));
      p8.add(new SCData(16, "Idle"));
      p8.add(new SCData(2, "Scene"));
      p8.add(new SCData(1, "Comp"));
      p8.add(new SCData(1, "Idle"));
      p8.add(new SCData(1, "Lens"));
      p8.add(new SCData(1, "Idle"));
      p8.add(new SCData(1, "Comp"));
      p8.add(new SCData(5, "Idle"));
      p8.add(new SCData(1, "Comp"));
      p8.add(new SCData(2, "Idle"));
      p8.add(new SCData(1, "Comp"));
      p8.add(new SCData(2.5, "Idle"));
      p8.add(new SCData(2.5, "Lens"));
      p8.add(new SCData(85, "Idle"));
      
      
      Vector<SCData> p9 = new Vector<SCData>();
      p9.add(new SCData(1, "Time"));
      p9.add(new SCData(19, "Idle"));
      p9.add(new SCData(23, "Hier"));
      p9.add(new SCData(15, "Idle"));
      p9.add(new SCData(1.5, "Lens"));
      p9.add(new SCData(1, "Idle"));
      p9.add(new SCData(1, "Lens"));
      p9.add(new SCData(1.5, "Idle"));
      p9.add(new SCData(3.5, "Lens"));
      p9.add(new SCData(6, "Idle"));
      p9.add(new SCData(13, "Scene"));
      p9.add(new SCData(16.5, "Idle"));
      p9.add(new SCData(5.5, "Scene"));
      p9.add(new SCData(10, "Idle"));
      p9.add(new SCData(1.5, "Scene"));
      p9.add(new SCData(16, "Idle"));
      
      Vector<SCData> p10 = new Vector<SCData>();
      p10.add(new SCData(2, "Time"));
      p10.add(new SCData(18, "Idle"));
      p10.add(new SCData(25, "Hier"));
      p10.add(new SCData(35, "Idle"));
      p10.add(new SCData(1, "Lens"));
      p10.add(new SCData(2.5, "Idle"));
      p10.add(new SCData(2.5, "Scene"));
      p10.add(new SCData(1, "Idle"));
      p10.add(new SCData(1, "Lens"));
      p10.add(new SCData(6, "Idle"));
      
      Vector<SCData> p11 = new Vector<SCData>();
      p11.add(new SCData(7, "Time"));
      p11.add(new SCData(35, "Idle"));
      p11.add(new SCData(38, "Hier"));
      p11.add(new SCData(7, "Idle"));
      p11.add(new SCData(22, "Scene"));
      p11.add(new SCData(1, "Idle"));
      p11.add(new SCData(4, "Scene"));
      p11.add(new SCData(4.5, "Idle"));
      p11.add(new SCData(1.5, "Scene"));
      p11.add(new SCData(0.5, "Idle"));
      p11.add(new SCData(1.5, "Scene"));
      p11.add(new SCData(5, "Idle"));
      p11.add(new SCData(4, "Scene"));
      p11.add(new SCData(2, "Idle"));
      
      
      
      
      bar.add(p1);
      bar.add(p2);
      bar.add(p3);
      bar.add(p4);
      bar.add(p5);
      bar.add(p6);
      bar.add(p7);
      bar.add(p8);
      bar.add(p9);
      bar.add(p10);
      bar.add(p11);
      
      /*
      Vector<SCData> b1 = new Vector<SCData>();
      b1.add(new SCData(15, "A"));
      b1.add(new SCData(11, "B"));
      b1.add(new SCData(1,  "C"));
      b1.add(new SCData(12, "A"));
      
      Vector<SCData> b2 = new Vector<SCData>();
      b2.add(new SCData(60, "A"));
      
      Vector<SCData> b3 = new Vector<SCData>();
      b3.add(new SCData(6, "C"));
      b3.add(new SCData(6, "B"));
      b3.add(new SCData(6, "N"));
      b3.add(new SCData(6, "C"));
      b3.add(new SCData(6, "B"));
      b3.add(new SCData(6, "A"));
      
      bar.add(b1);
      bar.add(b2);
      bar.add(b3);
      */
      
      
      // Get the vector max   
      //double localMax = 0;
      //for (int i=0; i < bar.size(); i++) localMax += bar.elementAt(i).value;   
      
      // Now normalize to a percentage
      //for (int i=0; i < bar.size(); i++) bar.elementAt(i).value /= localMax;
      
      
      // Set up the "legend"
      /*
      Hashtable<String, String> dupe = new Hashtable<String, String>();
      for (int i=0; i < bar.size(); i++) {
         if (dupe.get(bar.elementAt(i).category)!= null) continue;     
         dupe.put(bar.elementAt(i).category, bar.elementAt(i).category);
      }
      */
       
      tf2 = new TextureFont();
      tf2.height = (float)(spacer * bar.size());
      tf2.width = 150;
      for (int i=0; i < bar.size(); i++) {
         tf2.addMark("P" + (i+1), Color.BLACK, new Font("Arial", Font.PLAIN, 16), 10, (float)(tf2.height-i*spacer-15));
      }
      
      for (Vector<SCData> line : bar) {
         double value = 0;
         for (SCData data : line) {
            value += data.value;   
         }
         System.out.println("Value : " + value);
      }
      
      
   }
   
   
   public class SCData {
      public SCData(double v, String s) { value = v; category = s; }
      double value;
      String category;
   }
   
   
   public Hashtable<String, DCColour> lookup = new Hashtable<String, DCColour>();
   
   public Vector<Vector<SCData>> bar = new Vector<Vector<SCData>>();
   public double MAX = 200;
   public TextureFont tf;
   public TextureFont tf2;
   public Vector<TextureFont> legend = new Vector<TextureFont>();
   public double spacer = 28.0;
   public double height = 20.0;

}
