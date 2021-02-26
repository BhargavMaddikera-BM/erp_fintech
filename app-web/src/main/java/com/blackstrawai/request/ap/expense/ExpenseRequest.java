package com.blackstrawai.request.ap.expense;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

import java.util.List;

public class ExpenseRequest extends BaseRequest {


    private Boolean isSuperAdmin;
    private Integer organizationId;

    private ExpenseRequestDetails expenseGeneralInfo;
    private List<UploadFileRequest> attachments;
    private List<Integer> attachmentsToRemove;


    public ExpenseRequestDetails getExpenseGeneralInfo() {
        return expenseGeneralInfo;
    }

    public void setExpenseGeneralInfo(ExpenseRequestDetails expenseGeneralInfo) {
        this.expenseGeneralInfo = expenseGeneralInfo;
    }


    public List<UploadFileRequest> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<UploadFileRequest> attachments) {
        this.attachments = attachments;
    }

    public Boolean getSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public List<Integer> getAttachmentsToRemove() {
        return attachmentsToRemove;
    }

    public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
        this.attachmentsToRemove = attachmentsToRemove;
    }
}
