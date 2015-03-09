package sim;

import java.util.Vector;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;

public final class NoteScreen extends MainScreen {
	 // Members -------------------------------------------------------------------------------------
    private boolean _editable;
    private SimRecordController _controller;
    private SimRecord _updatedSimRecord;    
   
    private MenuItem _editItem = new MenuItem( "Edit" , 0, 100 ) 
    {
        public void run() 
        {
            /*outer.*/makeEditScreen();
        }
    };
    
    private MenuItem _saveItem = new MenuItem( "Save" , 0, 200 ) 
    {       
        public void run() 
        {
            if ( /*outer.*/onSave() ) 
            {
                UiApplication.getUiApplication().popScreen( NoteScreen.this );
            }
        }
    };       
    
    public NoteScreen( SimRecord simRecord, boolean editable ) 
    {
        super();
        
        _editable = editable;
        String title;
        
        if ( editable ) 
        {
            title = "Edit Sim Record" ;
        } 
        else 
        {
            title = "View Sim Record" ;
        }
        
        setTitle( new LabelField( title));
        
        _controller = new SimRecordController( simRecord, editable );
        Vector fields = _controller.getFields();
        int numFields = fields.size();
        
        for ( int i = 0; i < numFields; ++i ) 
        {
            add( (Field) fields.elementAt( i ) );
        }
    }    
    
    SimRecord getUpdatedOrderRecord() 
    {
        return _updatedSimRecord;
    }    
    
    protected boolean keyChar( char key, int status, int time ) 
    {
        if ( key == Characters.ENTER ) 
        {
            return onMenu( 0 );
        }
        
        return super.keyChar( key, status, time );
    }    
    
    protected boolean onSave() 
    {
        _updatedSimRecord = _controller.getUpdatedOrderRecord();
        
        return true;
    }    

    protected void makeMenu( Menu menu, int instance ) 
    {
        super.makeMenu( menu, instance );
        
        if ( _editable ) 
        {
            menu.add( _saveItem );
        } 
        else 
        {
            menu.add( _editItem );
        }
    }    
    
    private void makeEditScreen() 
    {
        setTitle( new LabelField( "Edit Sim Record") );
        _editable = true;
        _controller.makeEditable();
    }
}
