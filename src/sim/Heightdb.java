package sim;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public final class Heightdb {
	RecordStore _rs;
	
    public Heightdb(String name) throws RecordStoreException, java.io.IOException
    {
        _rs = RecordStore.openRecordStore(name, true, RecordStore.AUTHMODE_ANY, false);
    }    
    
    public Heightdb(String recordStoreName, String vendorName, String suiteName) throws RecordStoreException, java.io.IOException
    {
        _rs = RecordStore.openRecordStore( recordStoreName, vendorName, suiteName );
    } 
    
    public synchronized int add(int height) throws java.io.IOException, RecordStoreNotOpenException, RecordStoreException
    {
        Sim sim = new Sim(height);
        byte[] data = sim.toByteArray();
                
        return _rs.addRecord(data, 0, data.length);
    }
    
    public synchronized void edit(int index, int height) throws java.io.IOException, RecordStoreNotOpenException, RecordStoreException
    {
    	Sim sim = new Sim(height);
        byte[] data = sim.toByteArray();        
        _rs.setRecord(index, data, 0, data.length);
    }
    
    public Sim getSim(int recordID) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException, java.io.IOException 
    {
        byte[] data = _rs.getRecord(recordID);
        
        return new Sim(data);
    }   
    
    public synchronized void delete(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException 
    {
        _rs.deleteRecord(recordId);
    }
    
    RecordEnumeration enumerate() throws RecordStoreNotOpenException
    {
        return _rs.enumerateRecords(null, null, true);
    }
}
