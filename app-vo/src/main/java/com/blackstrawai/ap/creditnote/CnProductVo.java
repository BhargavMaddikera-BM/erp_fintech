package com.blackstrawai.ap.creditnote;

public class CnProductVo {

	private Integer id;
	
	private Integer productId;
	
	private String description ;
	
	private String productDisplayname;
	
	private Integer productAccountId;
	
	private String productAccountDisplayname;
	
	private String productAccountName;
	
	private String productAccountLevel;
	
	private String quantity;
	
	private String quantityName;
	
	private Integer measureId;
	
	private Double unitPrice;
	
	private Double baseUnitPrice;
	
	private Integer taxRateId;
	
	private String taxDisplayValue;
	
	private Double amount;
	
	private Double netAmount;
	
	private String status;
	
	private String hsn;

	private String type;

	private CnTaxDetailsVo taxDetails;

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getProductAccountDisplayname() {
		return productAccountDisplayname;
	}

	public void setProductAccountDisplayname(String productAccountDisplayname) {
		this.productAccountDisplayname = productAccountDisplayname;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHsn() {
		return hsn;
	}

	public void setHsn(String hsn) {
		this.hsn = hsn;
	}

	public CnTaxDetailsVo getTaxDetails() {
		return taxDetails;
	}

	public void setTaxDetails(CnTaxDetailsVo taxDetails) {
		this.taxDetails = taxDetails;
	}
	

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

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PoProductVo [id=");
		builder.append(id);
		builder.append(", productId=");
		builder.append(productId);
		builder.append(", productDisplayname=");
		builder.append(productDisplayname);
		builder.append(", productAccountId=");
		builder.append(productAccountId);
		builder.append(", productAccountDisplayname=");
		builder.append(productAccountDisplayname);
		builder.append(", productAccountName=");
		builder.append(productAccountName);
		builder.append(", productAccountLevel=");
		builder.append(productAccountLevel);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", quantityName=");
		builder.append(quantityName);
		builder.append(", measureId=");
		builder.append(measureId);
		builder.append(", unitPrice=");
		builder.append(unitPrice);
		builder.append(", taxRateId=");
		builder.append(taxRateId);
		builder.append(", taxDisplayValue=");
		builder.append(taxDisplayValue);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", status=");
		builder.append(status);
		builder.append(", hsn=");
		builder.append(hsn);
		builder.append(", taxDetails=");
		builder.append(taxDetails);
		builder.append("]");
		return builder.toString();
	}
	
	
}
