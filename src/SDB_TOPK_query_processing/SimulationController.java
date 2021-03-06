
package basic_approach_one_msg.core;



import data_encryption.FirstProposition;
import data_encryption.ListElement;
import data_encryption.MyBlowfish;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDSimulator;

public class SimulationController implements Control{
    //--------------------------------------------------------------------------
    //Parameters
    //--------------------------------------------------------------------------
    private static int DELAY = 50;
    /**
     * The protocol for Step 1.
     * @config
     */
    private static final String PAR_PROT_STEP1 = "step1Prtcl";

    /**
     * The protocol for Step 2.
     * @config
     */
    private static final String PAR_PROT_STEP2 = "step2Prtcl";

    /**
     * The protocol for Step 3.
     * @config
     */
    private static final String PAR_PROT_STEP3 = "step3Prtcl";


    //--------------------------------------------------------------------------
    // Fields
    //--------------------------------------------------------------------------

    /** The name of this observer in the configuration */
    private final String name;

    /** Step 1 Protocol id */
    private final int step1pid;

    /** Step 2 Protocol id */
    private final int step2pid;
    
    private static int asking_for_next_buckets = 0;

    /** Step 3 Protocol id */
    private final int step3pid;
    
    //node number
    
    int node_nbr;
    
    
    //secret key
    
    static MyBlowfish bf;
    static double simulation_time;
    byte[] key_xor;
    static double threshold;
    static int asking_for_next_dataItem = 0;
    static int asking_for_next_dataItemScores = 0;
    
    
    
    static HashMap<String,Bounds> collectedData =  new HashMap<>();
    static HashMap<String,EncryptedItem[]> topkcollected;


    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    
    
     
      public static int ieme_nbr_premier(int max){
        int divis, nbr, compt = 1 ;
         boolean Est_premier;
         
         for( nbr = 3; compt < max; nbr += 2 )
         { Est_premier = true;
           for (divis = 2; divis<= nbr/2; divis++ )
             if ( nbr % divis == 0 )
             { Est_premier = false;
                break;
             }
           if (Est_premier)
           {
               compt++;
               
           }
         }
         return nbr;
    }
    
    public SimulationController(String name) {
        this.name = name;
        this.step1pid = Configuration.getPid(name + "." + PAR_PROT_STEP1);
        this.step2pid = Configuration.getPid(name + "." + PAR_PROT_STEP2);
        this.step3pid = Configuration.getPid(name + "." + PAR_PROT_STEP3);
        
        String key_string="huyfhksdlkopijuhygtfreazdfdgoizeuydfuyifkiudf";
        this.key_xor=key_string.getBytes();
        topkcollected = new HashMap<>();
    }
    
    public MyBlowfish getBf (){
       
        return bf;
    }


