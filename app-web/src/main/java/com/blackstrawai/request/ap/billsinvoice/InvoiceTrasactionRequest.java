package com.blackstrawai.request.ap.billsinvoice;

import java.util.List;

public class InvoiceTrasactionRequest {
	
	private Integer sourceOfSupplyId;
	
	private Integer destinationOfSupplyId;
	
	private Integer taxApplicationMethodId;
	
	private Integer currencyid ;
	
	private Double exchangeRate;
	
	private Double subTotal;
	
	private Double discountValue;
	
	private Integer discountTypeId;
	
	private Double discountAmount;
	
	private Integer discountAccountId;
	
	private String discountAccountName;
	
	private String discountAccountLevel;
	
	private Boolean isApplyAfterTax;
	
	private String gstTaxDistribution;
	
	private Integer tdsId;
	
	private Double tdsValue;
	
	private Double adjustment;
	
	private Integer adjustmentAccountId;
	
	private String adjustmentAccountName;
	
	private String adjustmentAccountLevel;
	
	private Double total;

	private List<InvoiceProductRequest> products;
	
	public Integer getSourceOfSupplyId() {
		return sourceOfSupplyId;
	}

	public void setSourceOfSupplyId(Integer sourceOfSupplyId) {
		this.sourceOfSupplyId = sourceOfSupplyId;
	}

	public Integer getDestinationOfSupplyId() {
		return destinationOfSupplyId;
	}

	public void setDestinationOfSupplyId(Integer destinationOfSupplyId) {
		this.destinationOfSupplyId = destinationOfSupplyId;
	}

	public Integer getTaxApplicationMethodId() {
		return taxApplicationMethodId;
	}

	public void setTaxApplicationMethodId(Integer taxApplicationMethodId) {
		this.taxApplicationMethodId = taxApplicationMethodId;
	}

	public Integer getCurrencyid() {
		return currencyid;
	}

	public void setCurrencyid(Integer currencyid) {
		this.currencyid = currencyid;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(Double discountValue) {
		this.discountValue = discountValue;
	}

	public Integer getDiscountTypeId() {
		return discountTypeId;
	}

	public void setDiscountTypeId(Integer discountTypeId) {
		this.discountTypeId = discountTypeId;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Integer getDiscountAccountId() {
		return discountAccountId;
	}

	public void setDiscountAccountId(Integer discountAccountId) {
		this.discountAccountId = discountAccountId;
	}

	public Boolean getIsApplyAfterTax() {
		return isApplyAfterTax;
	}

	public void setIsApplyAfterTax(Boolean isApplyAfterTax) {
		this.isApplyAfterTax = isApplyAfterTax;
	}

	public String getGstTaxDistribution() {
		return gstTaxDistribution;
	}

	public void setGstTaxDistribution(String gstTaxDistribution) {
		this.gstTaxDistribution = gstTaxDistribution;
	}

	public Integer getTdsId() {
		return tdsId;
	}

	public void setTdsId(Integer tdsId) {
		this.tdsId = tdsId;
	}

	public Double getTdsValue() {
		return tdsValue;
	}

	public void setTdsValue(Double tdsValue) {
		this.tdsValue = tdsValue;
	}

	public Double getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(Double adjustment) {
		this.adjustment = adjustment;
	}

	public Integer getAdjustmentAccountId() {
		return adjustmentAccountId;
	}

	public void setAdjustmentAccountId(Integer adjustmentAccountId) {
		this.adjustmentAccountId = adjustmentAccountId;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getDiscountAccountName() {
		return discountAccountName;
	}

	public void setDiscountAccountName(String discountAccountName) {
		this.discountAccountName = discountAccountName;
	}

	public String getDiscountAccountLevel() {
		return discountAccountLevel;
	}

	public void setDiscountAccountLevel(String discountAccountLevel) {
		this.discountAccountLevel = discountAccountLevel;
	}

	public String getAdjustmentAccountName() {
		return adjustmentAccountName;
	}

	public void setAdjustmentAccountName(String adjustmentAccountName) {
		this.adjustmentAccountName = adjustmentAccountName;
	}

	public String getAdjustmentAccountLevel() {
		return adjustmentAccountLevel;
	}

	public void setAdjustmentAccountLevel(String adjustmentAccountLevel) {
		this.adjustmentAccountLevel = adjustmentAccountLevel;
	}

	public List<InvoiceProductRequest> getProducts() {
		return products;
	}

	public void setProducts(List<InvoiceProductRequest> products) {
		this.products = products;
	}
	
	
}
