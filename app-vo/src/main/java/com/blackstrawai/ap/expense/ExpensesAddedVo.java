package com.blackstrawai.ap.expense;

import java.sql.Timestamp;

public class ExpensesAddedVo {

    private Integer id;
    private Integer expensesAccountId;
    private String expenseAccountName;
    private String expenseAccountLevel;
    private Integer expensesId;
    private String notes;
    private String amount;
    private Integer vendorId;
    private String vendorName;
    private Integer customerId;
    private String customerName;
    private Integer vendorIdOrCustomerId;

    private Timestamp createTimeStamp;
    private Timestamp updateTimeStamp;
    private String status;
    private Boolean customer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExpensesAccountId() {
        return expensesAccountId;
    }

    public void setExpensesAccountId(Integer expensesAccountId) {
        this.expensesAccountId = expensesAccountId;
    }

    public Integer getExpensesId() {
        return expensesId;
    }

    public void setExpensesId(Integer expensesId) {
        this.expensesId = expensesId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Timestamp getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Timestamp createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Integer getVendorIdOrCustomerId() {
        return vendorIdOrCustomerId;
    }

    public void setVendorIdOrCustomerId(Integer vendorIdOrCustomerId) {
        this.vendorIdOrCustomerId = vendorIdOrCustomerId;
    }

    public Boolean getCustomer() {
        return customer;
    }

    public void setCustomer(Boolean customer) {
        this.customer = customer;
    }

    public String getExpenseAccountName() {
        return expenseAccountName;
    }

    public void setExpenseAccountName(String expenseAccountName) {
        this.expenseAccountName = expenseAccountName;
    }

    public String getExpenseAccountLevel() {
        return expenseAccountLevel;
    }

    public void setExpenseAccountLevel(String expenseAccountLevel) {
        this.expenseAccountLevel = expenseAccountLevel;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return "ExpensesAddedVo{" +
                "id=" + id +
                ", expensesAccountId=" + expensesAccountId +
                ", expensesId=" + expensesId +
                ", notes='" + notes + '\'' +
                ", amount='" + amount + '\'' +
                ", vendorId=" + vendorId +
                ", customerId=" + customerId +
                ", vendorIdOrCustomerId=" + vendorIdOrCustomerId +
                ", createTimeStamp=" + createTimeStamp +
                ", updateTimeStamp=" + updateTimeStamp +
                ", status='" + status + '\'' +
                ", customer=" + customer +
                '}';
    }
}