    //--------------------------------------------------------------------------
    // Methods
    //--------------------------------------------------------------------------

    
    public boolean execute() {
        System.out.println("===========================SimulationController controller start encryption step =====================");
        
        int max_values_score=1000;
        double a=596;
        System.out.println("    "+a);
        int e=ieme_nbr_premier(1000);
        System.out.println("    "+e);
        int Bucket_size=Configuration.getInt("BUCKET_SIZE");//il faut que numR soit un multiple de size
        int K=Configuration.getInt("K");
        int numC=Configuration.getInt("SIZE"),numR=Configuration.getInt("NBR_ELEMENTS");
        FirstProposition fp;
        try {
            fp = new FirstProposition(numC,numR,max_values_score,Bucket_size);
            System.out.println("creation de la base est terminée");
            fp.db.sortLists();
            System.out.println("sort fini");
            fp.encode_using_xor(key_xor);        
            System.out.println("chiffrement XOR FINI");
            bf = new MyBlowfish();
            bf.generateKey();
            fp.encode_using_Blowfish(this.getBf());
            
            System.out.println("CHIFFREMENT bLOWFISH fini");
            fp.baquetization_equal_packets(Bucket_size,a,e);
            fp.db = null;
            System.out.println("baquetization fini");
            List_EncryptedItems temp = null;
            for (int j = 0; j < fp.db1.m; j++) {
                List_EncryptedItems [] list = new List_EncryptedItems[fp.db1.n];
                for (int i = 0; i < fp.db1.n; i++) {
                    temp = new List_EncryptedItems();
                    temp.dataID = fp.db1.listsCR[j].elementsCR[i].dataID;
                    temp.score = new byte[fp.db1.listsCR[j].elementsCR[i].score.length];
                    System.arraycopy(fp.db1.listsCR[j].elementsCR[i].score, 0, temp.score, 0, fp.db1.listsCR[j].elementsCR[i].score.length); //h++;
                    temp.min_bound = fp.db1.listsCR[j].elementsCR[i].PID;
                    temp.max_bound = fp.score_sup.get(fp.db1.listsCR[j].elementsCR[i].dataID)[j];
                    list[i] = temp;
            
                }
            ((DataNode) Network.get(j)).setData(list);
        }
        System.out.println("===========================SimulationController controller end encryption step =====================");
        } catch (IOException ex) {
             Logger.getLogger(DataEncryption.class.getName()).log(Level.SEVERE, null, ex);
         }
        System.out.println("========================= Step 1 Starting ====================");

        Node master = Network.get(0); //Network.prototype;
        double begin = System.currentTimeMillis();
        Message3 msg = new Message3(master);
        asking_for_next_dataItem++;
        for (int i = 0; i < Network.size(); i++) {
            Node n = Network.get(i);
            EDSimulator.add(DELAY, msg, n, this.step1pid);
            }
        double end = System.currentTimeMillis();
        simulation_time = simulation_time + (end - begin);

        return (false);
    }
    
    
    public void collectdataphase2(ArrayList data, double time){
        double begin = System.currentTimeMillis();
        int nbr_node = Configuration.getInt("SIZE");
        for(int i = 0; i < data.size(); i++) {
            msg2ReturnedScores object = ((msg2ReturnedScores)data.get(i));
            for (int l = 0; l <object.val.length ; l++) {
                if (!(collectedData.containsKey(object.val[l].id))) {
                    Bounds temp = new Bounds(nbr_node);
                    temp.min_bound[object.node] = object.val[l].min_bound;
                    temp.max_bound[object.node] = object.val[l].min_bound;
                    collectedData.put(object.val[l].id, temp);
                }else{
                    if (collectedData.get(object.val[l].id).min_bound[object.node] == 0) {
                        collectedData.get(object.val[l].id).min_bound[object.node] = object.val[l].min_bound;
                    }
                    if (collectedData.get(object.val[l].id).max_bound[object.node] == 0) {
                        collectedData.get(object.val[l].id).max_bound[object.node] = object.val[l].max_bound;
                    }


                }
            }
            
            
            
            
        }        
        double end = System.currentTimeMillis();
        simulation_time = simulation_time + (end - begin)+time;
        StopSortedAccess();
        
    }
    
    
     public boolean StopSortedAccess(){
        double begin = System.currentTimeMillis();
        boolean stop = false;
        int k = Configuration.getInt("K");
        double [] k_high_scores = new double [k];
        double global_score;
        
        double min_glb_scr=0;
        int minIndex = 0;
        double min=0;
        Set keys = collectedData.keySet();
                Iterator it = keys.iterator();
                while (it.hasNext()){
                    String key = (String) it.next(); 
                    global_score = computeOverallScore(collectedData.get(key).min_bound); 
                   if (global_score > min_glb_scr){
                        k_high_scores[minIndex] = global_score;
                        min=k_high_scores[0];
                        minIndex = 0;
                        for (int h=1; h<k; ++h){
                            if (k_high_scores[h]< min) {
                                min = k_high_scores[h];
                                minIndex = h;
                            }
                        }
                        min_glb_scr = min;
                    }
                    
                }
                if (min_glb_scr>threshold) {
                    double global_score_max;
                    ArrayList topkcandidate = new ArrayList();
                    int h = 0;
                    it = keys.iterator();
                    while (it.hasNext()){
                        String key = (String) it.next(); 
                        global_score_max = computeOverallScore(collectedData.get(key).max_bound); 
                        if (global_score_max >= min_glb_scr){
                            topkcandidate.add(key);
                        }

                    }
                    MasterDataNode master = (MasterDataNode)Network.get(0);
                    Message7 msg = new Message7(topkcandidate,master);
                    for (int i = 0; i < Network.size(); i++) {
                        Node n = Network.get(i);
                        EDSimulator.add(DELAY, msg, n, this.step3pid);
                    }
                    collectedData = null;
                    
                }else{
                    MasterDataNode master = (MasterDataNode)Network.get(0);
                    asking_for_next_dataItem++;
                    Message3 msg = new Message3(master);
                    for (int i = 0; i < Network.size(); i++) {
                        Node n = Network.get(i);
                        EDSimulator.add(DELAY, msg, n, this.step1pid);
                        }
            
                }
                
                double end = System.currentTimeMillis();
                    simulation_time = simulation_time  +(end - begin);
               return stop;
    }
    
    
    public void askForDataScores(ArrayList data, double time ){
        double begin = System.currentTimeMillis();
        int leng1 = data.size();
        double [] THscores = new double[leng1];
        ArrayList dataids = new ArrayList();
        
        for (int i = 0; i < leng1; i++) {
            THscores[i] = ((Bucket_description)data.get(i)).bucket_min[0];
            int leng2 = ((Bucket_description)data.get(i)).dataids.length;
            for (int j = 0; j < leng2; j++) {
                if(!dataids.contains(((Bucket_description)data.get(i)).dataids[j]))
                    dataids.add(((Bucket_description)data.get(i)).dataids[j]);
                
            }
           }
        threshold = computeOverallScore(THscores);
        MasterDataNode master = (MasterDataNode)Network.get(0);
        Message7 msg = new Message7(dataids,master);
        asking_for_next_dataItemScores++;
            for (int i = 0; i < Network.size(); i++) {
                Node n = Network.get(i);
                EDSimulator.add(DELAY, msg, n, this.step2pid);
            }
            double end = System.currentTimeMillis();
            simulation_time = simulation_time + (end - begin)+time;
    }
    
    
    
