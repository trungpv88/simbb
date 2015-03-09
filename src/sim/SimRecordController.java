/**
 * Based on 'Memory Demo' sample
 * http://developer.blackberry.com/
 */

package sim;

import java.util.Vector;

import net.rim.device.api.system.ObjectGroup;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.DateField;

public final class SimRecordController {
    private boolean _editable;
    private SimRecord _simRecord;
    private DateField _date;
    private BasicEditField _note;    
           
    public SimRecordController( SimRecord simRecord, boolean editable ) 
    {
        _editable = editable;
        
        if ( editable ) 
        {
            _simRecord = (SimRecord) ObjectGroup.expandGroup( simRecord );
            simRecord = _simRecord;
        } 
        else 
        {
            _simRecord = simRecord;
        }
        
        _simRecord = simRecord;
        
        _date = new DateField( "Date: " , simRecord.getDate(), DateField.DATE );
        _note = new BasicEditField( "Note: " , simRecord.getNote() );
        setFieldsEditable( editable );
    }    
    
    Vector getFields() 
    {
        Vector fields = new Vector();
        
        fields.addElement( _date );
        fields.addElement( _note );
        
        return fields;
    }    
    
    SimRecord getUpdatedOrderRecord() 
    {
        if ( _editable ) 
        {
            _simRecord.setDate( _date.getDate() );
            _simRecord.setNote( _note.getText() );
            
            return _simRecord;
        }
        
        throw new IllegalStateException( "Cannot retrieve an updated record from a non-editable screen." );
    }    
    
    void makeEditable() 
    {
        if ( ! _editable ) 
        {
            _editable = true;
            
            _simRecord = (SimRecord) ObjectGroup.expandGroup( _simRecord );
            
            setFieldsEditable( true );
        }
    }    
    
    private void setFieldsEditable( boolean editable ) 
    {
        _date.setEditable( editable );
        _note.setEditable( editable );
    }
}
