package com.blackstrawai.workflow;

import java.util.List;
import java.util.Set;

public class WorkflowSettingsRuleData {
	;
	private Integer validationParameterId;
	private Integer choiceId;
	private String choiceName;
	private String lessThan;
	private String greaterThan;
	private String equal;
	private String min;
	private String max;
	private List<Object> is;
	private List<Object> isNot;
	private boolean enabled;
	private boolean disabled;
	private int approvalTypeId;
	//private List<WorkflowSettingsCommonVo> rolesList;
	private List<WorkflowSettingsCommonVo> usersList;
	private List<WorkflowSettingsCommonVo> setSequence;
	private boolean inApp;
	private boolean email;
	private boolean sms;
	private boolean whatsApp;
	

	
	public Integer getValidationParameterId() {
		return validationParameterId;
	}
	public void setValidationParameterId(Integer validationParameterId) {
		this.validationParameterId = validationParameterId;
	}
	public Integer getChoiceId() {
		return choiceId;
	}
	public void setChoiceId(Integer choiceId) {
		this.choiceId = choiceId;
	}
	public String getChoiceName() {
		return choiceName;
	}
	public void setChoiceName(String choiceName) {
		this.choiceName = choiceName;
	}
	public String getLessThan() {
		return lessThan;
	}
	public void setLessThan(String lessThan) {
		this.lessThan = lessThan;
	}
	public String getGreaterThan() {
		return greaterThan;
	}
	public void setGreaterThan(String greaterThan) {
		this.greaterThan = greaterThan;
	}
	public String getEqual() {
		return equal;
	}
	public void setEqual(String equal) {
		this.equal = equal;
	}
	public List<Object> getIs() {
		return is;
	}
	public void setIs(List<Object> is) {
		this.is = is;
	}
	public List<Object> getIsNot() {
		return isNot;
	}
	public void setIsNot(List<Object> isNot) {
		this.isNot = isNot;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public int getApprovalTypeId() {
		return approvalTypeId;
	}
	public void setApprovalTypeId(int approvalTypeId) {
		this.approvalTypeId = approvalTypeId;
	}
//	public List<WorkflowSettingsCommonVo> getRolesList() {
//		return rolesList;
//	}
//	public void setRolesList(List<WorkflowSettingsCommonVo> rolesList) {
//		this.rolesList = rolesList;
//	}
	public List<WorkflowSettingsCommonVo> getUsersList() {
		return usersList;
	}
	public void setUsersList(List<WorkflowSettingsCommonVo> usersList) {
		this.usersList = usersList;
	}
	public List<WorkflowSettingsCommonVo> getSetSequence() {
		return setSequence;
	}
	public void setSetSequence(List<WorkflowSettingsCommonVo> setSequence) {
		this.setSequence = setSequence;
	}
	public boolean isInApp() {
		return inApp;
	}
	public void setInApp(boolean inApp) {
		this.inApp = inApp;
	}
	public boolean isEmail() {
		return email;
	}
	public void setEmail(boolean email) {
		this.email = email;
	}
	public boolean isSms() {
		return sms;
	}
	public void setSms(boolean sms) {
		this.sms = sms;
	}
	public boolean isWhatsApp() {
		return whatsApp;
	}
	public void setWhatsApp(boolean whatsApp) {
		this.whatsApp = whatsApp;
	}
	
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	@Override
	public String toString() {
		return "WorkflowSettingsRuleData [validationParameterId=" + validationParameterId + ", choiceId=" + choiceId
				+ ", choiceName=" + choiceName + ", lessThan=" + lessThan + ", greaterThan=" + greaterThan + ", equal="
				+ equal + ", min=" + min + ", max=" + max + ", is=" + is + ", isNot=" + isNot + ", enabled=" + enabled
				+ ", disabled=" + disabled + ", approvalTypeId=" + approvalTypeId  
				+ ", usersList=" + usersList + ", setSequence=" + setSequence + ", inApp=" + inApp + ", email=" + email
				+ ", sms=" + sms + ", whatsApp=" + whatsApp + ", getValidationParameterId()="
				+ getValidationParameterId() + ", getChoiceId()=" + getChoiceId() + ", getChoiceName()="
				+ getChoiceName() + ", getLessThan()=" + getLessThan() + ", getGreaterThan()=" + getGreaterThan()
				+ ", getEqual()=" + getEqual() + ", getIs()=" + getIs() + ", getIsNot()=" + getIsNot()
				+ ", isEnabled()=" + isEnabled() + ", isDisabled()=" + isDisabled() + ", getApprovalTypeId()="
				+ getApprovalTypeId() +  ", getUsersList()=" + getUsersList()
				+ ", getSetSequence()=" + getSetSequence() + ", isInApp()=" + isInApp() + ", isEmail()=" + isEmail()
				+ ", isSms()=" + isSms() + ", isWhatsApp()=" + isWhatsApp() + ", getMin()=" + getMin() + ", getMax()="
				+ getMax() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
}