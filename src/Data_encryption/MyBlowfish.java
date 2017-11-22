package data_encryption;


import java.io.*;
import java.math.*;
import java.nio.ByteBuffer;
import java.util.*;
import javax.crypto.*;
import java.security.*;
import javax.crypto.spec.*;
import java.security.interfaces.*;


public class MyBlowfish {
  public final static int KEY_SIZE = 128;  // [32..448]

  private Key secretKey;
  
  
  public MyBlowfish() {
  }

 
  public Key getSecretKey() {
    return secretKey;
  }
 
  public byte[] getSecretKeyInBytes() {
    return secretKey.getEncoded();
  }
  
    
  public void setSecretKey(Key secretKey) {
    this.secretKey = secretKey;
  }
  
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

