package com.blackstrawai.upload;

import com.blackstrawai.common.BaseVo;

public class UploadFileVo extends BaseVo {

	private String data;
	private String moduleName;
	private String fileType;
	private boolean duplicacy;

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

	public boolean isDuplicacy() {
		return duplicacy;
	}

	public void setDuplicacy(boolean duplicacy) {
		this.duplicacy = duplicacy;
	}

}
