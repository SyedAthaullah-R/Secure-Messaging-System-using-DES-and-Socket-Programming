package com.securechat.JAVA_DES;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DES {
    Cipher cipher;
    public String keyToKeyString(SecretKey secKey){
        return Base64.getEncoder().encodeToString(secKey.getEncoded());
    }
    public SecretKey keyStringToKey(String keyString){
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
    }
    public SecretKey generateKey() throws Exception{
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        SecretKey secKey = keyGen.generateKey();
        return secKey;
    }
    public String encrypt(String plainText, SecretKey secKey) throws Exception{
        cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] encryptedByte = cipher.doFinal(plainText.getBytes());
        String encryptedString = Base64.getEncoder().encodeToString(encryptedByte);
        return encryptedString;
    }
    public String decrypt(String encryptedString, SecretKey secKey)throws Exception{
        cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] decryptedByte = cipher.doFinal(Base64.getDecoder().decode(encryptedString));
        String decryptedString = new String(decryptedByte);
        return decryptedString;
    }
}
