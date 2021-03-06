/**
 * This application based on 'Wire Frame Layout Demo'
 * and 'Memory Demo' samples (http://developer.blackberry.com/)
 */

package sim;

import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class SimBBApp extends UiApplication
{
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
        SimBBApp theApp = new SimBBApp();       
        theApp.enterEventDispatcher();
    }
    

    /**
     * Creates a new SimApp object
     */
    public SimBBApp()
    {        
        // Push a screen onto the UI stack for rendering.
        pushScreen(new SimBBScreen());
    }    
}
