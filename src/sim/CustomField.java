package sim;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class CustomField extends Field{
    private static final int FIELD_HEIGHT = 40;
    
    private String _text;
    
    
     /**
      * Creates a new CustomField object with provided color and text
      * @param color Background color for this field
      * @param text Text to display in this field
      */
    CustomField(int color, String text)
    {
        super(Field.FOCUSABLE);       
                
        _text = text;
                
        // Set the background color for this field
        Background background = BackgroundFactory.createSolidBackground(color);
        setBackground(background);                  
    }
    
        
    /**
     * @see Field#layout(int, int)
     */
    protected void layout(int width, int height)
    {       
        // Calculate width
        width = Math.min(width, getPreferredWidth());

        // Calculate height
        height = Math.min(height, getPreferredHeight());
        
        setExtent(width, height);
    }
        
        
    /**
    * @see Field#getPreferredHeight()
    */
    public int getPreferredHeight()        
    {
        return FIELD_HEIGHT;        
    } 
       
    
    /**
    * @see Field#getPreferredWidth()
    */
    public int getPreferredWidth()
    {
        return Display.getWidth();
    }
    
        
    /** 
    * @see Field#paint(Graphics)
    */
    protected void paint(Graphics graphics)
    {            
        int rectHeight = getPreferredHeight();
        int rectWidth = getPreferredWidth();                       
        
        graphics.drawRect(0, 0, rectWidth, rectHeight);            
        graphics.drawText(_text, 0 , 0);             
    } 
}
