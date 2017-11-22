/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author sakina
 */
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
            //System.err.println("node "+"le resultat est " + CommonState.getTime());
        if (event instanceof Message3) {
             
            //System.out.println(" -------------------protocol1-----------------");
            Message3 msg = (Message3) event;
            if(msg.getSender() != null) {
                //System.err.println("node "+node.getIndex()+" receive msg from master 0 at " + CommonState.getTime());
                double begin = System.currentTimeMillis();
                Bucket_description result = ((DataNode)node).getDataIDinNextBucket();
                double end = System.currentTimeMillis();
                Message2 reply = new Message2(result, node, (end-begin));
               
                //System.err.println("le temps écoulé pour calculer le résultat est " + CommonState.getTime());
                
                ((Transport)node.getProtocol(FastConfig.getTransport(pid))).send(node, master, reply, pid);
                //System.out.println("-----------------latency between "+ node.getIndex()+" and "+ master.getIndex()+ " is "+((Transport)node.getProtocol(FastConfig.getTransport(pid))).getLatency(node, master));
                
                //System.err.println("le temps écoulé pour répondre au 1ier message est " + CommonState.getTime());
                //System.out.println("Data sent from " + node.getIndex() + " to " + master.getIndex()+" at "+CommonState.getTime());
                
            }             
        }
        else{
            if (event instanceof Message2) {
                //System.out.println(" -------------------protocol1-----------------");
                if(!(node instanceof MasterDataNode)) return;
                Message2 msg = (Message2) event;
                if (msg.getCalcul_time()>master.getMax_time()) {
                    master.setMax_time(msg.getCalcul_time());
                }
                double begin = System.currentTimeMillis();
                master.putStep1Result((Bucket_description)msg.getValue(), msg.getSender().getIndex());
                double end = System.currentTimeMillis();
                master.setProcess_time(master.getProcess_time()+(end-begin));
                //System.err.println("master receive the response from "+msg.getSender().getIndex()+" at " + CommonState.getTime());
                //master.printdataCollection();
                if(master.getNbrResponses()%Network.size()==0) { // Everyone responded
//                    for (int i = 0; i < master.getCollectedData().size(); i++) {
//                        System.out.println("collected data : ids"+Arrays.toString(((Bucket_description)master.getCollectedData().get(i)).dataids)+
//                                "  min"+Arrays.toString(((Bucket_description)master.getCollectedData().get(i)).bucket_min));
//                    }
                    
                    // Process data
                    //System.out.println("Everyone responded! process data and start Step2 ");
                    
                    SimulationController controller = (SimulationController)Configuration.getInstance(Configuration.getNames("control")[0]);
                    begin = System.currentTimeMillis();
                    ArrayList list = master.getCollectedData();
                    end = System.currentTimeMillis();
                    master.setProcess_time(master.getProcess_time()+(end-begin));
                    controller.askForDataScores(list,master.getProcess_time()+master.getMax_time());
                    
                    master.setMax_time(0);
                    master.setProcess_time(0);
                    master.dataCollectionReinitialzation();
                    
                    
                    //this.startStep2(master);
                    
                     
                        
                }
            }
        }            
                    
        }           
                    
                    

        
        
}
