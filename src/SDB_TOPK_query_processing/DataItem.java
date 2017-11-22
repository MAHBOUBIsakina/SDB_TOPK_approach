/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic_approach_one_msg.core;

import peersim.config.Configuration;

/**
 *
 * @author sakina
 */
public class DataItem {
    String id;
    Bounds [] bound;
    
    public DataItem(){
        int node_nbr = Configuration.getInt("SIZE");
        bound = new Bounds[node_nbr];
    }
    
}
