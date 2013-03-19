package run;


import leapmotion.LeapTest;
import leapmotion.LeapTest2;
import base.AWTWindow;
import examples.*;
import examples.combine.AnnotateProgram;
import examples.danielstyle.SqRot;

////////////////////////////////////////////////////////////////////////////////
// A basic bootstrapper to load up JOGL demos
////////////////////////////////////////////////////////////////////////////////
public class Runner {
   
   public static void main(String args[]) {
      AWTWindow base = new AWTWindow();
      //base.setProgram(new Annotate());
      //base.setProgram(new FrameBuffer2());
      base.setProgram(new AnnotateProgram());
      //base.setProgram(new Annotate("C:\\Users\\daniel\\Pictures\\New Folder\\seminar_DNPR\\illusion.PNG"));
      base.run("Demo Runner", 800, 800);
   }

}
