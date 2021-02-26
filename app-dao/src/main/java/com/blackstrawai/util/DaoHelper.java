package com.blackstrawai.util;

import java.io.FileReader;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

public class DaoHelper {
	
	private static DaoHelper daoHelper;
	private Logger logger = Logger.getLogger(DaoHelper.class);

	private static String secretKey;
	private static String salt;
	
	
	private DaoHelper(){
		
	}
	
	public static DaoHelper getInstance(){
		if(daoHelper==null){
			FileReader reader;
			try {
				reader = new FileReader("/decifer/config/app_config.properties");
				Properties p = new Properties();
				p.load(reader);
				secretKey = p.getProperty("encrypt_secret_key");
				salt = p.getProperty("encrypt_salt_key");
			}catch (Exception e) {
				// TODO Auto-generated catch block
				secretKey = null;
				salt = null;
				}
			daoHelper=new DaoHelper();
		}
		return daoHelper;
	}
	
	public String getBankingKeyForEncryption(){
		return null;
	}
	
	public String getBankingKeyForDecryption(){
		return null;
	}
	
	public String getBankingKeyForEncryptionAndDecryption(){
		// it can be in config file or database or AD
		return null;
	}
	
	
	public String encrypt(String strToEncrypt) 
	{
	    try
	    {
	    	if(secretKey!=null && salt!=null) {
	        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	         
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	         
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
	        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	    	}
	    } 
	    catch (Exception e) 
	    {
	    	logger.info("Error while encrypting: " + e);
	    }
	    
	    return null;
	}

	
	public String decrypt(String strToDecrypt) {
	    try
	    {
	    	if(secretKey!=null && salt!=null) {
	        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	         
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	         
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
	        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	    	}
	    } 
	    catch (Exception e) {
	        System.out.println("Error while decrypting: " + e.toString());
	    }
	    return null;
	}
}
