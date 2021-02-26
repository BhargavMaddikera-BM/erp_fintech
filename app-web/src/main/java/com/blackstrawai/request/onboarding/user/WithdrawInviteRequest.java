package com.blackstrawai.request.onboarding.user;

import com.blackstrawai.common.BaseRequest;

public class WithdrawInviteRequest extends BaseRequest {
	private int id;
	private String reason;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
