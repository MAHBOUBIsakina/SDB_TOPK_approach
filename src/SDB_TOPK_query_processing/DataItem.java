
package basic_approach_one_msg.core;

import peersim.config.Configuration;

public class DataItem {
    String id;
    Bounds [] bound;
    
    public DataItem(){
        int node_nbr = Configuration.getInt("SIZE");
        bound = new Bounds[node_nbr];
    }
    
}
