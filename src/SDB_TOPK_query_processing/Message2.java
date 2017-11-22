
package basic_approach_one_msg.core;

import peersim.core.Node;

public class Message2 {
    
    final Bucket_description value;
    
    final Node sender;
    
    final double calcul_time;
    
    public Message2(Bucket_description val, Node node, double time) {
        this.value = val;
        this.sender = node;
        this.calcul_time=time;
    }
    
    public Node getSender() {
        return this.sender;
    }
    
    public Bucket_description getValue() {
        return this.value;
    }
    
    public double getCalcul_time(){
        return this.calcul_time;
    }
    
}
