package com.blackstrawai.attachments;

import com.blackstrawai.common.BaseVo;

public class AttachmentsVo extends BaseVo {
	private Integer id;

	private String type;

	private Integer typeId;

	private String version;

	private String fileName;

	private String status;

	private String size;

	private Integer documentTypeId;
	
	private boolean isAkshar;
	

	public boolean getIsAkshar() {
		return isAkshar;
	}

	public void setIsAkshar(boolean isAkshar) {
		this.isAkshar = isAkshar;
	}

	public Integer getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Integer documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AttachmentsVo [id=");
		builder.append(id);
		builder.append(", type=");
		builder.append(type);
		builder.append(", typeId=");
		builder.append(typeId);
		builder.append(", version=");
		builder.append(version);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", status=");
		builder.append(status);
		builder.append(", size=");
		builder.append(size);
		builder.append("]");
		return builder.toString();
	}

}
