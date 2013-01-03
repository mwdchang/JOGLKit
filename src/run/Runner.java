package run;

import base.AWTWindow;
import examples.*;

////////////////////////////////////////////////////////////////////////////////
// A basic bootstrapper to load up JOGL demos
////////////////////////////////////////////////////////////////////////////////
public class Runner {
   
   public static void main(String args[]) {
      AWTWindow base = new AWTWindow();
      //base.setProgram(new Annotate());
      base.setProgram(new FrameBuffer2());
      //base.setProgram(new Annotate("C:\\Users\\daniel\\Pictures\\New Folder\\seminar_DNPR\\illusion.PNG"));
      base.run("Demo Runner", 800, 800);
   }

}
