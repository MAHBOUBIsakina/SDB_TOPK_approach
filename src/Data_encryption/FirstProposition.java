package data_encryption;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

public class FirstProposition {
    int numColumns;
    int numRows;
    
    public Database db;
    public EncryptedDatabase db1;
    Vector<tempon_ansewer_ele> Answers;
    Vector<AnswerElementCR> answers_client;
    Vector<AnswerElementCR> AnswersTA;
    top_k_answer [] top_k_Answers;
    top_k_answer [] top_k_AnswersTA;
    Vector<AnswerElement> Answer_clair;
    int size_intrv;
    Map<String,int[]> positions = new HashMap<String,int[]>();
    public Map<String,double[]> score_sup = new HashMap<String,double[]>();
    public FirstProposition(int numC, int numR, int max_values_score,int size) throws IOException {
        numColumns=numC;
        numRows=numR;
        size_intrv=size;
        db= new Database(numColumns, numRows,size_intrv);
        db1=new EncryptedDatabase(numColumns, numRows,max_values_score,size);
        Answers=new Vector<tempon_ansewer_ele>();
        answers_client=new  Vector<AnswerElementCR> (numColumns);
        AnswersTA=new  Vector<AnswerElementCR> (numColumns);
        Answer_clair= new Vector<AnswerElement> (numColumns);
       }
    public FirstProposition(int numC, int numR, int max_values_score) throws IOException {
        numColumns=numC;
        numRows=numR;
        db= new Database(numColumns, numRows,size_intrv);
        Answers=new Vector<tempon_ansewer_ele>();
        answers_client=new  Vector<AnswerElementCR> (numColumns);
        AnswersTA=new  Vector<AnswerElementCR> (numColumns);
        Answer_clair= new Vector<AnswerElement> (numColumns);
       }
    
   
    public void encode_using_xor(byte [] secret_key) {
    
   try{
        for(int i=0;i<numColumns;i++ ){
            for (int j=0;j<numRows;j++){
                
                byte [] plaintext=db.lists[i].elements[j].dataID.getBytes("UTF-8");  
                byte [] ciphertext = new byte[plaintext.length];
                int spos = 0;
                for (int k=0;k<plaintext.length;k++){
                    ciphertext[k]=(byte) (plaintext[k]^secret_key[spos]);
                    spos++;
                    if (spos >= secret_key.length) {
                        spos = 0;
                    }
                }
                db1.listsCR[i].elementsCR[j].dataID=new sun.misc.BASE64Encoder().encode(ciphertext);
                }
        }
        
    }catch(UnsupportedEncodingException e){
        System.out.println("erreur");
        }
    }
   
