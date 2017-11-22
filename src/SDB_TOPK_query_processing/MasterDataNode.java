
package basic_approach_one_msg.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import peersim.config.Configuration;

public class MasterDataNode extends DataNode {
    private  ArrayList dataCollection;
    private HashMap<String,Bounds> candidatecollection; 
    
    double [] last_seen_bucket_limits;
    int rang;
    
    private int nbrResponses;
    
    private double TH;
    
    private double max_time = 0;
    
    private double process_time= 0;
    
    public MasterDataNode(String prefix) {
        
        super(prefix);
        int size = Configuration.getInt("SIZE");
        dataCollection = new ArrayList();
        candidatecollection = new HashMap(); 
        nbrResponses = 0;
        last_seen_bucket_limits = new double[size];
        rang = 0;
    }

    public void setMax_time(double max_time) {
        this.max_time = max_time;
    }

    public double getMax_time() {
        return max_time;
    }

    public double getProcess_time() {
        return process_time;
    }

    public void setProcess_time(double process_time) {
        this.process_time = process_time;
    }
    
    
    
    
    
    public void dataCollectionReinitialzation(){
        dataCollection.clear();
        nbrResponses = 0;
        rang = 0;
    }
    
    public ArrayList getCollectedData() {
        return this.dataCollection;
    }
    
    public ArrayList putStep1Result(Bucket_description data, int indexNode) {
            dataCollection.add(data);
        this.nbrResponses++;
        return dataCollection;
    }
    
    public int getNbrResponses() {
        return this.nbrResponses;
    }

    public double getTH() {
        return TH;
    }
    

    private double computeOverallScore (double [] localScores){
    
        double overallScore =0;
        for (int k=0; k<localScores.length; ++k)
            overallScore = overallScore + localScores[k];

        return overallScore;    

    }
    
    
    public void putStep2Result(msg2ReturnedScores data){
       dataCollection.add(data);
        
        this.nbrResponses++;
    }
    
    public void putStep3Result(msg3ReturnedScores data){
         dataCollection.add(data);
        this.nbrResponses++;
    }
    
    
    
    public ArrayList getDataCollection(){
        return dataCollection;
    }
    

    public HashMap<String,Bounds> getCandidatecollection (){
        return this.candidatecollection;
    }
    
     
    
    
}
