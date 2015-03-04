package sim;

import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class SimApp extends UiApplication
{
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
        SimApp theApp = new SimApp();       
        theApp.enterEventDispatcher();
    }
    

    /**
     * Creates a new SimApp object
     */
    public SimApp()
    {        
        // Push a screen onto the UI stack for rendering.
        pushScreen(new SimScreen());
    }    
}
