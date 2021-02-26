package com.blackstrawai.keycontact.statutorybody;

import com.blackstrawai.common.BookKeepingSettingsVo;
import com.blackstrawai.common.TokenVo;

public class StatutoryBodyVo extends TokenVo {

	private Integer id;
	private String name;
	private String departmentName;
	private String type;
	private String registrationNo;
	private String date;
	private int state;
	private String city;
	private String addr1;
	private String addr2;
	private String pincode;
	private String website;
	private int organizationId;
	private String roleName;
	private int updateUserId;	
	private String updateRoleName;
	private String status;
	private String statutoryName;
	private BookKeepingSettingsVo bookKeepingSettings;
	
	
	public BookKeepingSettingsVo getBookKeepingSettings() {
		return bookKeepingSettings;
	}
	public void setBookKeepingSettings(BookKeepingSettingsVo bookKeepingSettings) {
		this.bookKeepingSettings = bookKeepingSettings;
	}
	public String getStatutoryName() {
		return statutoryName;
	}
	public void setStatutoryName(String statutoryName) {
		this.statutoryName = statutoryName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRegistrationNo() {
		return registrationNo;
	}
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(int updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getUpdateRoleName() {
		return updateRoleName;
	}
	public void setUpdateRoleName(String updateRoleName) {
		this.updateRoleName = updateRoleName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
