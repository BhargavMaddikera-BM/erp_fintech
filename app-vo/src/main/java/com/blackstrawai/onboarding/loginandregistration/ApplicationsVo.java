package com.blackstrawai.onboarding.loginandregistration;

import java.util.List;

import com.blackstrawai.common.TokenVo;

public class ApplicationsVo extends TokenVo {
	
	private List<ApplicationVo>apps;

	public List<ApplicationVo> getApps() {
		return apps;
	}

	public void setApps(List<ApplicationVo> apps) {
		this.apps = apps;
	}

}
