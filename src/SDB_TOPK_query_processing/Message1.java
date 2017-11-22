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
