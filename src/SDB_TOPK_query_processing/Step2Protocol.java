
package basic_approach_one_msg.core;

import basic_approach_one_msg.core.CryptedScore;
import basic_approach_one_msg.core.DataNode;
import basic_approach_one_msg.core.MasterDataNode;
import basic_approach_one_msg.core.Message4;
import basic_approach_one_msg.core.Message5;
import basic_approach_one_msg.core.Message5Value;
import basic_approach_one_msg.core.SimulationController;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;

public class Step2Protocol implements EDProtocol{
    public Step2Protocol(String prefix){   
    }
    
    public Object clone(){
        return new Step2Protocol("") ;
    }
    
    public void processEvent(Node node, int pid, Object event) {
       
        
        MasterDataNode master = (MasterDataNode) Network.get(0);
        
        if (event instanceof Message7) {
            Message7 msg = (Message7) event;
            if(msg.getSender() != null) {
                double begin = System.currentTimeMillis();
                msg2ReturnedScores result = ((DataNode)node).getDataItemsScores(msg.getValue());
                double end = System.currentTimeMillis();
                Message6 reply = new Message6(result, node, (end-begin));
                ((Transport)node.getProtocol(FastConfig.getTransport(pid))).send(node, master, reply, pid);
            }  
        
        }
        else{
            if (event instanceof Message6) {
                if(!(node instanceof MasterDataNode)) return;
                Message6 msg = (Message6) event;
                if (msg.getCalcul_time()>master.getMax_time()) {
                        master.setMax_time(msg.getCalcul_time());
                }
                double begin = System.currentTimeMillis();
                master.putStep2Result((msg2ReturnedScores)msg.getValue());
                double end = System.currentTimeMillis();
                master.setProcess_time(master.getProcess_time()+(end-begin));
                
                if(master.getNbrResponses()%Network.size() == 0) { // Everyone responded
                    begin = System.currentTimeMillis();
                    ArrayList candidate = master.getDataCollection();
                    end = System.currentTimeMillis();
                    master.setProcess_time(master.getProcess_time()+(end-begin));
                    SimulationController controller = (SimulationController)Configuration.getInstance(Configuration.getNames("control")[0]);
                    controller.collectdataphase2(candidate,master.getProcess_time()+master.getMax_time());
                    master.setMax_time(0);
                    master.setProcess_time(0);
                    master.dataCollectionReinitialzation();
                }
            }
        }
        
        
    }
}
