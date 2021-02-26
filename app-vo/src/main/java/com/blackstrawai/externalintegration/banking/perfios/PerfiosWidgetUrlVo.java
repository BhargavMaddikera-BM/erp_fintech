package com.blackstrawai.externalintegration.banking.perfios;

public class PerfiosWidgetUrlVo {
	private String authToken;
	private String userAuth;
	private String autoUpdateWidgetUrl;
	private String uniqueUserId;
	
	public String getUniqueUserId() {
		return uniqueUserId;
	}
	public void setUniqueUserId(String uniqueUserId) {
		this.uniqueUserId = uniqueUserId;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getUserAuth() {
		return userAuth;
	}
	public void setUserAuth(String userAuth) {
		this.userAuth = userAuth;
	}
	public String getAutoUpdateWidgetUrl() {
		return autoUpdateWidgetUrl;
	}
	public void setAutoUpdateWidgetUrl(String autoUpdateWidgetUrl) {
		this.autoUpdateWidgetUrl = autoUpdateWidgetUrl;
	}

	
	
}
