package com.blackstrawai.request.ar.invoice;

public class ArInvoiceProductRequest {
	
	private Integer id;
	
	private Integer tempId;
	
	private Integer productId;
	
	private String productDisplayname;
	
	private Integer productAccountId;
	
	private String productAccountName;
	
	private String productAccountLevel;
	
	private String quantity;
	
	private String quantityName;
	
	private Integer measureId;
	
	private Double baseUnitPrice;
	
	private Double unitPrice;
	
	private Integer taxRateId;
	
	private String taxDisplayValue;
	
	private Double amount;
	
	private Double discount ;
	
	private Integer discountType;
	
	private Double discountAmount;
	
	private Double netAmount;
	
	private String status;
	
	private String hsn;
	
	private String description;
	
	
	public Integer getTempId() {
		return tempId;
	}

	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}

	private ArInvoiceTaxDetailsRequest taxDetails;

	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getBaseUnitPrice() {
		return baseUnitPrice;
	}

	public void setBaseUnitPrice(Double baseUnitPrice) {
		this.baseUnitPrice = baseUnitPrice;
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

	public String getProductDisplayname() {
		return productDisplayname;
	}

	public void setProductDisplayname(String productDisplayname) {
		this.productDisplayname = productDisplayname;
	}

	public Integer getProductAccountId() {
		return productAccountId;
	}

	public void setProductAccountId(Integer productAccountId) {
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

	public String getQuantityName() {
		return quantityName;
	}

	public void setQuantityName(String quantityName) {
		this.quantityName = quantityName;
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

	public String getTaxDisplayValue() {
		return taxDisplayValue;
	}

	public void setTaxDisplayValue(String taxDisplayValue) {
		this.taxDisplayValue = taxDisplayValue;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getHsn() {
		return hsn;
	}

	public void setHsn(String hsn) {
		this.hsn = hsn;
	}

	public ArInvoiceTaxDetailsRequest getTaxDetails() {
		return taxDetails;
	}

	public void setTaxDetails(ArInvoiceTaxDetailsRequest taxDetails) {
		this.taxDetails = taxDetails;
	}

	
	
}
