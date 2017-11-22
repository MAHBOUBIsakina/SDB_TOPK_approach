package data_encryption;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//import propo1_pqt_tll_eg_vector2.*;
//import propo1_pqt_tll_eg_vector.*;
//import proposition1_equal_size_packets.*;
//import proposition_1_quicksort_optimise.*;
//import proposition_1_optimisee.*;
//import proposition_1_hash_function_byte.*;
import java.io.*;
//import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author smahboub
 */
public class FirstProposition {
    int numColumns;
    int numRows;
    
    public Database db;
    public EncryptedDatabase db1;
    //AnswerElementCR [] answer;
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
       /* Answers=new AnswerElementCR[numRows];
        for (int i=0;i<numRows;i++){
            Answers[i]=new AnswerElementCR();
            Answers[i].dataID="";
            Answers[i].local_scores=new CryptedScore[numColumns];
            for (int j=0;j<Answers[i].local_scores.length;j++){
                Answers[i].local_scores[j]= new CryptedScore();
                
            }
        }*/
      /*  Answer_clair=new AnswerElement[numRows];
        for (int i=0;i<numRows;i++){
            Answer_clair[i]=new AnswerElement();
            Answer_clair[i].dataID="";
            Answer_clair[i].PeriScoreinf=0;
            Answer_clair[i].periscoresup=0;
            Answer_clair[i].local_scores=new double[numColumns];
            for (int j=0;j<numColumns;j++){
                Answer_clair[i].local_scores[j]= 0;
            }
        
    }*/}
    public FirstProposition(int numC, int numR, int max_values_score) throws IOException {
        numColumns=numC;
        numRows=numR;
        //size_intrv=size;
        db= new Database(numColumns, numRows,size_intrv);
        //db1=new EncryptedDatabase(numColumns, numRows,max_values_score,size);
        Answers=new Vector<tempon_ansewer_ele>();
        answers_client=new  Vector<AnswerElementCR> (numColumns);
        AnswersTA=new  Vector<AnswerElementCR> (numColumns);
        Answer_clair= new Vector<AnswerElement> (numColumns);
       /* Answers=new AnswerElementCR[numRows];
        for (int i=0;i<numRows;i++){
            Answers[i]=new AnswerElementCR();
            Answers[i].dataID="";
            Answers[i].local_scores=new CryptedScore[numColumns];
            for (int j=0;j<Answers[i].local_scores.length;j++){
                Answers[i].local_scores[j]= new CryptedScore();
                
            }
        }*/
      /*  Answer_clair=new AnswerElement[numRows];
        for (int i=0;i<numRows;i++){
            Answer_clair[i]=new AnswerElement();
            Answer_clair[i].dataID="";
            Answer_clair[i].PeriScoreinf=0;
            Answer_clair[i].periscoresup=0;
            Answer_clair[i].local_scores=new double[numColumns];
            for (int j=0;j<numColumns;j++){
                Answer_clair[i].local_scores[j]= 0;
            }
        
    }*/}
    
   
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
                //System.out.print("         "+db.lists[i].elements[j].dataID);
                db1.listsCR[i].elementsCR[j].dataID=new sun.misc.BASE64Encoder().encode(ciphertext);
                //("         "+db1.listsCR[i].elementsCR[j].dataID);

                }
        }
        
