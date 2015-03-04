package sim;

import net.rim.device.api.ui.*;

/**
 * A Manager class which divides its available width between two fields 
 */
public class JustifiedHorizontalFieldManager extends Manager
{
    private Field _leftField;
    private Field _rightField;    
    private boolean _giveLeftFieldPriority;
    
    /**
     * Creates a new JustifiedHorizontalFieldManager
     * @param leftField Field to be positioned on the left
     * @param rightField Field to be positioned on the right
     * @param giveLeftFieldPriority True is the left field is to be given all of its preferred width, false otherwise
     */
    public JustifiedHorizontalFieldManager(Field leftField, Field rightField, boolean giveLeftFieldPriority)
    {
        super(USE_ALL_WIDTH);        
        _leftField = leftField;
        _rightField = rightField;
        
        add(_leftField);
        add(_rightField);
        
        _giveLeftFieldPriority = giveLeftFieldPriority;
    }   
    
     
    /**
     * @see Manager#sublayout(int, int)
     */
    protected void sublayout(int width, int height)
    {
        Field firstField;
        Field secondField;
        if(_giveLeftFieldPriority)
        {
            firstField = _leftField;
            secondField = _rightField;
        }
        else
        {
            firstField = _rightField;
            secondField = _leftField;
        }

        int maxHeight = 0;
        
        int leftFieldLeftMargin = _leftField.getMarginLeft(); 
        int leftFieldRightMargin = _leftField.getMarginRight(); 
        int rightFieldLeftMargin = _rightField.getMarginLeft(); 
        int rightFieldRightMargin = _rightField.getMarginRight(); 
        
        int firstFieldMarginBottom = firstField.getMarginBottom();
        int firstFieldMarginTop = firstField.getMarginTop();
        int secondFieldMarginBottom = secondField.getMarginBottom();
        int secondFieldMarginTop = secondField.getMarginTop();

        int availableWidth = width;
        availableWidth -= leftFieldLeftMargin;
        availableWidth -= Math.max(leftFieldRightMargin, rightFieldLeftMargin);
        availableWidth -= rightFieldRightMargin;

        
        layoutChild(firstField, availableWidth, height - firstFieldMarginTop - firstFieldMarginBottom);
        maxHeight = Math.max(maxHeight, firstFieldMarginTop + firstField.getHeight() + firstFieldMarginBottom);
        availableWidth -= firstField.getWidth();
        
        layoutChild(secondField, availableWidth, height - secondFieldMarginTop - secondFieldMarginBottom);
        maxHeight = Math.max(maxHeight, secondFieldMarginTop + secondField.getHeight() + secondFieldMarginBottom);
        availableWidth -= secondField.getWidth();     
        
        if(!isStyle(Field.USE_ALL_HEIGHT))
        {
            height = maxHeight;
        }
        if(!isStyle(Field.USE_ALL_WIDTH))
        {
            width -= availableWidth;
        }       
        
        setPositionChild(_leftField, leftFieldLeftMargin, 0);
        setPositionChild(_rightField, width - _rightField.getWidth() - rightFieldRightMargin, 0);
   
   
        setExtent(width, height);
    }  
}
