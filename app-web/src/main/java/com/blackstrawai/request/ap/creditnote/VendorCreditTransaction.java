package com.blackstrawai.request.ap.creditnote;

import java.util.List;

import com.blackstrawai.ap.creditnote.CnProductVo;
import com.blackstrawai.ap.creditnote.CnTaxDistributionVo;

public class VendorCreditTransaction {

	private Integer sourceOfSupplyId;
	private Integer destinationOfSupplyId;
	private Integer taxApplicationMethodId;
	private Integer currencyid;
	private String exchangeRate;
	private String currencyCode;
	private String baseCurrencyCode;
	private String defaultCurrency;
	private String subTotal;
	private String discountValue;
	private Integer discountTypeId;
	private String discountAmount;
	private Boolean isApplyAfterTax;
	private List<CnTaxDistributionVo> groupedTax;
	private List<CnProductVo> products;
	private Integer tdsId;
	private String tdsValue;
	private String adjustment;
	private String total;

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

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getBaseCurrencyCode() {
		return baseCurrencyCode;
	}

	public void setBaseCurrencyCode(String baseCurrencyCode) {
		this.baseCurrencyCode = baseCurrencyCode;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(String discountValue) {
		this.discountValue = discountValue;
	}

	public Integer getDiscountTypeId() {
		return discountTypeId;
	}

	public void setDiscountTypeId(Integer discountTypeId) {
		this.discountTypeId = discountTypeId;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Boolean getIsApplyAfterTax() {
		return isApplyAfterTax;
	}

	public void setIsApplyAfterTax(Boolean isApplyAfterTax) {
		this.isApplyAfterTax = isApplyAfterTax;
	}

	public List<CnTaxDistributionVo> getGroupedTax() {
		return groupedTax;
	}

	public void setGroupedTax(List<CnTaxDistributionVo> groupedTax) {
		this.groupedTax = groupedTax;
	}

	public List<CnProductVo> getProducts() {
		return products;
	}

	public void setProducts(List<CnProductVo> products) {
		this.products = products;
	}

	public Integer getTdsId() {
		return tdsId;
	}

	public void setTdsId(Integer tdsId) {
		this.tdsId = tdsId;
	}

	public String getTdsValue() {
		return tdsValue;
	}

	public void setTdsValue(String tdsValue) {
		this.tdsValue = tdsValue;
	}

	public String getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(String adjustment) {
		this.adjustment = adjustment;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "VendorCreditTransaction [sourceOfSupplyId=" + sourceOfSupplyId + ", destinationOfSupplyId="
				+ destinationOfSupplyId + ", taxApplicationMethodId=" + taxApplicationMethodId + ", currencyid="
				+ currencyid + ", exchangeRate=" + exchangeRate + ", currencyCode=" + currencyCode
				+ ", baseCurrencyCode=" + baseCurrencyCode + ", defaultCurrency=" + defaultCurrency + ", subTotal="
				+ subTotal + ", discountValue=" + discountValue + ", discountTypeId=" + discountTypeId
				+ ", discountAmount=" + discountAmount + ", isApplyAfterTax=" + isApplyAfterTax + ", groupedTax="
				+ groupedTax + ", products=" + products + ", tdsId=" + tdsId + ", tdsValue=" + tdsValue
				+ ", adjustment=" + adjustment + ", total=" + total + "]";
	}

}
