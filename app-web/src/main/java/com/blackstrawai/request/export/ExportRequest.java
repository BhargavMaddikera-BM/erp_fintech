package com.blackstrawai.request.export;

import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class ExportRequest extends BaseRequest {

	private String moduleName;
	private String fileType;
	private List<Integer> listOfId;
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
		return listOfId;
	}

	public void setListOfId(List<Integer> listOfId) {
		this.listOfId = listOfId;
	}

}
