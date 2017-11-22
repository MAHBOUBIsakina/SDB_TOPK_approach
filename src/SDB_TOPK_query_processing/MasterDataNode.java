/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author sakina
 */
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
        //int nbr_node = Configuration.getInt("SIZE");
        
        
            dataCollection.add(data);
        
        
        this.nbrResponses++;
        //last_seen_bucket_limits[indexNode]= data[data.length-1].min_bound;
        return dataCollection;
    }
    
    
    public int getNbrResponses() {
        return this.nbrResponses;
    }

    public double getTH() {
        return TH;
    }
    
    
    
//    public void printdataCollection(){
//        Set keys = this.dataCollection.keySet();
//                Iterator it = keys.iterator();
//                while (it.hasNext()){
//                    String key = (String) it.next(); // tu peux typer plus finement ici
//                    Bounds value = this.dataCollection.get(key); // tu peux typer plus finement ici
//                    System.out.print("the data item is "+key + "   ");
//                    for (int i = 0; i < this.dataCollection.get(key).max_bound.length; i++) {
//                        System.out.print(" min bound "+ this.dataCollection.get(key).min_bound[i]+"  max bound " 
//                                + this.dataCollection.get(key).max_bound[i]);
//                    }
//                    System.out.println();
//                }
//    }
    
//    public boolean StopSortedAccess(){
//        boolean stop = false;
//        
//        int nbr_node = Configuration.getInt("SIZE");
//        int k = Configuration.getInt("K");
//        double [] k_high_scores = new double [k];
//        double global_score;
//        double min_glb_scr=0;
//        int minIndex = 0;
//        double min=0;
//        double th1;
//        Set keys = this.dataCollection.keySet();
//                Iterator it = keys.iterator();
//                while (it.hasNext()){
//                    String key = (String) it.next(); 
//                    global_score = computeOverallScore(this.dataCollection.get(key).min_bound); 
//                    //System.out.println("overall score of " + key + " = " + global_score);
//                    if (global_score > min_glb_scr){
//                        k_high_scores[minIndex] = global_score;
//                        min=k_high_scores[0];
//                        minIndex = 0;
//                        for (int h=1; h<k; ++h){
//                            if (k_high_scores[h]< min) {
//                                min = k_high_scores[h];
//                                minIndex = h;
//                            }
//                        }
//                        min_glb_scr = min;
//                    }
//                    
//                }
//                TH=min_glb_scr;
//                th1 = computeOverallScore(last_seen_bucket_limits);
//                if (min_glb_scr>th1) {
//                    stop = true;
//                }
//                //System.out.println("StopSortedAccess methode return "+stop);
//       return stop;
//    }
    
//    public Set filterResult(){
//        int j = 0;
//        Set keys = this.dataCollection.keySet();
//        Object[] dataitems = keys.toArray();
//        for (int i = 0; i < dataitems.length; i++) {
//            String key = (String) dataitems[i];
//            double maxOverallScore =0;
//            for (int h=0; h<this.dataCollection.get(key).max_bound.length; ++h){
//                if (this.dataCollection.get(key).max_bound[h] == 0) {
//                    maxOverallScore = maxOverallScore + last_seen_bucket_limits[h];
//                }else{
//                    maxOverallScore = maxOverallScore + this.dataCollection.get(key).max_bound[h];
//                }
//            }
//            if (maxOverallScore < TH){
//                this.dataCollection.remove(key);
//                j++;
//            }
//        }
//        System.out.println("number of filtred elements "+j);
//                
//        return this.dataCollection.keySet();
//    }
//    
    private double computeOverallScore (double [] localScores){
    
        double overallScore =0;
        for (int k=0; k<localScores.length; ++k)
            overallScore = overallScore + localScores[k];

        return overallScore;    

    }
    
    
    public void putStep2Result(msg2ReturnedScores data){
        //int nbr_node = Configuration.getInt("SIZE");
        //for(int i = 0; i < data.val.length; i++) {
           dataCollection.add(data);
        //}
        //System.out.println("data colleter 1"+dataCollection.s);
        
        this.nbrResponses++;
    }
    
    public void putStep3Result(msg3ReturnedScores data){
        //int nbr_node = Configuration.getInt("SIZE");
        //for(int i = 0; i < data.length; i++) {
           // System.out.println("   Message5Value[] "+data[i]);
           dataCollection.add(data);
        //}
        
        this.nbrResponses++;
    }
    
    
    
    public ArrayList getDataCollection(){
        return dataCollection;
    }
    
//    public void printcandidateCollection(){
//        Set keys = this.candidatecollection.keySet();
//                Iterator it = keys.iterator();
//                while (it.hasNext()){
//                    String key = (String) it.next(); // tu peux typer plus finement ici
//                    CryptedScore[] value = this.candidatecollection.get(key); // tu peux typer plus finement ici
//                    System.out.print("the data item is "+key + "  its scores are ");
//                    for (int i = 0; i < this.candidatecollection.get(key).length; i++) {
//                        System.out.print(  Arrays.toString(this.candidatecollection.get(key)[i].score));
//                    }
//                    System.out.println();
//                }
//    }
    
    public HashMap<String,Bounds> getCandidatecollection (){
        return this.candidatecollection;
    }
    
     
    
    
}
