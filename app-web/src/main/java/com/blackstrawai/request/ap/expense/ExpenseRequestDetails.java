package com.blackstrawai.request.ap.expense;

import com.blackstrawai.ap.expense.ExpensesAddedVo;

import java.sql.Timestamp;
import java.util.List;

public class ExpenseRequestDetails {

    private Integer id;
    private Integer accountTypeId;
    private Integer natureOfSpendingId;
    private Integer statusId;
    private Integer employeeId;
    private String expenseTimeStamp;
    private Timestamp expenseStatusUpdateTimeStamp;
    private Boolean isCash;
    private String status;




    private List<ExpensesAddedVo> expensesAddedVoList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Integer accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public Integer getNatureOfSpendingId() {
        return natureOfSpendingId;
    }

    public void setNatureOfSpendingId(Integer natureOfSpendingId) {
        this.natureOfSpendingId = natureOfSpendingId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getExpenseTimeStamp() {
        return expenseTimeStamp;
    }

    public void setExpenseTimeStamp(String expenseTimeStamp) {
        this.expenseTimeStamp = expenseTimeStamp;
    }

    public Timestamp getExpenseStatusUpdateTimeStamp() {
        return expenseStatusUpdateTimeStamp;
    }

    public void setExpenseStatusUpdateTimeStamp(Timestamp expenseStatusUpdateTimeStamp) {
        this.expenseStatusUpdateTimeStamp = expenseStatusUpdateTimeStamp;
    }

    public List<ExpensesAddedVo> getExpensesAddedVoList() {
        return expensesAddedVoList;
    }

    public void setExpensesAddedVoList(List<ExpensesAddedVo> expensesAddedVoList) {
        this.expensesAddedVoList = expensesAddedVoList;
    }

    public Boolean getCash() {
        return isCash;
    }

    public void setCash(Boolean cash) {
        this.isCash = cash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
