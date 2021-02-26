package com.blackstrawai.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;

@Service
public class CacheService extends BaseService{
	
	private static Map<String,String> tokenMap=new HashMap<String,String>();	
	private static Map<String,String> roMap=new HashMap<String,String>();	
	
	static{
		tokenMap.put("1579675637495", "68f3e48e205d1c0834d7120a85d06cf044a40973fc79ad136173da0cb90b6865");
	}
		
	public void addCinNumber(String cinNumber, String summary)throws ApplicationException{
		roMap.put(cinNumber, summary);
	}
	
	public boolean validateCinNumber(String cinNumber, String summary)throws ApplicationException{
		if(roMap.containsKey(cinNumber)){
			String vaueTokenInMap=roMap.get(cinNumber);
			if(vaueTokenInMap.equals(summary)){
				return  true;
			}
		}
		return false;
	}
	
	public void removeCinNumber(String cinNumber)throws ApplicationException{
		if(roMap.containsKey(cinNumber)){
			roMap.remove(cinNumber);
		}
	}
	
	public String getCinNumber(String cinNumber)throws ApplicationException{
		if(roMap.containsKey(cinNumber)){
			return roMap.get(cinNumber);
		}
		return null;
	}
	
	public void addToken(String keyToken, String valueToken)throws ApplicationException{
		tokenMap.put(keyToken, valueToken);
	}
	
	public boolean validateToken(String keyToken, String valueToken)throws ApplicationException{
		if(tokenMap.containsKey(keyToken)){
			String vaueTokenInMap=tokenMap.get(keyToken);
			if(vaueTokenInMap.equals(valueToken)){
				return  true;
			}
		}
		return false;
	}
	
	public void removeToken(String keyToken)throws ApplicationException{
		if(tokenMap.containsKey(keyToken)){
			tokenMap.remove(keyToken);
		}
	}
	
	public String getToken(String keyToken)throws ApplicationException{
		if(tokenMap.containsKey(keyToken)){
			return tokenMap.get(keyToken);
		}
		return null;
	}
}
