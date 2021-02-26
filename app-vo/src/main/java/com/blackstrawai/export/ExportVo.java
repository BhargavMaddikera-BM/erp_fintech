package com.blackstrawai.export;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class ExportVo extends BaseVo {

	private String moduleName;
	private String fileType;
	private List<Integer> ListOfId;
	private Integer organizationId;

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public List<Integer> getListOfId() {
		return ListOfId;
	}

	public void setListOfId(List<Integer> listOfId) {
		ListOfId = listOfId;
	}

}
