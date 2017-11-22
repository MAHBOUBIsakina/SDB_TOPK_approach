
package basic_approach_one_msg.core;

import peersim.core.Node;

public class Message3 {
    final String value;
    
    final Node sender;
    
    public Message3(Node node){
        this.value = "send data in next bucket";
        this.sender = node;
    }
    
    public Node getSender() {
        return this.sender;
    }
    
    public String getValue() {
        return this.value;
    }
    
    
}
