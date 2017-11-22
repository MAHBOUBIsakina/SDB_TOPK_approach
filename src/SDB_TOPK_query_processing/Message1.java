
package basic_approach_one_msg.core;

import peersim.core.Node;

public class Message1 {
    final double value;
    
    final Node sender;
    
    public Message1(double val, Node node) {
        this.value = val;
        this.sender = node;
    }
    
    public Node getSender() {
        return this.sender;
    }
    
    public double getValue() {
        return this.value;
    }
    
}
