package com.blackstrawai.request.upload;

import com.blackstrawai.common.BaseRequest;

public class ImportFileRequest extends BaseRequest {

	private String data;
	private String moduleName;
	private String fileType;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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

}
