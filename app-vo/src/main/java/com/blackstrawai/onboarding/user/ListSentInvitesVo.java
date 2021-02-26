package com.blackstrawai.onboarding.user;

import java.sql.Timestamp;

public class ListSentInvitesVo {
	private String to;
	private Timestamp sentOn;
	private String role;
	private String status;
	private int id;
	private String reason;
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Timestamp getSentOn() {
		return sentOn;
	}
	public void setSentOn(Timestamp sentOn) {
		this.sentOn = sentOn;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