    public void AskForNextBucket(double time){
        double begin = System.currentTimeMillis();
        MasterDataNode master = (MasterDataNode)Network.get(0);
        Message3 msg = new Message3(master);
        asking_for_next_dataItem++;
        for (int i = 0; i < Network.size(); i++) {
            Node n = Network.get(i);
            EDSimulator.add(DELAY, msg, n, this.step1pid);
            }
        double end = System.currentTimeMillis();
        simulation_time = simulation_time + time +(end - begin);
    }
    
   
    
    public void collectdataphase3(ArrayList data, double time) throws IOException{
        double begin = System.currentTimeMillis();
        int nbr_node = Configuration.getInt("SIZE");
        for(int i = 0; i < data.size(); i++) {
            msg3ReturnedScores object = (msg3ReturnedScores)data.get(i);
            for (int l = 0; l < object.val.length; l++) {
                String id = object.val[l].dataID;
            
                if (!(topkcollected.containsKey(id))) {
                    EncryptedItem []temp = new EncryptedItem[nbr_node];
                    for (int j = 0; j < temp.length; j++) {
                        temp[j] = new EncryptedItem();
                    }
                    temp[object.node].score = new byte[object.val[l].score.length];
                    System.arraycopy(object.val[l].score, 0, temp[object.node].score, 0, object.val[l].score.length);

                    topkcollected.put(id, temp);
                }else{

                    if(topkcollected.get(id)[object.node].score == null){
                        topkcollected.get(id)[object.node].score = new byte[object.val[l].score.length];
                        System.arraycopy(object.val[l].score, 0, topkcollected.get(id)[object.node].score, 0, object.val[l].score.length);
                    }
                }
            }
        }
        double end = System.currentTimeMillis();
        simulation_time = simulation_time + (end - begin)+time;
        startLastStep();
    }
    
