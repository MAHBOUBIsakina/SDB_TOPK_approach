package data_encryption;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



//import proposition_1_quicksort_optimise.*;
//import proposition_1_optimisee.*;
//import proposition_1_hash_function_byte.*;
//import propo1_pqt_tll_eg_vector2.*;
//import propo1_pqt_tll_eg_vector.*;
//import proposition1_equal_size_packets.*;
//import test.*;
import java.io.*;
import java.math.*;
import java.nio.ByteBuffer;
import java.util.*;
import javax.crypto.*;
import java.security.*;
import javax.crypto.spec.*;
import java.security.interfaces.*;


/** 
 * Cette classe propose des méthodes permettant de crypter et décrypter des 
 * messages avec l'algorithme de Blowfish.
 */
public class MyBlowfish {
  public final static int KEY_SIZE = 128;  // [32..448]

  private Key secretKey;
  
  
  public MyBlowfish() {
  }

 
  public Key getSecretKey() {
    return secretKey;
  }
  
  
  /**
   * Retourne toutes les informations de la clé sous forme d'un tableau de
   * bytes. Elle peut ainsi être stockée puis reconstruite ultérieurement en
   * utilisant la méthode setSecretKey(byte[] keyData)
   */
  public byte[] getSecretKeyInBytes() {
    return secretKey.getEncoded();
  }
  
    
  public void setSecretKey(Key secretKey) {
    this.secretKey = secretKey;
  }
  
  
  /**
   * Permet de reconstruire la clé secrète à partir de ses données, stockées 
   * dans un tableau de bytes.
   */
  public void setSecretKey(byte[] keyData) {
    secretKey = new SecretKeySpec(keyData, "Blowfish");    
  }
  
  
  public void generateKey() {
    try {
      KeyGenerator keyGen = KeyGenerator.getInstance("Blowfish");
      keyGen.init(KEY_SIZE);
      secretKey = keyGen.generateKey();  
    }
    catch (Exception e) {System.out.println(e);} 
  }


  public byte[] crypt(byte[] plaintext) {
    try {
      Cipher cipher = Cipher.getInstance("Blowfish");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return cipher.doFinal(plaintext);    
    }
    catch (Exception e) {System.out.println(e);} 
    return null;
  }

  
  public byte[] crypt(double plaintext) {
      byte[] bytes = new byte[16];
    ByteBuffer.wrap(bytes).putDouble(plaintext);
      //byte [] text=ByteBuffer.allocate(8).putDouble(plaintext).array();
    return crypt(bytes);
  }
  

  public byte[] decryptInBytes(byte[] ciphertext) {
    try {
      Cipher cipher = Cipher.getInstance("Blowfish");
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return cipher.doFinal(ciphertext);
    }
    catch (Exception e) {System.out.println(e);} 
    return null;
  }
  
  
  public double decryptInDouble(byte[] ciphertext) {
      
      try {
      Cipher cipher = Cipher.getInstance("Blowfish");
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return ByteBuffer.wrap(cipher.doFinal(ciphertext)).getDouble();
    }
    catch (Exception e) {System.out.println(e);} 
    return 0;
  }
      
        
        
    
      
  }


  /**
   * Cette méthode permet de tester le bon fonctionnement des autres.
   */
  /*public static void main(String[] args) {
    String plaintext = "comment allez vous";
    System.out.println("plaintext = " + plaintext);
    MyBlowfish bf = new MyBlowfish();
    bf.generateKey();
    byte[] secretKey = bf.getSecretKeyInBytes();
    byte[] ciphertext = bf.crypt(plaintext);   
    System.out.println("ciphertext = " + new BigInteger(ciphertext));

    bf.setSecretKey(secretKey);
    String plaintext2 = bf.decryptInString(ciphertext);
    System.out.println("plaintext2 = " + plaintext2);
    if (!plaintext2.equals(plaintext)) System.out.println("Error: plaintext2 != plaintext");
  }*/
