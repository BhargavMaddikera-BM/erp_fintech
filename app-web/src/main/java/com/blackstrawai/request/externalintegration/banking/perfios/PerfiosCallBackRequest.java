package com.blackstrawai.request.externalintegration.banking.perfios;

import com.blackstrawai.common.BaseRequest;

public class PerfiosCallBackRequest extends BaseRequest {
	
	private String accountId;
	private String success;
	private String description;
	private String message;
	private String userId;
	private String uniqueUserId;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUniqueUserId() {
		return uniqueUserId;
	}
	public void setUniqueUserId(String uniqueUserId) {
		this.uniqueUserId = uniqueUserId;
	}
}
