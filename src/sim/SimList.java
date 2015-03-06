package sim;

import java.util.*;

import net.rim.device.api.lowmemory.LowMemoryManager;
import net.rim.device.api.system.ObjectGroup;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.Comparator;
import net.rim.device.api.util.SimpleSortingVector;

public final class SimList {
    private PersistentObject _persist;
    private Vector _simRecords;    
    private static SimList _instance;    
    private static final long PERSIST = 0x7a746e92888b0db5L; // SimBB
    private static final int MAX_NUM_ORDERED = 100;    
    
    public SimList() 
    {
        _persist = PersistentStore.getPersistentObject( PERSIST );
        _simRecords = (Vector) _persist.getContents();        

        if ( _simRecords == null ) 
        {
            _simRecords = new Vector();            
            _persist.setContents( _simRecords );
            _persist.commit();
        }
    }
    
    static SimList getInstance() 
    {
        if ( _instance == null ) 
        {
            _instance = new SimList();
        }
        return _instance;
    }    
    
    int getNumSimRecords() 
    {
        return _simRecords.size();
    }
    
    int getNumSimRecordsById(int id) 
    {
    	int nbRecord = 0;
    	for (int i = 0; i < _simRecords.size(); i++) {
			if (id == (( SimRecord )_simRecords.elementAt( i )).getId())
				nbRecord++;
		}
        return nbRecord;
    }    
    
    SimRecord getSimRecordAt( int index) 
    {
    	return ( SimRecord )_simRecords.elementAt( index );
    } 
    
    synchronized void insertSimRecord( SimRecord orderRecord ) 
    {
    	_simRecords.insertElementAt(orderRecord, 0);
    }    
    
    synchronized void deleteSimRecord( SimRecord orderRecord ) 
    {
        _simRecords.removeElement( orderRecord );
    }    
    
    synchronized void deleteAllOrderRecords() 
    {
        _simRecords = new Vector();        
        _persist.setContents( _simRecords );
    }    
    
    synchronized void replaceOrderRecordAt( int index, SimRecord newSimRecord ) 
    {
        ObjectGroup.createGroup( newSimRecord );
        _simRecords.setElementAt( newSimRecord, index );
    }    
    
    synchronized boolean removeStaleSimRecords( long before ) 
    {
        if ( _simRecords.size() == 0 ) 
        {
            return false;
        }      
       
       // Sort the order records
       _simRecords = sortVector(_simRecords); // Make sure records are in order by date.
        
        boolean freedData = false;
        
        Vector simRecords = _simRecords;
        
        int i = 0;
        while ( (( SimRecord )simRecords.elementAt(i)).getDate() < before )   // Skip over all stale records.
        {  
            ++i;
        }        
        
        if ( i > 0 ) 
        {
            int numRecordsKept = simRecords.size() - i;
            _simRecords = new Vector();
            
            _persist.setContents( _simRecords );
            
            for ( int j = 0; j < numRecordsKept; ++j ) 
            {
                _simRecords.addElement( simRecords.elementAt( j + i ) ) ;                
            }           
            
            LowMemoryManager.markAsRecoverable( simRecords );
            
            freedData = true;
            
            commit();
        }
        
        return freedData;
    }
    
    private Vector sortVector( Vector records )
    {
        SortableVector sortableVector = new SortableVector();
        
        for(int i = 0; i < records.size(); ++i)
        {
                sortableVector.addElement( records.elementAt(i) );
        }       
        
        sortableVector.reSort();  // Make sure records are in order by date.
            
        for(int i = 0; i < sortableVector.size(); ++i)
        {
                records.setElementAt( sortableVector.elementAt(i), i );
        }
        
        return records;
    }

    synchronized void commit() 
    {
        _persist.commit();
    }
  
    int getNumRecordsToAdd( int totalRecords )
    {
        return ( totalRecords - _simRecords.size() );
    }    
    
    private void addSimRecord( SimRecord orderRecord ) 
    {
        ObjectGroup.createGroup( orderRecord );
        _simRecords.addElement( orderRecord );
    }
    
    /**
     * Implements a sortable vector which sorts based on chronological order.
     */
    final static class SortableVector extends SimpleSortingVector
    {
        // Constructor
        SortableVector()
        {
            setSortComparator( new Comparator() 
            {
                public int compare( Object o1, Object o2 ) 
                {
                	SimRecord r1 = (SimRecord) o1;
                	SimRecord r2 = (SimRecord) o2;
                    
                    if ( r1.getDate() < r2.getDate() ) 
                    {
                        return -1;
                    }
                    
                    if ( r1.getDate() > r2.getDate() ) 
                    {
                        return 1;
                    }
                    
                    return 0;
                }
            });    
        }
    }  
}
