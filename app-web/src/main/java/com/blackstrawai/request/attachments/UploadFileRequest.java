package com.blackstrawai.request.attachments;

public class UploadFileRequest {
	private String name;

	private String data;

	private String size;

	private Integer documentType;

	private boolean isAkshar;

	private String moduleName;
	
	private String fileType;
	
	private boolean duplicacy;

	public boolean getIsAkshar() {
		return isAkshar;
	}

	public void setIsAkshar(boolean isAkshar) {
		this.isAkshar = isAkshar;
	}

	public Integer getDocumentType() {
		return documentType;
	}

	public void setDocumentType(Integer documentType) {
		this.documentType = documentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
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

	public void setAkshar(boolean isAkshar) {
		this.isAkshar = isAkshar;
	}

}
