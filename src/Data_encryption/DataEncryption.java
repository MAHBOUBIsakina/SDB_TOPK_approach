
package data_encryption;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import peersim.core.Control;


public class DataEncryption implements Control{
    
     public DataEncryption (String prefix) {
        
    }
     
    
    
    @Override
    public boolean execute() {
        
        
        System.out.println("===========================DataEncryption controller start=====================");
        int max_values_score=1000;
        double a=Math.random()*1000;
        System.out.println("    "+a);
        int e=ieme_nbr_premier(1000);
        System.out.println("    "+e);
        int size=2;
        int k=3;
        int numC=2,numR=6;
        ListElement [] final_result=new ListElement[k];
        for (int i = 0; i < k; i++) {
            final_result[i]=new ListElement();
            final_result[i].dataID="";
            final_result[i].score=0;
        }
        
         
        FirstProposition fp;
         try {
             fp = new FirstProposition(numC,numR,max_values_score,size);
             System.out.println("creation de la base est terminée");
        
        fp.db.sortLists();
        System.out.println("sort fini");
        //fp.db.printDatabase();
        
        String key_string="huyfhksdlkopijuhygtfreazdfdgoizeuydfuyifkiudf";
        byte [] key_xor=key_string.getBytes();
        
        //System.out.println("clé ="+Arrays.toString(key_xor));
        fp.encode_using_xor(key_xor);        System.out.println("chiffrement XOR FINI");
        //System.out.println("hhhhhhhhhhhhhhhhhhh"+j );
        //fp.db1.printDatabase();
        //fp.decode_using_xor(key_xor);
        //fp.db.printDatabase();
        MyBlowfish bf = new MyBlowfish();
        bf.generateKey();
        byte[] secretKey = bf.getSecretKeyInBytes();
        fp.encode_using_Blowfish(bf);
        System.out.println("CHIFFREMENT bLOWFISH fini");
        fp.baquetization_equal_packets(size,a,e);
        System.out.println("baquetization fini");
        
        
        
//        InputStream flux=new FileInputStream("/home/sakina/Desktop/sakina/NetBeansProjects/BuckTop_without_filtering/src/bucktop_without_filtering/generate_rypted_data/crypted_DB.txt"); 
//        InputStreamReader lecture=new InputStreamReader(flux);
//        BufferedReader buff=new BufferedReader(lecture);
        File ff=new File("/home/sakina/Desktop/crypted_DB.txt"); // définir l'arborescence
        ff.createNewFile();
        FileWriter ffw=new FileWriter(ff);
        BufferedWriter bw = new BufferedWriter(ffw);
//        String ligne;
//        String valeur;
            
        for (int j = 0; j < fp.db1.m; j++) {
            for (int i = 0; i < fp.db1.n; i++) {
                int h=0;
                String score="";
                for (int l = 0; l < fp.db1.listsCR[j].elementsCR[i].score.length; l++) {
                    score = score + fp.db1.listsCR[j].elementsCR[i].score[l] + " ";
                    h++;
                    //System.out.println(fp.db1.listsCR[j].elementsCR[i].score[l]);
                }
                //System.out.println();
            String tuple = fp.db1.listsCR[j].elementsCR[i].dataID + "/" + score + "/" + 
                    fp.db1.listsCR[j].elementsCR[i].PID + "/" + fp.score_sup.get(fp.db1.listsCR[j].elementsCR[i].dataID)[j];
            
                
                    System.out.println(tuple);
                    bw.write(tuple);  // écrire une ligne dans le fichier resultat.txt
                    bw.write("\n"); 
                
            
            }
        }
        
        bw.close();
                System.out.println("===========================DataEncryption controller end =====================");

        
         } catch (IOException ex) {
             Logger.getLogger(DataEncryption.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    return false;
    }  
}
