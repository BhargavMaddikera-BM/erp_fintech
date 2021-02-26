package com.blackstrawai.request.onboarding.loginandregistration;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseRequest;

public class ProfileRequest extends BaseRequest{
	
	  private String firstName;
	  private String lastName;
	  private String mobileNo;
	  private String emailId;
	  private String roleName;
	  private UploadFileVo profilePic;
	  
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public UploadFileVo getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(UploadFileVo profilePic) {
		this.profilePic = profilePic;
	}

}
