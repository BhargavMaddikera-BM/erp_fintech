package com.blackstrawai.onboarding.organization;

public class IncomeTaxLoginVo {

private String pan;

public String getPan() {
	return pan;
}

public void setPan(String pan) {
	this.pan = pan;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

private String password;

private int id;
private boolean rememberMe;

public boolean isRememberMe() {
	return rememberMe;
}

public void setRememberMe(boolean rememberMe) {
	this.rememberMe = rememberMe;
}
}
