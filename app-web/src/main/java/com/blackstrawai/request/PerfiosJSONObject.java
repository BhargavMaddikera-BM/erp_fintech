package com.blackstrawai.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Model for Request Body")
public class PerfiosJSONObject {

	@ApiModelProperty(value="accountId")
	private String accountId;
	@ApiModelProperty(value="success")
	private String success;
	@ApiModelProperty(value="description")
	private String description;
	@ApiModelProperty(value="message")
	private String message;
	@ApiModelProperty(value="userId")
	private String userId;
	@ApiModelProperty(value="uniqueUserId")
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
