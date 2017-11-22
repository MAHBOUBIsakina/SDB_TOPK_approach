
package basic_approach_one_msg.core;

import java.util.ArrayList;
import peersim.core.Node;

public class Message7 {
    final ArrayList value;
    
    final Node sender;
    
    
    
    public Message7(ArrayList val, Node node) {
        this.value = val;
        this.sender = node;
        
    }
    
    public Node getSender() {
        return this.sender;
    }
    
    public ArrayList getValue() {
        return this.value;
    }
    
    
}
