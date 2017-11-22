
package basic_approach_one_msg.core;

import basic_approach_one_msg.core.DataNode;
import basic_approach_one_msg.core.MasterDataNode;
import basic_approach_one_msg.core.Message2;
import basic_approach_one_msg.core.Message2Value;
import basic_approach_one_msg.core.Message3;
import basic_approach_one_msg.core.SimulationController;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

public class Step3Protocol implements EDProtocol{
    
    
    public Step3Protocol(String prefix){   
    }
    
    public Object clone(){
        return new Step3Protocol("") ;
    }
    
    @Override

        public void processEvent(Node node, int pid, Object event) {
            
           
            double TH;
            MasterDataNode master = (MasterDataNode) Network.get(0);
            if (event instanceof Message7) {
            Message7 msg = (Message7) event;
            if(msg.getSender() != null) {
                double begin = System.currentTimeMillis();
                msg3ReturnedScores result = ((DataNode)node).getDataItemsCryptedScores(msg.getValue());
                double end = System.currentTimeMillis();
                Message5 reply = new Message5(result, node, (end-begin));
                ((Transport)node.getProtocol(FastConfig.getTransport(pid))).send(node, master, reply, pid);
            }             
        }
        else{
            if (event instanceof Message5) {
                if(!(node instanceof MasterDataNode)) return;
                Message5 msg = (Message5) event;
                if (msg.getCalcul_time()>master.getMax_time()) {
                    master.setMax_time(msg.getCalcul_time());
                }
                double begin = System.currentTimeMillis();
                master.putStep3Result((msg3ReturnedScores)msg.getValue());
                double end = System.currentTimeMillis();
                master.setProcess_time(master.getProcess_time()+(end-begin));
                if(master.getNbrResponses()%Network.size()==0) { // Everyone responded
                    SimulationController controller = (SimulationController)Configuration.getInstance(Configuration.getNames("control")[0]);
                    begin = System.currentTimeMillis();
                    ArrayList list = master.getCollectedData();
                    end = System.currentTimeMillis();
                    master.setProcess_time(master.getProcess_time()+(end-begin));
                    try {
                        controller.collectdataphase3(list, master.getProcess_time()+master.getMax_time());
                    } catch (IOException ex) {
                        Logger.getLogger(Step3Protocol.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    master.setMax_time(0);
                    master.setProcess_time(0);
                }
            }
        }            
                    
        }           
                    
                    

        
        
}
