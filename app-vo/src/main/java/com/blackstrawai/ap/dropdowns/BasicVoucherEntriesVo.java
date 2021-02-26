package com.blackstrawai.ap.dropdowns;

public class BasicVoucherEntriesVo {
	
	private int id;
	private String prefix;
	private String suffix;
	private String minimumDigits;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getMinimumDigits() {
		return minimumDigits;
	}
	public void setMinimumDigits(String minimumDigits) {
		this.minimumDigits = minimumDigits;
	}
	@Override
	public String toString() {
		return "BasicVoucherEntriesVo [id=" + id + ", prefix=" + prefix + ", suffix=" + suffix + ", minimumDigits="
				+ minimumDigits + "]";
	}
	
	
}
