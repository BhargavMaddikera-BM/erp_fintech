package com.blackstrawai.attachments;

public class UploadFileVo {
	private Integer id;

	private String name;

	private String data;

	private String size;

	private Integer documentType;
	
	private boolean isAkshar;
	
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UploadFileVo [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", data=");
		builder.append(data);
		builder.append(", size=");
		builder.append(size);
		builder.append("]");
		return builder.toString();
	}

}
