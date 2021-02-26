package com.blackstrawai.common;

import org.springframework.web.bind.annotation.RequestMapping;

import com.blackstrawai.ApplicationRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/decifer")
public abstract class BaseController {
	
	public BaseResponse constructResponse(BaseResponse baseResponse,String status,String description,String message,String code)
	{
		baseResponse.setResponseStatus(status);
		baseResponse.setResponseDescription(description);
		baseResponse.setResponseCode(code);
		baseResponse.setResponseMessage(message);
		//baseResponse.setStatus(baseResponse.getResponseStatus());
		return baseResponse;
	}
	
	public String generateRequestAndResponseLogPaylod(Object o)throws ApplicationRuntimeException{
		try {
			ObjectMapper Obj = new ObjectMapper();
			String jsonStr;		
			jsonStr = Obj.writeValueAsString(o);
			return jsonStr;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new ApplicationRuntimeException(e);
		}		
	}
	
	public  void setTokens(BaseResponse response,String keyToken, String valueToken){
		response.setKeyToken(keyToken);
		response.setValueToken(valueToken);
		
	}

}