//       for (int i=0;i<numRows;i++){
//           //System.out.println(db1.listsCR[0].elementsCR[i].dataID+"      "+db.lists[0].elements[i].dataID);
//           db1.positions[i].dataid=db1.listsCR[0].elementsCR[i].dataID;
//       }
       
        
        
       
    }catch(UnsupportedEncodingException e){
        System.out.println("erreur");
        }
    }
    
    
   
    
    
    
    
    
    
    public void decode_DB(byte [] secret_key,MyBlowfish blf){
        //int [] position;
        Vector<ListElement> Answer_claire1 = new Vector<ListElement>();
        
        try{
            int i=0;
            while (i<answers_client.size()){
                AnswerElement temp = new AnswerElement(numColumns);
                int spos = 0;
                byte [] ciphertext=(new sun.misc.BASE64Decoder().decodeBuffer(answers_client.elementAt(i).dataID));
                //System.out.println("l'element chiffré est "+answers_client.elementAt(i).dataID);
                byte [] ciphertext1=new byte [ciphertext.length];
                for (int pos = 0; pos < ciphertext.length; ++pos) {
                    //System.out.println(" spos="+spos+"pos="+pos);
                    ciphertext1[pos] = (byte) (ciphertext[pos] ^ secret_key[spos]);
                    ++spos;
                    
                    if (spos >= secret_key.length) {
                        spos = 0;
                    }
                }
                temp.dataID=new String(ciphertext1, "UTF-8");
                // System.out.println("l'element aprée le déchiffrement est  "+temp.dataID);
                //temp.PeriScoreinf=answers_client.elementAt(i).PeriScoreinf;
                //temp.periscoresup=answers_client.elementAt(i).periscoresup;
                for(int k=0;k<numColumns;k++){
                       //System.out.println("-----------------------------"+Arrays.toString(Answers[j].local_scores[i].CR_score)+"       "+j);
                    temp.local_scores[k]=blf.decryptInDouble(answers_client.elementAt(i).local_scores[k].CR_score);             
                }
                Answer_clair.addElement(temp);
                //System.out.println(temp.dataID+ "  "+ Arrays.toString(temp.local_scores));
                i++;
            }
            
            
//             i=0;
//            while (i<Answers.size()){
//                ListElement temp = new ListElement();
//                int spos = 0;
//                byte [] ciphertext=(new sun.misc.BASE64Decoder().decodeBuffer(Answers.elementAt(i).dataID));
//                //System.out.println("l'element chiffré est "+answers_client.elementAt(i).dataID);
//                byte [] ciphertext1=new byte [ciphertext.length];
//                for (int pos = 0; pos < ciphertext.length; ++pos) {
//                    //System.out.println(" spos="+spos+"pos="+pos);
//                    ciphertext1[pos] = (byte) (ciphertext[pos] ^ secret_key[spos]);
//                    ++spos;
//                    
//                    if (spos >= secret_key.length) {
//                        spos = 0;
//                    }
//                }
//                temp.dataID=new String(ciphertext1, "UTF-8");
//                // System.out.println("l'element aprée le déchiffrement est  "+temp.dataID);
//                //temp.PeriScoreinf=answers_client.elementAt(i).PeriScoreinf;
//                //temp.periscoresup=answers_client.elementAt(i).periscoresup;
//                int [] posi=db.positions_elem.get(temp.dataID);
//                double[] scores=new double[numColumns];
//                for(int k=0;k<numColumns;k++){
//                       //System.out.println("-----------------------------"+Arrays.toString(Answers[j].local_scores[i].CR_score)+"       "+j);
//                    scores[k]=(db.lists[k].elements[posi[k]].score);             
//                }
//                temp.score=computeNewOverallScore(scores);
//                Answer_claire1.addElement(temp);
//                //System.out.println(temp.dataID+ "  "+ Arrays.toString(temp.local_scores));
//                i++;
//            }
//            
//            System.out.println(" le tableau des k element est");
//            for (int j = 0; j < Answer_claire1.size(); j++) {
//                System.out.println( Answer_claire1.elementAt(j).dataID+"    "+Answer_claire1.elementAt(j).score);
//            }
//            System.out.println("");
            
            
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
    
	     		
    
    
    
    
    
    public void encode_using_Blowfish(MyBlowfish blf){
        
        
       
        for(int i=0;i<numColumns;i++){
           for (int j=0;j<numRows;j++){
                double plaintext=db.lists[i].elements[j].score;
               byte [] ciphertext=blf.crypt(plaintext);
               
               //int temp=ByteBuffer.wrap(ciphertext).getInt();
                       //new String (ciphertext);
               //result =new int [temp.length()];
               //for ( int k = 0; k < temp.length(); k++ ){
                 //   char c = temp.charAt(i);
                   // result [k]= (int) c; 
               //}
            
                db1.listsCR[i].elementsCR[j].score=Arrays.copyOf(ciphertext, ciphertext.length);
        }
    }

}
    
   /* public void decode_using_Blofish(MyBlowfish blf){

        //System.out.println(" _________________"+Arrays.toString(Answers[0].local_scores[0].CR_score));
        //for(int i=0;i<numColumns;i++){
           for (int j=0;j<Answers.size();j++){
                if (Answers.elementAt(j).dataID.length()!=0){
                  //System.out.println("-----------------------------"+Arrays.toString(Answers[j].local_scores[i].CR_score));
                   //System.out.println("-----------------------------"+Arrays.toString(Answer_clair[j].local_scores));
                   //for(int i=0;i<numColumns;i++){
                      // System.out.println("-----------------------------"+Arrays.toString(Answers[j].local_scores[i].CR_score));
                    //Answer_clair[j].local_scores[i]=blf.decryptInDouble(Answers[j].local_scores[i].CR_score);
                  //}
                  for(int i=0;i<numColumns;i++){
                       //System.out.println("-----------------------------"+Arrays.toString(Answers[j].local_scores[i].CR_score)+"       "+j);
                    Answer_clair[j].local_scores[i]=blf.decryptInDouble(Answers.elementAt(j).local_scores[i].CR_score);
                  }
                 
                Answer_clair[j].PeriScoreinf=Answers.elementAt(j).PeriScoreinf;
                Answer_clair[j].periscoresup=Answers.elementAt(j).periscoresup;
               //int temp=ByteBuffer.wrap(ciphertext).getInt();
                       //new String (ciphertext);
               //result =new int [temp.length()];
               //for ( int k = 0; k < temp.length(); k++ ){
                 //   char c = temp.charAt(i);
                   // result [k]= (int) c; 
               //}
          // }
                }}//}
              /* System.out.println("le tableau des Answers après le déchiffrement est");
        for (int k=0;k<numRows;k++){
           
       
                System.out.print("  "+Answer_clair[k].dataID+"     "+Answer_clair[k].PeriScoreinf+ "      "+Answer_clair[k].periscoresup);
                for (int h=0;h<numColumns;h++){
                    System.out.print("     "+(Answer_clair[k].local_scores[h]));
            }
            System.out.println();
            
                
        }*/
  //  }
       
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
            System.out.println("hhhhhhhhhhhhhhhhhhhh tab size "+tab.length);
            int f=size;
            int l=0;
                    while(l < tab.length) {
                        if(l==0){
                            //System.out.println(" vvvvvvvvvv"+db.lists[i].elements[0].score);
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
                    //System.out.println("le tableau des valeurs est "+Arrays.toString(tab));
                    
                    
            //score_sup.put(db1.listsCR[i].elementsCR[0].dataID, new double[numColumns]);
//            score_supr=(tab)*z-y;
//            db1.listsCR[i].elementsCR[numRows-1].PID=(Math.random()*(db.lists[i].elements[numRows-1].score))*z-y;
            l=0;
            for (int j=0;j<numRows;j=j+size){
                //System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuuj="+j);
                score_supr=tab[l]*z+y;
                l++;
                last_elem=j+size;
                if(last_elem>numRows){
                    last_elem=numRows;
                }
                pid=tab[l]*z+y;
               
                l++;
                System.out.println("hhhhhhhhhhhhhhhhhhhh l"+l+"j="+j);
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
    
   /* public void get_result_algorithm(int k_elements){
        
        double [] seenDatalocalScores = new double [numColumns];
        double [] seenDatafilteringScore=new double [numColumns];
        double [] currentPositionScores = new double [numColumns];
        top_k_Answers=new AnswerElementCR[k_elements];
        for (int k=0;k<k_elements;k++){
            top_k_Answers[k]=new AnswerElementCR();
            top_k_Answers[k].dataID="";
            top_k_Answers[k].PeriScoreinf=-1;
            top_k_Answers[k].periscoresup=-1;
        }
        
      
        double min_périscore=0;
        double seenDataPeriScore;
        double filteringvalue;
        
        
        String seenDataID;
        double threshold;
        
        for (int d=0;d<10;d++){
            for (int r=0;r<numColumns;r++){
                System.out.println("le paquet"+d+"dans la liste"+r+"est dans la position entre"+db1.inPaquet[d].position_borne_inf[r]+" est "+db1.inPaquet[d].position_borne_sup[r]);
            }
        }
        
        
        boolean stop = false;
    int j=0;int s=0;
    while ((stop == false) && (j < numRows))
    {
      for (int i=0; i<numColumns; ++i)
      {
          
        currentPositionScores[i] = db1.listsCR[i].elementsCR[j].PID;
        seenDataID = db1.listsCR[i].elementsCR[j].dataID;
        int u =0;
        boolean find=false;
        while (u<numRows&&find==false){
            if(seenDataID.equals(db1.positions[u].dataid)){
                find=true;
            }else{
                u++;
            }
        }
        //System.out.println("l'element est trouve"+find+"dans la position"+u);
        
        for (int g=0;g<db1.positions[u].position_in_lists.length;g++){
            
                seenDatalocalScores[g] =db1.listsCR[g].elementsCR[db1.positions[u].position_in_lists[g]].PID;
                seenDatafilteringScore[g]=db1.listsCR[g].elementsCR[db1.positions[u].position_in_lists[g]].PID+10;
        }
        seenDataPeriScore = computeOverallScore (seenDatalocalScores);
        //System.out.println("l'élément liste "+i+" colone"+j+"a les score suivant");
        //for(int q=0;q<numColumns;q++){
          //  System.out.print(seenDatalocalScores[q]+"    ");
        //}
        //System.out.println("le periscore de l'élément liste "+i+" colone"+j+"est "+seenDataPeriScore);
        
        
        filteringvalue= computeOverallScore (seenDatafilteringScore);
        AnswerElementCR temp = new AnswerElementCR();
        temp.dataID = seenDataID;
        temp.PeriScoreinf = seenDataPeriScore;
        temp.periscoresup = filteringvalue;
        
        
        if(Answers.contains(temp)==false){
          Answers.add(temp);
          s++;
        
        if (seenDataPeriScore > min_périscore)
        {
          
          double min=top_k_Answers[0].PeriScoreinf;
          int minIndex = 0;
          for (int h=1; h<k_elements; ++h)
            {
             if (top_k_Answers[h].PeriScoreinf < min) {
                min = top_k_Answers[h].PeriScoreinf;
                minIndex = h;
                }
            }
          //System.out.println("le min dans top_k_Answers est "+min+"et minindex="+minIndex);
            if (seenDataPeriScore > min)
             {
                 top_k_Answers[minIndex].dataID = seenDataID;
                 top_k_Answers[minIndex].PeriScoreinf = seenDataPeriScore;
                 top_k_Answers[minIndex].periscoresup = filteringvalue;
                 //System.out.println("top_k_Answers");
                }
            min=top_k_Answers[0].PeriScoreinf;
            for (int h=1; h<k_elements; ++h)
            {
             if (top_k_Answers[h].PeriScoreinf < min) {
                min = top_k_Answers[h].PeriScoreinf;
                
                }
            }
             //System.out.println();
               //     for (int w=0;w<k_elements;w++){
               //         System.out.print("top_k_Answers["+w+"].PeriScoreinf"+top_k_Answers[w].PeriScoreinf+"       ");
              //      }
               //     System.out.println();
            
          
         min_périscore = min;
          
        }
        }
      
        
    }
      
      
      
      
      //System.out.println();
          //for (int f=0;f<k_elements;f++){
            //  System.out.print(top_k_Answers[f].dataID+"   "+top_k_Answers[f].PeriScoreinf);
         // }
      threshold = computeOverallScore (currentPositionScores);
   // System.out.println("le threshold ="+threshold);
      
      
      if (threshold <=min_périscore)
        stop = true;
      ++j;
    }
    //System.out.println("stop position ="+j);
    //for (int k=0;k<numRows;k++){
       // System.out.println("  "+Answers[k].dataID+"      "+Answers[k].PeriScoreinf+ "      "+Answers[k].periscoresup);
    //}
    
    
    for(int i=0;i<numRows;i++){
        boolean eleminer=true;
        j=0;
        while(j<k_elements&&eleminer==true){
             //System.out.println("periscoresup"+Answers[i].periscoresup+"   PeriScoreinf "+top_k_Answers[j].PeriScoreinf);
    
            if(Answers.elementAt(i).periscoresup>=top_k_Answers[j].PeriScoreinf){
                eleminer=false;
            }else{
                j++;
            }
        }
        if(eleminer==true){
            Answers.remove(i);           
        }
    }
  //   System.out.println("les reponses aprés l'élimination des elements");
  //  for (int k=0;k<numRows;k++){
      //  System.out.println("  "+Answers[k].dataID);
   // }
    

    }*/
    private double computeOverallScore (double [] localScores)
  {
    
   double overallScore =0;
//    for (int k=0; k<numColumns; ++k)
//      overallScore = overallScore + Math.pow(2,(k+1))*localScores [k];
//
//    return overallScore;
//    for (int k=0; k<numColumns; ++k)
//      overallScore = overallScore +(k+1)*localScores[k];
//
//    return overallScore;
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
    

    public void get_result_algorithm_by_packet(int k_elements,int size){
        long begintime=System.currentTimeMillis();
        Map<String,Integer> exist_in_answers = new HashMap<String,Integer>();
        double [] seenDatalocalScores = new double [numColumns];
        double [] currentPositionScores = new double [numColumns];
        int last_position=0;
        double min_periscore=0;
        double seenDataPeriScore;
        //double filteringvalue;
        String seenDataID;
        double threshold;
         int minIndex = 0;
         double min;
         int count=0;
        top_k_Answers=new top_k_answer[k_elements];
        for (int k=0;k<k_elements;k++){
            top_k_Answers[k]=new top_k_answer();
            top_k_Answers[k].dataID="";
            top_k_Answers[k].PeriScoreinf=-1;
        }
        boolean stop1=false;
        boolean stop2=false;
        int j;
        int i=0;
        int limit;
        while((stop1==false)&&(stop2==false)){
            for (j=0;j<numColumns;j++){
                currentPositionScores[j]=db1.listsCR[j].elementsCR[last_position+size-1].PID;
//                if(j==0){
//                    System.out.println("vvvvvvvvvvvvvvvvvvvvvv"+Arrays.toString(currentPositionScores));
//                }
                limit=last_position+size;
                for (i=last_position;i<limit;i++){
                    seenDataID = db1.listsCR[j].elementsCR[i].dataID;
                    if(exist_in_answers.get(seenDataID)==null){
                        count=count+1;
                        AnswerElementCR temp = new AnswerElementCR(numColumns);
                        int [] posi=positions.get(seenDataID);
                         //System.out.println("posi  "+seenDataID+"   "+Arrays.toString(posi));
                        //int p=posi.length;
                        for (int g=0;g<numColumns;g++){
                            seenDatalocalScores[g] =db1.listsCR[g].elementsCR[posi[g]].PID;
                            int s=db1.listsCR[g].elementsCR[posi[g]].score.length;
                        temp.local_scores[g]= new CryptedScore(s);
                            
                        for (int l = 0; l < s; l++) {
                            temp.local_scores[g].CR_score[l]=db1.listsCR[g].elementsCR[posi[g]].score[l];
                        }
                        }
                        seenDataPeriScore = computeOverallScore (seenDatalocalScores);
                        
                        temp.dataID = seenDataID;
                        
                        answers_client.add(temp);
                        exist_in_answers.put(seenDataID, 1);
                        if (seenDataPeriScore > min_periscore){
                            top_k_Answers[minIndex].dataID = seenDataID;
                            top_k_Answers[minIndex].PeriScoreinf = seenDataPeriScore;
                            min=top_k_Answers[0].PeriScoreinf;
                            minIndex = 0;
                            for (int h=1; h<k_elements; ++h){
                                if (top_k_Answers[h].PeriScoreinf < min) {
                                    min = top_k_Answers[h].PeriScoreinf;
                                    minIndex = h;
                                }
                            }
                            min_periscore = min;
                        }
                    }
                    
                }
            }
            last_position=last_position+size;
            threshold = computeOverallScore (currentPositionScores);
            //System.out.println("le th ="+threshold+"le min = "+min_periscore);
            if(threshold<=min_periscore){
                stop1=true;
            }
            if(last_position>=numRows){
                stop2=true;
            }
        }
        
        long endtime=System.currentTimeMillis();
        System.out.println("le temps du proposition1 est "+(endtime-begintime));
        System.out.println("la stop position est "+(i)+"count="+count);
        long x=System.currentTimeMillis();
        j = answers_client.size();
        System.out.println("la taille des réponses est "+j);
//        for( i=0;i<j;i++){
//            
//            if(Answers.elementAt(i).periscoresup>=min_periscore){
//                //System.out.println("le  periscoresup= "+Answers.elementAt(i).periscoresup+"min_periscore =  "+min_periscore);
//                AnswerElementCR temp = new AnswerElementCR(numColumns);
//                temp.dataID=Answers.elementAt(i).dataID;
//                //temp.PeriScoreinf=Answers.elementAt(i).PeriScoreinf;
//                //temp.periscoresup=Answers.elementAt(i).periscoresup;
//                int [] posi=positions.get(Answers.elementAt(i).dataID);
//                    for (int k = 0; k < numColumns; k++) {
//                        int s=db1.listsCR[k].elementsCR[posi[k]].score.length;
//                        temp.local_scores[k]= new CryptedScore(s);
//                            
//                        for (int l = 0; l < s; l++) {
//                            temp.local_scores[k].CR_score[l]=db1.listsCR[k].elementsCR[posi[k]].score[l];
//                        }
//                    }
//                answers_client.add(temp);
//                
//            }
//        }
        
//        long m=System.currentTimeMillis();
//        System.out.println("le temps d'elimination des éléments est "+(m-x)+"la taille des réponses aprés le filtrage est"+answers_client.size());
//      
    }
    
    
    
    
    public void copy_array(byte [] tab1,byte [] tab2){
        for(int m=0;m<tab1.length;m++){
            tab1[m]=tab2[m];
        }
    }
   
  public void clone(  CryptedScore[] tab2, CryptedScore[] tab){
           
         for (int i = 0; i < tab.length; i++) {
             for (int j = 0; j < tab[i].CR_score.length; j++) {
                 tab2[i].CR_score[j]=tab[i].CR_score[j];
             }
            
        }
        
         
    }
  
  public void top_k_elem_extraction(ListElement [] final_result, int k){
        double global_score;
        double min;
        int index_min;
        double min_glb_scr=0;
      
        for (int i = 0; i <Answer_clair.size(); i++) {
            //System.out.println(Answer_clair.elementAt(i).dataID+"  "+Arrays.toString(Answer_clair.elementAt(i).local_scores));
            global_score=computeOverallScore(Answer_clair.elementAt(i).local_scores);
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
                    final_result[index_min].dataID=Answer_clair.elementAt(i).dataID;
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
        
        
    }
  
  
  
  
  public void TA (int k_elements){
      
        Map<String,Integer> exist_in_answersTA = new HashMap<String,Integer>();
        double [] seenDatalocalScores = new double [numColumns];
        double [] currentPositionScores = new double [numColumns];
        double min_periscore=0;
        double seenDataOverallScore;
        String seenDataID;
        double threshold;
        int minIndex = 0;
        double min;
        int count1=0,count2=0;
        int dataPosition = 0;
        top_k_AnswersTA=new top_k_answer[k_elements];
        for (int k=0;k<k_elements;k++){
            top_k_AnswersTA[k]=new top_k_answer();
            top_k_AnswersTA[k].dataID="";
            top_k_AnswersTA[k].PeriScoreinf=-1;
        }
        boolean stop=false;
        int j=0;
        int i;
        while((stop==false)&&(j<numRows)){
            for (i=0;i<numColumns;i++){
                currentPositionScores[i]=db.lists[i].elements[j].score;
                seenDataID = db.lists[i].elements[j].dataID;
                int [] posi=db.positions_elem.get(seenDataID);
                for (int g=0;g<numColumns;g++){
                    dataPosition =posi[g];
                    seenDatalocalScores[g] =db.lists[g].elements[dataPosition].score;
                }
                seenDataOverallScore = computeOverallScore (seenDatalocalScores);
                count1=count1+1;
                 //System.out.println("posi  "+seenDataID+"   "+Arrays.toString(posi));
                //System.out.println(seenDataID+"   "+Arrays.toString(seenDatalocalScores));
                if(exist_in_answersTA.get(seenDataID)==null){
                    count2=count2+1;
                    if (seenDataOverallScore > min_periscore){
                        top_k_AnswersTA[minIndex].dataID = seenDataID;
                        top_k_AnswersTA[minIndex].PeriScoreinf = seenDataOverallScore;
                        min=top_k_AnswersTA[0].PeriScoreinf;
                        minIndex = 0;
                        for (int h=1; h<k_elements; ++h){
                            if (top_k_AnswersTA[h].PeriScoreinf < min) {
                                min = top_k_AnswersTA[h].PeriScoreinf;
                                minIndex = h;
                            }
                        }
                        min_periscore = min;
                          
                    }
                    exist_in_answersTA.put(seenDataID, 1);
                
                }
            }
//            System.out.println("le tableau des top_k elements es ");
//            for (int k = 0; k < k_elements; k++) {
//                System.out.println(top_k_AnswersTA[k].dataID+"   a un  score de "+top_k_AnswersTA[k].PeriScoreinf);
//            }
//            
            threshold = computeOverallScore (currentPositionScores);
//            System.out.println("le th ="+threshold+"  min = "+ min_periscore   );
            if(threshold<=min_periscore){
                stop=true;
            }
            j++;
        }
        System.out.println("stop position ="+j+"      "+"count="+count1);
  }
  
   private double updateAnswers (top_k_answer [] answers, int k, top_k_answer potentialAnswer)
  {
    int i;
    double minAnswerOverallScore = answers[0].PeriScoreinf;
    int minIndex = 0;
    for (i=1; i<k; ++i)
    {
      if (answers[i].PeriScoreinf < minAnswerOverallScore) {
        minAnswerOverallScore = answers[i].PeriScoreinf;
        minIndex = i;
      }
    }
    
    for (i=0; i<k; ++i)
    {
    	if (answers[i].dataID == potentialAnswer.dataID)
            return minAnswerOverallScore; //data item is yet in the list	
    }

    if (potentialAnswer.PeriScoreinf > minAnswerOverallScore)
    {
      answers[minIndex] = potentialAnswer;
    }

/*    minAnswerOverallScore = answers[0].overallScore;
    minIndex = 0;
    for (i=1; i<k; ++i)
      if (answers[i].overallScore < minAnswerOverallScore)
      {
        minAnswerOverallScore = answers[i].overallScore;
        minIndex = i;
      }*/
    return minAnswerOverallScore;
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
