/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic_approach_one_msg.core;

import peersim.core.Node;

/**
 *
 * @author sakina
 */
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
