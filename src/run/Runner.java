package run;


import leapmotion.LeapTest;
import leapmotion.LeapTest2;
import base.AWTWindow;
import examples.*;
import examples.combine.AnnotateProgram;
import examples.combine.CombineDemo;
import examples.combine.FilterProgram;
import examples.danielstyle.Particle;
import examples.danielstyle.Polaroid;
import examples.danielstyle.SqRot;
import examples.filters.Blur;
import examples.filters.BrightSobel;
import examples.filters.DepthShader;

////////////////////////////////////////////////////////////////////////////////
// A basic bootstrapper to load up JOGL demos
////////////////////////////////////////////////////////////////////////////////
public class Runner {
   
   public static void main(String args[]) {
      AWTWindow base = new AWTWindow();
      //base.setProgram(new Annotate());
      //base.setProgram(new CombineDemo());
      //base.setProgram(new FilterProgram("C:\\Users\\Daniel\\Dropbox\\temp\\Toolkit\\Slide5.PNG"));
      //base.setProgram(new SequenceChart());
      base.setProgram( new Polaroid() );
      //base.setProgram(new BrightSobel());
      //base.setProgram(new Annotate("C:\\Users\\daniel\\Pictures\\New Folder\\seminar_DNPR\\illusion.PNG"));
      base.run("Demo Runner", 800, 800);
   }

}
