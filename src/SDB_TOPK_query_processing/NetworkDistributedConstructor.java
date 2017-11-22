
package basic_approach_one_msg.core;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;
import peersim.core.OverlayGraph;
import peersim.edsim.EDSimulator;
import peersim.edsim.PriorityQ.Event;
import peersim.graph.Graph;

public class NetworkDistributedConstructor implements Control{
    
    private static final String PAR_PROT = "protocol";
    
    
    protected final int protid;
    
    public Graph g=null;
    
    public NetworkDistributedConstructor(String prefix) {
        if( Configuration.contains(prefix + "." + PAR_PROT) )
		protid = Configuration.getPid(prefix + "." + PAR_PROT);
	else
                protid = -10;
    }
    
    @Override
    public boolean execute() {
        Graph gr;
	if(g==null && protid==-10)
	{
		throw new RuntimeException(
			"Neither a protocol, nor a graph is specified.");
	}
	if(g==null) gr = new OverlayGraph(protid,false);
	else gr=g;

	if(gr.size()==0) return false;
	
        final int n = gr.size();
        
        final int master = 0;
        
        for(int i = 1; i < n; i++) {
            gr.setEdge(master, i);
            gr.setEdge(i, master);
        }
        
	
      
        return false;
    }
}
