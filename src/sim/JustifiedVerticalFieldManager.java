/*
 * JustifiedVerticalFieldManager.java
 *
 * Copyright © 1998-2010 Research In Motion Ltd.
 * 
 * Note: For the sake of simplicity, this sample application may not leverage
 * resource bundles and resource strings.  However, it is STRONGLY recommended
 * that application developers make use of the localization features available
 * within the BlackBerry development platform to ensure a seamless application
 * experience across a variety of languages and geographies.  For more information
 * on localizing your application, please refer to the BlackBerry Java Development
 * Environment Development Guide associated with this release.
 */

package sim;

import net.rim.device.api.ui.*;

/**
 * A Manager class which divides its available height between two fields 
 */
public class JustifiedVerticalFieldManager extends Manager
{    
    private Field _topField;
    private Field _bottomField;    
    private boolean _giveTopFieldPriority;
    
    
    /**
     * Creates a new JustifiedVerticalFieldManager
     * @param topField Field to be positioned at the top
     * @param bottomField Field to be positioned at the bottom
     * @param giveTopFieldPriority True is the top field is to be given all of its preferred width, false otherwise
     */
    public JustifiedVerticalFieldManager(Field topField, Field bottomField, boolean giveTopFieldPriority)
    {
        super(USE_ALL_HEIGHT);
        
        _topField = topField;
        _bottomField = bottomField;
        
        add(_topField);
        add(_bottomField);
        
        _giveTopFieldPriority = giveTopFieldPriority;
    }
    
    
    /**
     * @see Manager#sublayout(int, int)
     */
    protected void sublayout(int width, int height)
    {
        Field firstField;
        Field secondField;
        if( _giveTopFieldPriority )
        {
            firstField = _topField;
            secondField = _bottomField;
        }
        else
        {
            firstField = _bottomField;
            secondField = _topField;
        }

        int maxWidth = 0;
        
        int firstFieldLeftMargin = firstField.getMarginLeft(); 
        int firstFieldRightMargin = firstField.getMarginRight(); 
        int secondFieldLeftMargin = secondField.getMarginLeft(); 
        int secondFieldRightMargin = secondField.getMarginRight(); 
        
        int bottomFieldMarginBottom = _bottomField.getMarginBottom();
        int topFieldMarginTop = _topField.getMarginTop();
        
        
        int availableHeight = height;
        availableHeight -= topFieldMarginTop;
        availableHeight -= Math.max( _topField.getMarginBottom(), _bottomField.getMarginTop());
        availableHeight -= bottomFieldMarginBottom;

        layoutChild(firstField, width - firstFieldLeftMargin - firstFieldRightMargin, availableHeight);
        maxWidth = Math.max(maxWidth, firstFieldLeftMargin + firstField.getWidth() + firstFieldRightMargin);
        availableHeight -= firstField.getHeight();
        
        layoutChild(secondField, width - secondFieldLeftMargin - secondFieldRightMargin, availableHeight);
        
        maxWidth = Math.max(maxWidth, secondFieldLeftMargin + secondField.getWidth() + secondFieldRightMargin);
        availableHeight -= secondField.getHeight();
        
        
        setPositionChild(_topField, 0, topFieldMarginTop);        
        setPositionChild(_bottomField, 0, height - _bottomField.getHeight() - bottomFieldMarginBottom);
        setExtent(width, height);       
    }    
}
