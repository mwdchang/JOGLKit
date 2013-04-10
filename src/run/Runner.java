package run;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import leapmotion.LeapTest;
import leapmotion.LeapTest2;
import base.AWTWindow;
import base.JOGLBase;
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
import examples.filters.Sepia;

////////////////////////////////////////////////////////////////////////////////
// A basic bootstrapper to load up JOGL demos
////////////////////////////////////////////////////////////////////////////////
public class Runner {
   
   public static void main(String args[]) {
      
      
//      try {
//         System.out.println("before");
//         Runner.loadJarDll("/jogl_desktop.dll");
//         Runner.loadJarDll("/glugen-rt.dll");
//         System.out.println("after");
//      } catch (Exception e) { e.printStackTrace(); }
      
      AWTWindow base = new AWTWindow();
      base.setProgram(new Sepia());
      //base.setProgram(new CombineDemo());
      //base.setProgram(new FilterProgram("C:\\Users\\Daniel\\Dropbox\\temp\\Toolkit\\Slide5.PNG"));
      //base.setProgram(new SequenceChart());
      //base.setProgram( new Polaroid() );
      //base.setProgram(new BrightSobel());
      //base.setProgram(new Annotate("C:\\Users\\daniel\\Pictures\\New Folder\\seminar_DNPR\\illusion.PNG"));
      //base.run("Demo Runner", 800, 800);
      
      try {
         //base.setProgram( (JOGLBase)Class.forName(args[0]).newInstance() );
         base.run("Demo Runner", 800, 800);
      } catch (Exception e) {}
      
   }
   
   
   public static void loadJarDll(String name) throws IOException {
      InputStream in = (InputStream) Runner.class.getResourceAsStream(name);
      byte[] buffer = new byte[1024];
      int read = -1;
      File temp = File.createTempFile(name, "");
      FileOutputStream fos = new FileOutputStream(temp);

      while((read = in.read(buffer)) != -1) {
          fos.write(buffer, 0, read);
      }
      fos.close();
      in.close();

      System.load(temp.getAbsolutePath());
  }

}
