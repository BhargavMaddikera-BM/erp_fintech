package com.blackstrawai.common;

public abstract class BaseResponse {
	
	private String responseCode;
	
	private String responseDescription;
	
	private String responseMessage;
	
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	private String responseStatus;
	
	private String keyToken;
	
	private String valueToken;

	public String getKeyToken() {
		return keyToken;
	}

	public void setKeyToken(String keyToken) {
		this.keyToken = keyToken;
	}

	public String getValueToken() {
		return valueToken;
	}

	public void setValueToken(String valueToken) {
		this.valueToken = valueToken;
	}
	

	

}
