package com.blackstrawai.common;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.blackstrawai.ApplicationException;

public abstract class BaseService {
	
	
	public String generateHashToken(String valueToken)throws ApplicationException{
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(valueToken.getBytes(StandardCharsets.UTF_8));
			byte[] digest = messageDigest.digest();
			String hex = String.format("%064x", new BigInteger(1, digest));
			return hex;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		
	}

}