    public void decode_DB(byte [] secret_key,MyBlowfish blf){
        Vector<ListElement> Answer_claire1 = new Vector<ListElement>();
       try{
            int i=0;
            while (i<answers_client.size()){
                AnswerElement temp = new AnswerElement(numColumns);
                int spos = 0;
                byte [] ciphertext=(new sun.misc.BASE64Decoder().decodeBuffer(answers_client.elementAt(i).dataID));
                byte [] ciphertext1=new byte [ciphertext.length];
                for (int pos = 0; pos < ciphertext.length; ++pos) {
                    ciphertext1[pos] = (byte) (ciphertext[pos] ^ secret_key[spos]);
                    ++spos;
                    if (spos >= secret_key.length) {
                        spos = 0;
                    }
                }
                temp.dataID=new String(ciphertext1, "UTF-8");
                for(int k=0;k<numColumns;k++){
                       temp.local_scores[k]=blf.decryptInDouble(answers_client.elementAt(i).local_scores[k].CR_score);             
                }
                Answer_clair.addElement(temp);
                i++;
            }
           
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    
    public void encode_using_Blowfish(MyBlowfish blf){
       
        for(int i=0;i<numColumns;i++){
           for (int j=0;j<numRows;j++){
                double plaintext=db.lists[i].elements[j].score;
               byte [] ciphertext=blf.crypt(plaintext);
               db1.listsCR[i].elementsCR[j].score=Arrays.copyOf(ciphertext, ciphertext.length);
        }
    }

}
    
  
       
    public void baquetization_equal_packets(int size,double z,int y) {
        int last_elem;
        double pid;
        double score_supr;
        
        for(int i=0;i<numColumns;i++ ){
            double x1;
            double x2;
            double[] tab;
            if(numRows%size==0){
                tab=new double[(numRows/size)*2];
            }else{
                tab=new double[((numRows/size)+1)*2];
            } 
           
            int f=size;
            int l=0;
                    while(l < tab.length) {
                        if(l==0){
                            
                            tab[l]=db.lists[i].elements[0].score+Math.random()*((db.lists[i].elements[0].score+100)-db.lists[i].elements[0].score);
                            l++;
                        }else{
                            if(l>0&&l<tab.length-1){
                            x1=db.lists[i].elements[f].score+Math.random()*(db.lists[i].elements[f-1].score-db.lists[i].elements[f].score);
                            x2=db.lists[i].elements[f].score+Math.random()*(db.lists[i].elements[f-1].score-db.lists[i].elements[f].score);
                            if(x1>x2){
                                tab[l]=x1;
                                tab[l+1]=x2;
                            }else{
                                tab[l]=x2;
                                tab[l+1]=x1;
                                }
                                l=l+2;
                                f=f+size;
                                }else{
                                    if(l==tab.length-1){
                                        tab[l]=Math.random()*(db.lists[i].elements[numRows-1].score);
                                        l=l+1;
                                    }
                                }
                            }
                        }
                    l=0;
            for (int j=0;j<numRows;j=j+size){
                 score_supr=tab[l]*z+y;
                l++;
                last_elem=j+size;
                if(last_elem>numRows){
                    last_elem=numRows;
                }
                pid=tab[l]*z+y;
               
                l++;
                
                for(int k=j;k< last_elem;k++){
                    int x;
                    if(last_elem==numRows){
                        x=k+(int)(Math.random()*( (j+(numRows-j)-1)-k));
                    }else{
                        x=k+(int)(Math.random()*( (j+size-1)-k));
                    }
                    ListElement_EncryptedDB temp=db1.listsCR[i].elementsCR[k];
                    db1.listsCR[i].elementsCR[k]=db1.listsCR[i].elementsCR[x];
                    db1.listsCR[i].elementsCR[x]=temp;
                    db1.listsCR[i].elementsCR[k].PID=pid;
                    if(i==0){
                     positions.put(db1.listsCR[i].elementsCR[k].dataID, new int[numColumns]);
                     score_sup.put(db1.listsCR[i].elementsCR[k].dataID, new double[numColumns]);
                    }
                    positions.get(db1.listsCR[i].elementsCR[k].dataID)[i]=k;
                    score_sup.get(db1.listsCR[i].elementsCR[k].dataID)[i]=score_supr;
                }
                if(j<numRows-size){
                    score_supr=Math.random()*(db1.listsCR[i].elementsCR[last_elem-1].PID-db.lists[i].elements[last_elem].score)*z-y;
                }
            }
            tab=null;
        }
        
    }
    
  
    private double computeOverallScore (double [] localScores)
  {
    
   double overallScore =0;
for (int k=0; k<numColumns; ++k)
      overallScore = overallScore + localScores[k];
	return overallScore;
}
   private double computeNewOverallScore (double [] localScores)
  {
      double overallScore =0;
       overallScore = localScores[0]+localScores[1]+localScores[2]+localScores[3]*3600+localScores[4];/*60+localScores[5];*/
	return overallScore;
  }
  
    private boolean exist_in_Answers(String data_id){
      boolean exist=false;
      int ss=0;
      while((exist==false)&&(ss<Answers.size())){
          if(Answers.elementAt(ss).dataID.equals(data_id))
              exist=true;
          else
              ss++;
      }
      return exist;
          
  }
    

    
    
   public void printDatabase() {
   int i, j;
   double temp;

   String dataID;
   for (j=0; j<numRows; ++j) {
     System.out.print((j+1) + "  -> " );
     for (i=0; i<numColumns; ++i){
       temp = Math.round ((db.lists[i].elements[j].score) * 1000);
       temp = temp / 1000;
       dataID = db.lists[i].elements[j].dataID;
       System.out.print (dataID + "   :   " + temp + "        "+   db1.listsCR[i].elementsCR[j].PID+"   ");
     }

     System.out.println();
   }
 }
   
   
}
