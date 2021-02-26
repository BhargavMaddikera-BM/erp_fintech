package com.blackstrawai.ap.purchaseorder;

import java.util.List;

public class PoItemsVo {

	private Integer sourceOfSupplyId;
	
	private Integer taxApplicationMethodId;
	
	private Integer currencyId;
	
	private String exchangeRate;
	
	private Double subTotal;
	
	private Double discountValue;
	
	private Integer discountTypeId;
	
	private Double discountAmount;
	
	private Boolean isApplyAfterTax;
	
	private Integer tdsId;
	
	private Double tdsValue;
	
	private Double adjustment;
	
	private Double total;
	
	private boolean isRCMApplicable;
	
	private List<PoTaxDistributionVo> groupedTax;

	private List<PoProductVo> products;
	

	public boolean getIsRCMApplicable() {
		return isRCMApplicable;
	}

	public void setIsRCMApplicable(boolean isRCMApplicable) {
		this.isRCMApplicable = isRCMApplicable;
	}

	public Integer getSourceOfSupplyId() {
		return sourceOfSupplyId;
	}

	public void setSourceOfSupplyId(Integer sourceOfSupplyId) {
		this.sourceOfSupplyId = sourceOfSupplyId;
	}

	public Integer getTaxApplicationMethodId() {
		return taxApplicationMethodId;
	}

	public void setTaxApplicationMethodId(Integer taxApplicationMethodId) {
		this.taxApplicationMethodId = taxApplicationMethodId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
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

	public Boolean getIsApplyAfterTax() {
		return isApplyAfterTax;
	}

	public void setIsApplyAfterTax(Boolean isApplyAfterTax) {
		this.isApplyAfterTax = isApplyAfterTax;
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

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public List<PoProductVo> getProducts() {
		return products;
	}

	public void setProducts(List<PoProductVo> products) {
		this.products = products;
	}

	public List<PoTaxDistributionVo> getGroupedTax() {
		return groupedTax;
	}

	public void setGroupedTax(List<PoTaxDistributionVo> groupedTax) {
		this.groupedTax = groupedTax;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PoItemsVo [sourceOfSupplyId=");
		builder.append(sourceOfSupplyId);
		builder.append(", taxApplicationMethodId=");
		builder.append(taxApplicationMethodId);
		builder.append(", currencyId=");
		builder.append(currencyId);
		builder.append(", exchangeRate=");
		builder.append(exchangeRate);
		builder.append(", subTotal=");
		builder.append(subTotal);
		builder.append(", discountValue=");
		builder.append(discountValue);
		builder.append(", discountTypeId=");
		builder.append(discountTypeId);
		builder.append(", discountAmount=");
		builder.append(discountAmount);
		builder.append(", isApplyAfterTax=");
		builder.append(isApplyAfterTax);
		builder.append(", tdsId=");
		builder.append(tdsId);
		builder.append(", tdsValue=");
		builder.append(tdsValue);
		builder.append(", adjustment=");
		builder.append(adjustment);
		builder.append(", total=");
		builder.append(total);
		builder.append(", groupedTax=");
		builder.append(groupedTax);
		builder.append(", products=");
		builder.append(products);
		builder.append("]");
		return builder.toString();
	}
	
	
}
