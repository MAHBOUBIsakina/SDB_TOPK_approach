
package basic_approach_one_msg.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import peersim.config.Configuration;
import peersim.core.Cleanable;
import peersim.core.CommonState;
import static peersim.core.Fallible.DEAD;
import static peersim.core.Fallible.DOWN;
import static peersim.core.Fallible.OK;
import peersim.core.Node;
import static peersim.core.Node.PAR_PROT;
import peersim.core.Protocol;
import peersim.core.Fallible;

public class DataNode implements Node{
    static private int pointer = 0;
    static private int nMasters = 0;
    
    static public int NBR_MASTERS;
    
    public List_EncryptedItems[] data;
    
    public LinkedHashMap<String,Limit> datacopy;
    
    
    protected Protocol[] protocol = null;
    
    
    protected int failstate = Fallible.OK;
    
    
    private int index;
    
    
     private long ID;
     
     private static long counterID = -1;
     
     int list_index;
    
    public DataNode(String prefix){
        NBR_MASTERS = Configuration.getInt("NBR_MASTERS");
        list_index = 0;
                
        String[] names = Configuration.getNames(PAR_PROT);
        CommonState.setNode(this);
        ID=nextID();
        protocol = new Protocol[names.length];
        for (int i=0; i < names.length; i++) {
                CommonState.setPid(i);
                Protocol p = (Protocol) Configuration.getInstance(names[i]); 
                protocol[i] = p; 
        }
        this.data = null;
    }
    
    private long nextID() {

            return counterID++;
    }

    public Object clone() {
        DataNode result = null;
        
        if(nMasters < NBR_MASTERS) {
            result = new MasterDataNode("");
            nMasters++;
        } else {
            try { result=(DataNode)super.clone(); }
            catch( CloneNotSupportedException e ) {} 
        }
        
        result.protocol = new Protocol[protocol.length];
        CommonState.setNode(result);
        result.ID=nextID();
        for(int i=0; i<protocol.length; ++i) {
                CommonState.setPid(i);
                result.protocol[i] = (Protocol)protocol[i].clone();
        }
        
        return result;
    }

    
    public void setData(List_EncryptedItems [] list){
        this.datacopy = new LinkedHashMap<String,Limit>();
        for(int i = 0; i < list.length; i++) {
            Limit temp = new Limit();
            temp.min = list[i].min_bound;
            temp.max = list[i].max_bound;
            temp.score = new byte[list[i].score.length];
            System.arraycopy(list[i].score, 0, temp.score, 0, list[i].score.length);
            this.datacopy.put(list[i].dataID, temp);
            
        }
        
        this.data = new List_EncryptedItems[list.length];
        for (int i = 0; i < list.length; i++) {
            this.data[i] = new List_EncryptedItems();
            this.data[i].dataID = list[i].dataID;
            this.data[i].min_bound = list[i].min_bound;
            this.data[i].max_bound = list[i].max_bound;
            this.data[i].score = new byte[list[i].score.length];
            System.arraycopy(list[i].score, 0, this.data[i].score, 0, list[i].score.length);
        }
    }
    
  
    public Bucket_description getDataIDinNextBucket(){
        int bucket_size = Configuration.getInt("BUCKET_SIZE");
        Bucket_description data_to_return = new Bucket_description();
        data_to_return.dataids = new String[bucket_size];
        data_to_return.bucket_min = new double[1];
        
        if (list_index<data.length) {
            
            int j=0;
            int k =  list_index + bucket_size;
            for (int i =  list_index; i < k; i++) {
            
                data_to_return.dataids[j] = this.data[i].dataID;
            
                j++;
            }
            data_to_return.bucket_min[0] = data[k-1].min_bound;
            list_index = list_index + bucket_size;
        }
       return data_to_return;
    }

    
    public msg2ReturnedScores getDataItemsScores (ArrayList dataids){
       msg2ReturnedScores result = new msg2ReturnedScores();
        result.val = new Message2Value[dataids.size()];
       int l=0;
            for (int i = 0; i < dataids.size(); i++) {
                    result.val[l] = new Message2Value();
                    result.val[l].id = (String)dataids.get(i);
                    result.val[l].min_bound = datacopy.get((String)dataids.get(i)).min;
                    result.val[l].max_bound = datacopy.get((String)dataids.get(i)).max;
                    
                     l++;
                   
            }
           
         result.node = CommonState.getNode().getIndex();
        
        return result;
    }
    
    public msg3ReturnedScores getDataItemsCryptedScores (ArrayList dataids){
        int size = dataids.size();
        msg3ReturnedScores result = new msg3ReturnedScores();
        result.val = new Message5Value[size];
        int l=0;
            for (int i = 0; i < size; i++) {
                String next = (String)dataids.get(i);
                for (int j = 0; j < data.length; j++) {
                   if (next.equals(data[j].dataID)) {
                        result.val[l] = new Message5Value();
                        result.val[l].dataID = next;
                        result.val[l].score = new byte[datacopy.get(next).score.length];
                        for (int k = 0; k < result.val[l].score.length; k++) {
                            result.val[l].score[k] = datacopy.get(next).score[k];
                        }
                        break;
                    }   
                }
                
                l++;
                   
            }
           result.node = CommonState.getNode().getIndex();
        
        return result;
    }

    
    public int getFailState() { return failstate; }

    // ------------------------------------------------------------------

    public boolean isUp() { return failstate==OK; }

    // -----------------------------------------------------------------

    public Protocol getProtocol(int i) { return protocol[i]; }

    //------------------------------------------------------------------

    public int protocolSize() { return protocol.length; }

    //------------------------------------------------------------------

    public int getIndex() { return index; }

    //------------------------------------------------------------------

    public void setIndex(int index) { this.index = index; }

    //------------------------------------------------------------------

   
    public long getID() { return ID; }
    
    public String toString() 
    {
            StringBuffer buffer = new StringBuffer();
            buffer.append("ID: "+ID+" index: "+index+"\n");
            for(int i=0; i<protocol.length; ++i)
            {
                    buffer.append("protocol["+i+"]="+protocol[i]+"\n");
            }
            return buffer.toString();
    }
    
    
        public void setFailState(int failState) {

            if(failstate==DEAD && failState!=DEAD) throw new IllegalStateException(
                    "Cannot change fail state: node is already DEAD");
            switch(failState)
            {
                    case OK:
                            failstate=OK;
                            break;
                    case DEAD:
                            index = -1;
                            failstate = DEAD;
                            CommonState.setNode(this);
                            for(int i=0;i<protocol.length;++i) {
                                    CommonState.setPid(i);
                                    if(protocol[i] instanceof Cleanable)
                                            ((Cleanable)protocol[i]).onKill();
                            }
                            break;
                    case DOWN:
                            failstate = DOWN;
                            break;
                    default:
                            throw new IllegalArgumentException(
                                    "failState="+failState);
            }
    }

    // -----------------------------------------------------------------

    
}
