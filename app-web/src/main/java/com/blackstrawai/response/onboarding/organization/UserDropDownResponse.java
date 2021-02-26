package com.blackstrawai.response.onboarding.organization;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.dropdowns.UserDropDownVo;

public class UserDropDownResponse extends BaseResponse{
	
	private UserDropDownVo data;

	public UserDropDownVo getData() {
		return data;
	}

	public void setData(UserDropDownVo data) {
		this.data = data;
	}

	

}
