package com.blackstrawai;

import java.io.FileReader;
import java.nio.charset.Charset;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class DeciferAES256 {
	
	private static SecretKeyFactory encyrptionComplianceFactory;
	private static SecretKeyFactory encyrptionBankingFactory;	
	private static SecretKeyFactory decyrptionComplianceFactory;
	private static SecretKeyFactory decyrptionBankingFactory;


	
	
	public static String encrypt_compliance(String strToEncrypt,String property) throws ApplicationException {
		try {
			if (strToEncrypt == null || strToEncrypt.length() == 0){
				return strToEncrypt;
			}
			
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			String key = p.getProperty(property);
			String salt = generateSalt(20);
			KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = getEncyrptionComplianceFactory().generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			return salt + Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			throw new ApplicationException("Error while encrypting: " + e.toString());
		}

	}	
	
	public static String encrypt_banking(String strToEncrypt,String property) throws ApplicationException {
		try {
			if (strToEncrypt == null || strToEncrypt.length() == 0){
				return strToEncrypt;
			}
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			String key = p.getProperty(property);
			String salt = generateSalt(20);
			KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = getEncyrptionBankingFactory().generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			return salt + Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			throw new ApplicationException("Error while encrypting: " + e.toString());
		}

	}

	public static String decrypt_compliance(String strToDecrypt,String property) throws ApplicationException {
		try {
			if (strToDecrypt == null || strToDecrypt.length() == 0){
				return strToDecrypt;
			}
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			String key = p.getProperty(property);
			String salt = strToDecrypt.substring(0, 20);
			String password = strToDecrypt.substring(20);
			KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = getDecyrptionComplianceFactory().generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			return new String(cipher.doFinal(Base64.getDecoder().decode(password)));
		} catch (Exception e) {
			throw new ApplicationException("Error while decrypting: " + e.toString());
		}
	}
	
	public static String decrypt_banking(String strToDecrypt,String property) throws ApplicationException {
		try {
			if (strToDecrypt == null || strToDecrypt.length() == 0){
				return strToDecrypt;
			}
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p = new Properties();
			p.load(reader);
			String key = p.getProperty(property);
			String salt = strToDecrypt.substring(0, 20);
			String password = strToDecrypt.substring(20);
			KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = getDecyrptionBankingFactory().generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			return new String(cipher.doFinal(Base64.getDecoder().decode(password)));
		} catch (Exception e) {
			throw new ApplicationException("Error while decrypting: " + e.toString());
		}
	}

	public static SecretKeyFactory getEncyrptionComplianceFactory() {
		return encyrptionComplianceFactory;
	}

	public static void setEncyrptionComplianceFactory(SecretKeyFactory encyrptionComplianceFactory) {
		DeciferAES256.encyrptionComplianceFactory = encyrptionComplianceFactory;
	}

	public static SecretKeyFactory getEncyrptionBankingFactory() {
		return encyrptionBankingFactory;
	}

	public static void setEncyrptionBankingFactory(SecretKeyFactory encyrptionBankingFactory) {
		DeciferAES256.encyrptionBankingFactory = encyrptionBankingFactory;
	}

	public static SecretKeyFactory getDecyrptionComplianceFactory() {
		return decyrptionComplianceFactory;
	}

	public static void setDecyrptionComplianceFactory(SecretKeyFactory decyrptionComplianceFactory) {
		DeciferAES256.decyrptionComplianceFactory = decyrptionComplianceFactory;
	}

	public static SecretKeyFactory getDecyrptionBankingFactory() {
		return decyrptionBankingFactory;
	}

	public static void setDecyrptionBankingFactory(SecretKeyFactory decyrptionBankingFactory) {
		DeciferAES256.decyrptionBankingFactory = decyrptionBankingFactory;
	}

	private static String generateSalt(int n) {
		byte[] array = new byte[256];
		new Random().nextBytes(array);

		String randomString = new String(array, Charset.forName("UTF-8"));

		StringBuffer r = new StringBuffer();
		for (int k = 0; k < randomString.length(); k++) {

			char ch = randomString.charAt(k);

			if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) && (n > 0)) {

				r.append(ch);
				n--;
			}
		}

		return r.toString();
	}

}
