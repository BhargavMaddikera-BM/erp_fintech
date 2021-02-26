package com.blackstrawai.ap.dropdowns;

public class VendorCreditInvoiceVo {

	private Integer id;
	private String name;
	private String originalInvoiceDate;
	private Integer value;

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

	public String getOriginalInvoiceDate() {
		return originalInvoiceDate;
	}

	public void setOriginalInvoiceDate(String originalInvoiceDate) {
		this.originalInvoiceDate = originalInvoiceDate;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "VendorCreditInvoiceVo [id=" + id + ", name=" + name + ", originalInvoiceDate=" + originalInvoiceDate
				+ ", value=" + value + "]";
	}

}
