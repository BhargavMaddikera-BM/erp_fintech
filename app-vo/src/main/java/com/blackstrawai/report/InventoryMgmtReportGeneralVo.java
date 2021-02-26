package com.blackstrawai.report;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InventoryMgmtReportGeneralVo {

  private String id;
  private String productId;
  private String productCategory;
  private String productName;
  private BigDecimal openingStock = BigDecimal.ZERO;
  private BigDecimal openingStockValue = BigDecimal.ZERO;
  private BigDecimal quantityBought = BigDecimal.ZERO;
  private BigDecimal quantitySold = BigDecimal.ZERO;
  private BigDecimal stockRate = BigDecimal.ZERO;
  private BigDecimal closingStock = BigDecimal.ZERO;
  private BigDecimal closingStockValue = BigDecimal.ZERO;
  private BigDecimal quantityBoughtPrev = BigDecimal.ZERO;
  private BigDecimal quantitySoldPrev = BigDecimal.ZERO;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(String productCategory) {
    this.productCategory = productCategory;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productnName) {
    this.productName = productnName;
  }

  public BigDecimal getOpeningStock() {

    return (openingStock != null)
        ? openingStock.setScale(2, RoundingMode.CEILING)
        : BigDecimal.ZERO;
  }

  public void setOpeningStock(BigDecimal openingStock) {
    this.openingStock = openingStock;
  }

  public BigDecimal getOpeningStockValue() {
    return (openingStockValue != null)
        ? openingStockValue.setScale(2, RoundingMode.CEILING)
        : BigDecimal.ZERO;
  }

  public void setOpeningStockValue(BigDecimal openingStockValue) {
    this.openingStockValue = openingStockValue;
  }

  public BigDecimal getQuantityBought() {

    return (quantityBought != null)
        ? quantityBought.setScale(2, RoundingMode.CEILING)
        : BigDecimal.ZERO;
  }

  public void setQuantityBought(BigDecimal quantityBought) {
    this.quantityBought = quantityBought;
  }

  public BigDecimal getQuantitySold() {

    return (quantitySold != null)
        ? quantitySold.setScale(2, RoundingMode.CEILING)
        : BigDecimal.ZERO;
  }

  public void setQuantitySold(BigDecimal quantitySold) {
    this.quantitySold = quantitySold;
  }

  public BigDecimal getStockRate() {

    return (stockRate != null) ? stockRate.setScale(2, RoundingMode.CEILING) : BigDecimal.ZERO;
  }

  public void setStockRate(BigDecimal stockRate) {
    this.stockRate = stockRate;
  }

  public BigDecimal getClosingStock() {
    return (closingStock != null)
        ? closingStock.setScale(2, RoundingMode.CEILING)
        : BigDecimal.ZERO;
  }

  public void setClosingStock(BigDecimal closingStock) {
    this.closingStock = closingStock;
  }

  public BigDecimal getClosingStockValue() {

    return (closingStockValue != null)
        ? closingStockValue.setScale(2, RoundingMode.CEILING)
        : BigDecimal.ZERO;
  }

  public void setClosingStockValue(BigDecimal closingStockValue) {
    this.closingStockValue = closingStockValue;
  }

  public BigDecimal getQuantityBoughtPrev() {
    return (quantityBoughtPrev != null)
        ? quantityBoughtPrev.setScale(2, RoundingMode.CEILING)
        : BigDecimal.ZERO;
  }

  public void setQuantityBoughtPrev(BigDecimal quantityBoughtPrev) {
    this.quantityBoughtPrev = quantityBoughtPrev;
  }

  public BigDecimal getQuantitySoldPrev() {
    return (quantitySoldPrev != null)
        ? quantitySoldPrev.setScale(2, RoundingMode.CEILING)
        : BigDecimal.ZERO;
  }

  public void setQuantitySoldPrev(BigDecimal quantitySoldPrev) {
    this.quantitySoldPrev = quantitySoldPrev;
  }

  @Override
  public String toString() {
    return "InventoryMgmtReportGeneralVo{"
        + "id='"
        + id
        + '\''
        + ", productId='"
        + productId
        + '\''
        + ", productCategory='"
        + productCategory
        + '\''
        + ", productName='"
        + productName
        + '\''
        + ", openingStock="
        + openingStock
        + ", quantityBought="
        + quantityBought
        + ", quantitySold="
        + quantitySold
        + ", stockRate="
        + stockRate
        + ", closingStock="
        + closingStock
        + ", closingStockValue="
        + closingStockValue
        + ", quantityBoughtPrev="
        + quantityBoughtPrev
        + ", quantitySoldPrev="
        + quantitySoldPrev
        + '}';
  }
}