    public ListElement[] startLastStep() throws UnsupportedEncodingException, IOException {
        
        System.out.println("========================= Last Step Starting ====================");
        
        double begin = System.currentTimeMillis();
        int k = Configuration.getInt("K");
        ListElement [] final_result = new ListElement [k];
        for (int i = 0; i < k; i++) {
            final_result[i]=new ListElement();
            final_result[i].dataID="";
            final_result[i].score=0;
        }
        System.out.println("the number of data items to decrypt is "+ topkcollected.size());
        HashMap<String,double[]> result = new HashMap<String,double[]>();
        node_nbr = Configuration.getInt("SIZE");
        double global_score;
        double min;
        int index_min;
        double min_glb_scr=0;
        double start_decryption = System.currentTimeMillis();
        Set listKeys=topkcollected.keySet();  
    		Iterator it=listKeys.iterator();
    		while(it.hasNext()){
                    String key= (String)it.next();
                    int spos = 0;
                    byte [] ciphertext=(new sun.misc.BASE64Decoder().decodeBuffer(key));
                    byte [] ciphertext1=new byte [ciphertext.length];
                    for (int pos = 0; pos < ciphertext.length; ++pos) {
                        ciphertext1[pos] = (byte) (ciphertext[pos] ^ key_xor[spos]);
                        ++spos;
                        if (spos >= key_xor.length) {
                            spos = 0;
                        }
                    }
                    String dataID=new String(ciphertext1, "UTF-8");
                    result.put(dataID, new double[node_nbr]);
                    for(int l=0;l<topkcollected.get(key).length;l++){
                        result.get(dataID)[l]= this.bf.decryptInDouble(topkcollected.get(key)[l].score);
                        }
                }
        double finish_decryption = System.currentTimeMillis();
        double start_extraction_k_data_item = System.currentTimeMillis();
        listKeys=result.keySet();  
    	Iterator it2=listKeys.iterator();
    		while(it2.hasNext()){
                    String key= (String)it2.next();
                    global_score=computeOverallScore(result.get(key));
                    if (global_score > min_glb_scr){
                        min=final_result[0].score;
                        index_min=0;
                        for (int j = 0; j < k; j++) {
                            if(final_result[j].score<min){
                                min = final_result[j].score;
                                index_min=j;
                            }
                        }
                        if (global_score > min){
                            final_result[index_min].score=global_score;
                            final_result[index_min].dataID=key;
                        }
                        min=final_result[0].score;
                        for (int j = 0; j < k; j++) {
                            if(final_result[j].score<min){
                                min = final_result[j].score;
                            }
                        }
                        min_glb_scr = min;
                    }
                }
                double finish_extraction_k_data_item = System.currentTimeMillis();
                double end = System.currentTimeMillis();
                System.out.println ("the k top-k elements are  " );
                for (int j = 0; j < final_result.length; j++) {
                    System.out.println ("  "+final_result[j].dataID+ "     "+final_result[j].score);
                }
                simulation_time = simulation_time + (end - begin);
                System.out.println("simulation time is  "+ simulation_time);
                System.out.println("decryption time is "+ (finish_decryption-start_decryption));
                System.out.println("extraction top_k data item time is "+ (finish_extraction_k_data_item-start_extraction_k_data_item));
                System.out.println(" server asks the nodes the next bucket " + asking_for_next_dataItem + " time ");
                System.out.println(" server asks the nodes the next bucket scores" + asking_for_next_dataItemScores + " time ");
        return final_result;
    }
    
   private double computeOverallScore (double [] localScores){
        double overallScore =0;
        for (int k=0; k<localScores.length; ++k)
        overallScore = overallScore + localScores[k];
        return overallScore;
    }
  
}
