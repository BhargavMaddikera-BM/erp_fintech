package com.blackstrawai.request.ap.purchaseorder;


public class PoProductRequest {
	private Integer id;
	
	private Integer productId;
	
	private String description ;

	private Integer productAccountId;
	
	private String productAccountName;
	
	private String productAccountLevel;
	
	private String quantity;
	
	private Integer measureId;
	
	private Double unitPrice;
	
	private Integer taxRateId;
		
	private Double amount;
	
	private String status;
	
	private PoTaxDetailsRequest taxDetails;

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

	public PoTaxDetailsRequest getTaxDetails() {
		return taxDetails;
	}

	public void setTaxDetails(PoTaxDetailsRequest taxDetails) {
		this.taxDetails = taxDetails;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PoProductRequest [id=");
		builder.append(id);
		builder.append(", productId=");
		builder.append(productId);
		builder.append(", productAccountId=");
		builder.append(productAccountId);
		builder.append(", productAccountName=");
		builder.append(productAccountName);
		builder.append(", productAccountLevel=");
		builder.append(productAccountLevel);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", measureId=");
		builder.append(measureId);
		builder.append(", unitPrice=");
		builder.append(unitPrice);
		builder.append(", taxRateId=");
		builder.append(taxRateId);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", status=");
		builder.append(status);
		builder.append(", taxDetails=");
		builder.append(taxDetails);
		builder.append("]");
		return builder.toString();
	}

	
	
}
