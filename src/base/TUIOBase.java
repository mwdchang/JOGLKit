package base;

import graphics.DCTriple;

import java.util.Hashtable;
import java.util.Vector;

import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;


////////////////////////////////////////////////////////////////////////////////
// Provide basic touch point heuristics
////////////////////////////////////////////////////////////////////////////////
public class TUIOBase implements TuioListener {
   
   public static float coincindentalPointThreshold = 10;
   public static float reinforceIntentionThreshold = 4;
   public static float realUpdateThreshold = 2;
   public static float movementBufferThreshold = 50;
   
   public TUIOBase(int w, int h) {
      winHeight = h;
      winWidth = w;
   }
   
   @Override
   public void addTuioCursor(TuioCursor tc) {
      
      long sessionID = tc.getSessionID();
      DCTriple point = new DCTriple( tc.getScreenX(winWidth), winHeight - tc.getScreenY(winHeight), 0);
      
      // Sanity check
      if (table.get(sessionID) != null) {
         System.err.println("Hmmm....Adding something new that already exist???");
         return;
      }
      
      
      ////////////////////////////////////////////////////////////////////////////////
      // Heuristic: Coincidental Points
      ////////////////////////////////////////////////////////////////////////////////
      boolean pass = true;
      for (Vector<TPoint> tVector : table.values()) {
         TPoint tp = tVector.lastElement();
         if (tp.pos.sub(point).mag() < coincindentalPointThreshold && tp.state == TPoint.NEW) {
            System.err.println("New points too close together");
            pass = false;   
            break;
         }
      }
      if (pass == false) return;
      
      
      ////////////////////////////////////////////////////////////////////////////////
      // Heuristic: Movement buffer
      ////////////////////////////////////////////////////////////////////////////////
      for (Vector<TPoint> tVector : table.values()) {
         TPoint tp = tVector.lastElement();
         if (tp.pos.sub(point).mag() < movementBufferThreshold && tp.state == TPoint.MOVING) {
            System.err.println("Cannot create new points in movement buffer");
            pass = false;   
            break;
         }
      }
      if (pass == false) return;
      
      
      
      
      Vector<TPoint> v = new Vector<TPoint>();
      v.add( new TPoint(point) );
      table.put(sessionID, v);
      
      System.out.println("Adding new point at : " + point);
   }
   
   
   @Override
   public void updateTuioCursor(TuioCursor tc) {
      long sessionID = tc.getSessionID();
      DCTriple point = new DCTriple( tc.getScreenX(winWidth), winHeight - tc.getScreenY(winHeight), 0);
      
      // Sanity Check
      if ( table.get(sessionID) == null) {
         System.err.println("Hmmm...Updating something that does not exist...?");
         return;
      }
      
      
      // The distance between the last updated point and the current updated point
      TPoint last   = table.get(sessionID).lastElement();
      TPoint update = new TPoint( point );
      
      
      ////////////////////////////////////////////////////////////////////////////////
      // Heuristic: Reinforce Intention
      ///////////////////////////////////////////////////////////////////////////////
      if (table.get(sessionID).size() == 1) {
         if (last.pos.sub(update.pos).mag() < reinforceIntentionThreshold) {
            System.err.println("Cannot determine intention");
            return;
         }
      }
      
      ////////////////////////////////////////////////////////////////////////////////
      // Heuristic: Real update
      ///////////////////////////////////////////////////////////////////////////////
      if (last.pos.sub(update.pos).mag() < realUpdateThreshold) {
         System.err.println("Movement increment too small");
         return;
      }
      
      
      table.get(sessionID).add( update );
      
      
   }
   
   
   @Override
   public void removeTuioCursor(TuioCursor tc) {
      long sessionID = tc.getSessionID();
      DCTriple point = new DCTriple( tc.getScreenX(winWidth), tc.getScreenY(winHeight), 0);
      
      table.remove(sessionID);
   }
   
   @Override
   public void refresh(TuioTime arg0) {
   }

   
   public Hashtable<Long, Vector<TPoint>> table = new Hashtable<Long, Vector<TPoint>>();
   public int winWidth, winHeight;
   
   
   
   ////////////////////////////////////////////////////////////////////////////////
   // Not used
   ////////////////////////////////////////////////////////////////////////////////
   @Override
   public void addTuioObject(TuioObject arg0) {}
   @Override
   public void updateTuioObject(TuioObject arg0) {}
   @Override
   public void removeTuioObject(TuioObject arg0) {}
}
