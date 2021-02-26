package com.blackstrawai.request.externalintegration.general;

import java.util.List;

public class AksharLineItemRequest {
	
	private String amount;
	
	private String hsn_sac_code;
	
	private String itemName;
	
	private String quantity;
	
	private String rate;
	
	private List<String> tax;

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getHsn_sac_code() {
		return hsn_sac_code;
	}

	public void setHsn_sac_code(String hsn_sac_code) {
		this.hsn_sac_code = hsn_sac_code;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public List<String> getTax() {
		return tax;
	}

	public void setTax(List<String> tax) {
		this.tax = tax;
	}


}
