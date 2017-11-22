package data_encryption;


//import .*;
//import proposition_1_quicksort_optimise.*;
//import proposition_1_optimisee.*;
//import proposition_1_hash_function_byte.*;
//import propo1_pqt_tll_eg_vector2.*;
//import propo1_pqt_tll_eg_vector.*;
//import proposition1_equal_size_packets.*;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author smahboub
 */
public class EncryptedDatabase {
    
    public ListEncryptedDB [] listsCR;
    public int m;
    public int n;
    //public PositionDataItem [] positions;
    public Position_packet [] pos_packet;
    public int value_max_scr;
    public int intrvl_size;
    
    public EncryptedDatabase(int in_m, int in_n,int max_values_score,int size)
  {
    m = in_m;
    n = in_n;
    intrvl_size=size;
    value_max_scr=max_values_score;
    listsCR = new ListEncryptedDB[m];
    int i, j;
    for (i=0; i<m; ++i) {
      listsCR[i] = new ListEncryptedDB();
      listsCR[i].elementsCR = new ListElement_EncryptedDB[n];
      for (j=0; j<n; ++j)
      {
        listsCR[i].elementsCR[j] = new ListElement_EncryptedDB();
      }
    }
//    positions=new PositionDataItem[n];
//
//    for (i=0;i<n;i++){
//        positions[i]= new PositionDataItem();
//        positions[i].dataid= "";
//        positions[i].position_in_lists= new int[m];
//        positions[i].global_score_sup=new double[m];
//        
//    }
    int indice=value_max_scr/size;
    pos_packet=new Position_packet[indice];
    for (i=0;i<indice;i++){
        pos_packet[i]= new Position_packet();
        pos_packet[i].in_list=new int [m];
    }
   
  }
    
     public void printDatabase() {
   int i, j;

   String dataID;
   for (j=0; j<n; ++j) {
     System.out.print((j+1) + "  -> " );
     for (i=0; i<m; ++i){
       dataID = listsCR[i].elementsCR[j].dataID;
       //String s="";
       //for(int k=0;k<listsCR[i].elementsCR[j].score.length;k++){
        //   s= s+listsCR[i].elementsCR[j].score[k];
      //}
       System.out.print (dataID + "  :  " + "    "+listsCR[i].elementsCR[j].PID+"   ");
     }

     System.out.println();
   }
 } 
//      public void get_unordred_list(){
//          
//    for(int i=0;i<m;i++){
//        
//        int j=0;
//        while(j<n){
//            
//            int first_position=j;
//            int k=first_position;
//            
//            int f=0;
//            /*while(k<n && listsCR[i].elementsCR[k].PID==listsCR[i].elementsCR[j].PID){
//                f=f+1;
//                k=k+1;
//                
//            }*/
//            
//            
//            int last_position=k-1;
//            //pos_packet[(int)(listsCR[i].elementsCR[last_position].PID/intrvl_size)].in_list[i]=f;
//            for(k=first_position;k<=last_position;k++){
//                if (k==last_position){
//                    int u =0;
//                    boolean find=false;
//                    while (u<n&&find==false){
//                        if(listsCR[i].elementsCR[k].dataID.equals(positions[u].dataid)){
//                            find=true;
//                        }else{
//                            u++;
//                        }
//                    }
//                    positions[u].position_in_lists[i]=k;
//                }else{
//                    int x=k+(int)(Math.random()*(last_position-k));
//                    ListElement_EncryptedDB temp=listsCR[i].elementsCR[k];
//                    listsCR[i].elementsCR[k]=listsCR[i].elementsCR[x];
//                    listsCR[i].elementsCR[x]=temp;
//                    int u =0;
//                    boolean find=false;
//                    while (u<n&&find==false){
//                        if(listsCR[i].elementsCR[k].dataID.equals(positions[u].dataid)){
//                            find=true;
//                        }else{
//                            u++;
//                        }
//                    }
//                    positions[u].position_in_lists[i]=k;
//                
//                //*first_position=first_position++;
//            }}
//            j=j+f;
//        }
//        
//    } 
//   // for (int i=0;i<m;i++){
//    //System.out.println("les position de l'element"+positions[i].dataid+"dans toutes les listes sont");
//     //   for (int b=0;b<m;b++){
//     //       System.out.print(positions[i].position_in_lists[b]);
//     //   } 
//     //   System.out.println();}
//    //affichage du tableau qui contien les position des paquets dans chaque liste
//    /*for (int i=0;i<10;i++){
//        System.out.print(i+"   ");
//        for (int j=0;j<m;j++){
//            System.out.print(inPaquet[i].liste[j]+"  ");
//        }
//        System.out.println();
//        for (int j=0;j<m;j++){
//            System.out.print("    "+inPaquet[i].position_borne_inf[j]+"  ");
//        }
//        System.out.println();
//        for (int j=0;j<m;j++){
//            System.out.print("    "+inPaquet[i].position_borne_sup[j]+"  ");
//        }
//        System.out.println();
//    }*/
//    //for(int i=0;i<10;i++){
//     //   System.out.println("le paquet"+i*10+"a le nb des élements suivant");
//     //   for(int j=0;j<m;j++){
//      //      System.out.println(pos_packet[i].in_list[j]);
//      //  }
//     //   System.out.println();
//  //  }
//}
}
