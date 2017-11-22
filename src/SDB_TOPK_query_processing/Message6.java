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
public class Message6 {
    final msg2ReturnedScores value;
    
    final Node sender;
    
    final double calcul_time;
    
    public Message6(msg2ReturnedScores val, Node node, double time) {
        this.value = val;
        this.sender = node;
        this.calcul_time=time;
    }
    
    public Node getSender() {
        return this.sender;
    }
    
    public msg2ReturnedScores getValue() {
        return this.value;
    }
    
    public double getCalcul_time(){
        return this.calcul_time;
    }
    
}
