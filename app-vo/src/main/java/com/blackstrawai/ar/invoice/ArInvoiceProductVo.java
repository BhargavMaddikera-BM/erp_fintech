package com.blackstrawai.ar.invoice;

public class ArInvoiceProductVo {

	private Integer id;
	
	private Integer tempId;
	
	private Integer productId;
	
	private String type;
	
	private String productDisplayname;
	
	private Integer productAccountId;
	
	private String productAccountName;
	
	private String productAccountLevel;
	
	private String quantity;
	
	private String baseQuantity;
	
	private Integer measureId;
	
	private Double baseUnitPrice;
	
	private Double unitPrice;
	
	private Integer taxRateId;
	
	private Double amount;
	
	private Double discount ;
	
	private Integer discountType;
	
	private Double discountAmount;
	
	private Double netAmount;
	
	private String status;
	
	private String hsn;
	
	private String description;

	private ArInvoiceTaxDetailsVo taxDetails;
	
	

	public Integer getTempId() {
		return tempId;
	}

	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}

	public String getBaseQuantity() {
		return baseQuantity;
	}

	public void setBaseQuantity(String baseQuantity) {
		this.baseQuantity = baseQuantity;
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

	public ArInvoiceTaxDetailsVo getTaxDetails() {
		return taxDetails;
	}

	public void setTaxDetails(ArInvoiceTaxDetailsVo taxDetails) {
		this.taxDetails = taxDetails;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ARInvoiceProductVo [id=");
		builder.append(id);
		builder.append(", productId=");
		builder.append(productId);
		builder.append(", type=");
		builder.append(type);
		builder.append(", productDisplayname=");
		builder.append(productDisplayname);
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
		builder.append(", baseUnitPrice=");
		builder.append(baseUnitPrice);
		builder.append(", unitPrice=");
		builder.append(unitPrice);
		builder.append(", taxRateId=");
		builder.append(taxRateId);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", discount=");
		builder.append(discount);
		builder.append(", discountType=");
		builder.append(discountType);
		builder.append(", discountAmount=");
		builder.append(discountAmount);
		builder.append(", netAmount=");
		builder.append(netAmount);
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
