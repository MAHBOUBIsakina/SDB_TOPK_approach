
package basic_approach_one_msg.core;

import basic_approach_one_msg.core.DataNode;
import basic_approach_one_msg.core.MasterDataNode;
import basic_approach_one_msg.core.Message2;
import basic_approach_one_msg.core.Message2Value;
import basic_approach_one_msg.core.Message3;
import basic_approach_one_msg.core.SimulationController;
import java.util.ArrayList;
import java.util.Arrays;
import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;

public class Step1Protocol implements EDProtocol{
    public Step1Protocol(String prefix){   
    }
    public Object clone(){
        return new Step1Protocol("") ;
    }
    
    @Override

    
        public void processEvent(Node node, int pid, Object event) {
            
           
            double TH;
            MasterDataNode master = (MasterDataNode) Network.get(0);
            if (event instanceof Message3) {
            Message3 msg = (Message3) event;
            if(msg.getSender() != null) {
                double begin = System.currentTimeMillis();
                Bucket_description result = ((DataNode)node).getDataIDinNextBucket();
                double end = System.currentTimeMillis();
                Message2 reply = new Message2(result, node, (end-begin));
                ((Transport)node.getProtocol(FastConfig.getTransport(pid))).send(node, master, reply, pid);
            }             
        }
        else{
            if (event instanceof Message2) {
                if(!(node instanceof MasterDataNode)) return;
                Message2 msg = (Message2) event;
                if (msg.getCalcul_time()>master.getMax_time()) {
                    master.setMax_time(msg.getCalcul_time());
                }
                double begin = System.currentTimeMillis();
                master.putStep1Result((Bucket_description)msg.getValue(), msg.getSender().getIndex());
                double end = System.currentTimeMillis();
                master.setProcess_time(master.getProcess_time()+(end-begin));
                if(master.getNbrResponses()%Network.size()==0) { // Everyone responded
                   SimulationController controller = (SimulationController)Configuration.getInstance(Configuration.getNames("control")[0]);
                    begin = System.currentTimeMillis();
                    ArrayList list = master.getCollectedData();
                    end = System.currentTimeMillis();
                    master.setProcess_time(master.getProcess_time()+(end-begin));
                    controller.askForDataScores(list,master.getProcess_time()+master.getMax_time());
                    
                    master.setMax_time(0);
                    master.setProcess_time(0);
                    master.dataCollectionReinitialzation();
                }
            }
        }            
                    
        }           
                 
}
