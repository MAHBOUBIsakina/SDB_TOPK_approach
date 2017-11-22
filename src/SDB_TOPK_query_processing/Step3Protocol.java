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

/**
 *
 * @author sakina
 */
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
            //System.err.println("node "+"le resultat est " + CommonState.getTime());
        if (event instanceof Message7) {
             
            //System.out.println(" -------------------Message1-----------------");
            Message7 msg = (Message7) event;
            if(msg.getSender() != null) {
                //System.err.println("node "+node.getIndex()+" receive msg from master 0 at " + CommonState.getTime());
                double begin = System.currentTimeMillis();
                msg3ReturnedScores result = ((DataNode)node).getDataItemsCryptedScores(msg.getValue());
                double end = System.currentTimeMillis();
                Message5 reply = new Message5(result, node, (end-begin));
               
                //System.err.println("le temps écoulé pour calculer le résultat est " + CommonState.getTime());
                
                ((Transport)node.getProtocol(FastConfig.getTransport(pid))).send(node, master, reply, pid);
                //System.out.println("-----------------latency between "+ node.getIndex()+" and "+ master.getIndex()+ " is "+((Transport)node.getProtocol(FastConfig.getTransport(pid))).getLatency(node, master));
                
                //System.err.println("le temps écoulé pour répondre au 1ier message est " + CommonState.getTime());
                //System.out.println("Data sent from " + node.getIndex() + " to " + master.getIndex()+" at "+CommonState.getTime());
                
            }             
        }
        else{
            if (event instanceof Message5) {
                //System.out.println(" -------------------Message2-----------------");
                if(!(node instanceof MasterDataNode)) return;
                Message5 msg = (Message5) event;
                if (msg.getCalcul_time()>master.getMax_time()) {
                    master.setMax_time(msg.getCalcul_time());
                }
                double begin = System.currentTimeMillis();
                master.putStep3Result((msg3ReturnedScores)msg.getValue());
                double end = System.currentTimeMillis();
                master.setProcess_time(master.getProcess_time()+(end-begin));
                //System.err.println("master receive the response from "+msg.getSender().getIndex()+" at " + CommonState.getTime());
                //master.printdataCollection();
                if(master.getNbrResponses()%Network.size()==0) { // Everyone responded
                    //System.out.println("'''''''''''''''''''''''''" + master.getNbrResponses());
                    // Process data
                    //System.out.println("Everyone responded! process data and start Step2 ");
                    
                    SimulationController controller = (SimulationController)Configuration.getInstance(Configuration.getNames("control")[0]);
                    begin = System.currentTimeMillis();
                    ArrayList list = master.getCollectedData();
                    //System.out.println("                                    "+list);
                    end = System.currentTimeMillis();
                    master.setProcess_time(master.getProcess_time()+(end-begin));
                    try {
                        controller.collectdataphase3(list, master.getProcess_time()+master.getMax_time());
                    } catch (IOException ex) {
                        Logger.getLogger(Step3Protocol.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    master.setMax_time(0);
                    master.setProcess_time(0);
                    
                    
                    //this.startStep2(master);
                    
                     
                        
                }
            }
        }            
                    
        }           
                    
                    

        
        
}
