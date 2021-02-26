package com.blackstrawai.onboarding.user;

import java.sql.Timestamp;

public class ManageInvitesVo {
	private int id;
	private String from;
	private String organization;
	private Timestamp receivedOn;
	private Timestamp expiresOn;
	private String role;
	private String status;
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public Timestamp getReceivedOn() {
		return receivedOn;
	}
	public void setReceivedOn(Timestamp receivedOn) {
		this.receivedOn = receivedOn;
	}
	public Timestamp getExpiresOn() {
		return expiresOn;
	}
	public void setExpiresOn(Timestamp expiresOn) {
		this.expiresOn = expiresOn;
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
	
}
