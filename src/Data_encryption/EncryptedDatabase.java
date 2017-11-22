package data_encryption;

import java.util.Arrays;

public class EncryptedDatabase {
    
    public ListEncryptedDB [] listsCR;
    public int m;
    public int n;
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
      System.out.print (dataID + "  :  " + "    "+listsCR[i].elementsCR[j].PID+"   ");
     }

     System.out.println();
   }
 } 

}
