package base;

import java.util.Vector;

import graphics.DCTriple;

////////////////////////////////////////////////////////////////////////////////
// A wrapper for touch points
////////////////////////////////////////////////////////////////////////////////
public class TPoint {
   public TPoint(DCTriple a) {
      pos = a;
      state = 0;
      timestamp = System.currentTimeMillis(); // Not the most accurate method, but works
   }
   
   
   public long sessionID;
   public DCTriple pos;
   public int state;
   public long timestamp;
   
   
   public static final int NEW = 0;
   public static final int MOVING = 1;
   public static final int HOLDING = 2;
   public static final int SWIPING = 3;
   
}
