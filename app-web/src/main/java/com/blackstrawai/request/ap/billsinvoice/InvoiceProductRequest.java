package com.blackstrawai.request.ap.billsinvoice;

public class InvoiceProductRequest {
	
	private Integer id;

	private Integer tempId;
	
	private Integer productId;
	
	private Integer productAccountId;
	
	private String productAccountName;
	
	private String productAccountLevel;
	
	private String quantity;
	
	private Integer measureId;
	
	private Double unitPrice;
	
	private Integer taxRateId;
	
	private Integer customerId;
	
	private Boolean isBillable;
	
	private String inputTaxCredit;
	
	private Double amount;
	
	private String status;
	
	private String description;
	
	private InvoiceTaxDetailsRequest taxDetails;

	
	
	public Integer getTempId() {
		return tempId;
	}

	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductAccountId() {
		return productAccountId;
	}

	public void setProductAccountId(Integer productAccountId) {
		this.productAccountId = productAccountId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public Integer getMeasureId() {
		return measureId;
	}

	public void setMeasureId(Integer measureId) {
		this.measureId = measureId;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getTaxRateId() {
		return taxRateId;
	}

	public void setTaxRateId(Integer taxRateId) {
		this.taxRateId = taxRateId;
	}

	
	public InvoiceTaxDetailsRequest getTaxDetails() {
		return taxDetails;
	}

	public void setTaxDetails(InvoiceTaxDetailsRequest taxDetails) {
		this.taxDetails = taxDetails;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	
	
}
