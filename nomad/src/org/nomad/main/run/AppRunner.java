package org.nomad.main.run;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

import org.nomad.main.Nomad;


import java.awt.*;
import java.lang.Math;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Christian Schneider
 */
public class AppRunner implements Runnable 
{
	/**
	 * @param args The command line arguments
	 */
	public static void main(String[] args)
	{
		try {
			SwingUtilities.invokeAndWait(new AppRunner());
		}
		catch (InterruptedException e) {
			// Ignore: If this exception occurs, we return too early, which
			// makes the splash window go away too early.
			// Nothing to worry about. Maybe we should write a log message.
		}
		catch (InvocationTargetException e) {
			// Error: Startup has failed badly.
			// We can not continue running our application.
			InternalError error = new InternalError();
			error.initCause(e);
			throw error;
		}
	}

    public void run()
    {
	    try { 
	    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
	    } catch (Exception e) { 
	    	e.printStackTrace();
	    }
	
	    Nomad nomad = null;
	
	    try {
	        nomad = new Nomad();
	    }
	    catch (Exception e){
	    	e.printStackTrace();
	        System.exit(1);
	    }
        nomad.validate();
	
	    // center window
	    Dimension screensz  = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension framesz   = nomad.getSize();
	
	    framesz.height = Math.min(framesz.height, screensz.height);
	    framesz.width  = Math.min(framesz.width,  screensz.width);
	
	    nomad.setLocation(
	      (screensz.width-framesz.width)/2,
	      (screensz.height-framesz.height)/2
	    );
	
	    // set close operation, then show window
	    nomad.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    nomad.setVisible(true);
	    //nomad.toFront();
	    nomad.initialLoading();
    }
  
}
