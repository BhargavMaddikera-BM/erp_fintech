package com.blackstrawai.ap.expense;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.List;

public class ExpensesVO extends BaseVo {
    private Integer id;
    private Integer accountTypeId;
    private Integer natureOfSpendingId;
    private String natureOfSpendingDesc;
    private Integer statusId;
    private String expenseStatusName;
    private Integer employeeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private String expenseTimeStamp;
    private Timestamp expenseStatusUpdateTimeStamp;
    private Boolean cash;
    private Integer organizationId;
    private Boolean superAdmin;
    private String amount;
    private String status;
    private String roleName;

    public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	private List<ExpensesAddedVo> expensesAddedVoList;
    private List<UploadFileVo> attachments;
    private List<Integer> attachmentsToRemove;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
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

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Boolean getCash() {
        return cash;
    }

    public Boolean getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    public Boolean isCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }

    public String getNatureOfSpendingDesc() {
        return natureOfSpendingDesc;
    }

    public void setNatureOfSpendingDesc(String natureOfSpendingDesc) {
        this.natureOfSpendingDesc = natureOfSpendingDesc;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpenseStatusName() {
        return expenseStatusName;
    }

    public void setExpenseStatusName(String expenseStatusName) {
        this.expenseStatusName = expenseStatusName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UploadFileVo> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<UploadFileVo> attachments) {
        this.attachments = attachments;
    }

    public List<Integer> getAttachmentsToRemove() {
        return attachmentsToRemove;
    }
    public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
        this.attachmentsToRemove = attachmentsToRemove;
    }

    @Override
    public String toString() {
        return "ExpensesVO{" +
                "id=" + id +
                ", accountTypeId=" + accountTypeId +
                ", natureOfSpendingId=" + natureOfSpendingId +
                ", natureOfSpendingDesc='" + natureOfSpendingDesc + '\'' +
                ", statusId=" + statusId +
                ", expenseStatusName='" + expenseStatusName + '\'' +
                ", employeeId=" + employeeId +
                ", expenseTimeStamp=" + expenseTimeStamp +
                ", expenseStatusUpdateTimeStamp=" + expenseStatusUpdateTimeStamp +
                ", cash=" + cash +
                ", organizationId=" + organizationId +
                ", superAdmin=" + superAdmin +
                ", Amount='" + amount + '\'' +
                ", status='" + status + '\'' +
                ", expensesAddedVoList=" + expensesAddedVoList +
                '}';
    }
}
