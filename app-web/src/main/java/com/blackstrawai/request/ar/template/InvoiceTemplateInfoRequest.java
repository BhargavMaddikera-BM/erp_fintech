package com.blackstrawai.request.ar.template;

public class InvoiceTemplateInfoRequest {
	
	private Integer templateId;  
	private String logoPosition; 
	private String headerNotes;
	private String footerSectionNotes;
	private String termsAndContd;
	private Boolean bankAccInfo;
	private String  footNotes;
	private String  footNotePosition;
	private Boolean  isAnnex;
	private Integer  userId;
	private Integer  organizationId;
	private String  roleName;
	private String  updateUserId;
	private String   updateRoleName;
	private String status;
	private String templateName;
	
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer id) {
		this.templateId = id;
	}
	public String getLogoPosition() {
		return logoPosition;
	}
	public void setLogoPosition(String logo_position) {
		this.logoPosition = logo_position;
	}
	public String getHeaderNotes() {
		return headerNotes;
	}
	public void setHeaderNotes(String header_notes) {
		this.headerNotes = header_notes;
	}
	public String getFooterSectionNotes() {
		return footerSectionNotes;
	}
	public void setFooterSectionNotes(String footer_section_notes) {
		this.footerSectionNotes = footer_section_notes;
	}
	public String getTermsAndContd() {
		return termsAndContd;
	}
	public void setTermsAndContd(String terms_and_contd) {
		this.termsAndContd = terms_and_contd;
	}
	public Boolean getBankAccInfo() {
		return bankAccInfo;
	}
	public void setBankAccInfo(Boolean bank_acc_info) {
		this.bankAccInfo = bank_acc_info;
	}
	public String getFootNotes() {
		return footNotes;
	}
	public void setFootNotes(String foot_notes) {
		this.footNotes = foot_notes;
	}
	public String getFootNotePosition() {
		return footNotePosition;
	}
	public void setFootNotePosition(String foot_note_position) {
		this.footNotePosition = foot_note_position;
	}
	public Boolean getIsAnnex() {
		return isAnnex;
	}
	public void setIsAnnex(Boolean is_annex) {
		this.isAnnex = is_annex;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer user_id) {
		this.userId = user_id;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organization_id) {
		this.organizationId = organization_id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String role_name) {
		this.roleName = role_name;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String update_user_id) {
		this.updateUserId = update_user_id;
	}
	public String getUpdateRoleName() {
		return updateRoleName;
	}
	public void setUpdateRoleName(String update_role_name) {
		this.updateRoleName = update_role_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	
}
