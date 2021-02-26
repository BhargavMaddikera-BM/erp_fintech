package com.blackstrawai.externalintegration.general.akshar;

public class AksharProductItemVo {
	
	private int id;
	private String productId;
	private String productDisplayname;
	private String hsn_sac_code;
	private int productAccountId;
	private String productAccountName;
	private String productAccountLevel;
	private String quantity;
	private int measureId;
	private String unitPrice;
	private String status;
	private String amount;
	private String taxDetails;
	private Boolean isBillable;
	private String inputTaxCredit;
	private boolean isProductExist;
	private boolean isTaxGroupExist;
	private int taxRateId;
	
	
	
	
	public int getTaxRateId() {
		return taxRateId;
	}
	public void setTaxRateId(int taxRateId) {
		this.taxRateId = taxRateId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getProductDisplayname() {
		return productDisplayname;
	}
	public void setProductDisplayname(String productDisplayname) {
		this.productDisplayname = productDisplayname;
	}
	
	public String getHsn_sac_code() {
		return hsn_sac_code;
	}
	public void setHsn_sac_code(String hsn_sac_code) {
		this.hsn_sac_code = hsn_sac_code;
	}
	public int getProductAccountId() {
		return productAccountId;
	}
	public void setProductAccountId(int productAccountId) {
		this.productAccountId = productAccountId;
	}
	public String getProductAccountName() {
		return productAccountName;
	}
	public void setProductAccountName(String productAccountName) {
		this.productAccountName = productAccountName;
	}
	public String getProductAccountLevel() {
		return productAccountLevel;
	}
	public void setProductAccountLevel(String productAccountLevel) {
		this.productAccountLevel = productAccountLevel;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public int getMeasureId() {
		return measureId;
	}
	public void setMeasureId(int measureId) {
		this.measureId = measureId;
	}
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTaxDetails() {
		return taxDetails;
	}
	public void setTaxDetails(String taxDetails) {
		this.taxDetails = taxDetails;
	}
	public Boolean getIsBillable() {
		return isBillable;
	}
	public void setIsBillable(Boolean isBillable) {
		this.isBillable = isBillable;
	}
	public String getInputTaxCredit() {
		return inputTaxCredit;
	}
	public void setInputTaxCredit(String inputTaxCredit) {
		this.inputTaxCredit = inputTaxCredit;
	}
	public boolean getIsProductExist() {
		return isProductExist;
	}
	public void setIsProductExist(boolean isProductExist) {
		this.isProductExist = isProductExist;
	}
	public boolean getIsTaxGroupExist() {
		return isTaxGroupExist;
	}
	public void setIsTaxGroupExist(boolean isTaxGroupExist) {
		this.isTaxGroupExist = isTaxGroupExist;
	}
	

}
