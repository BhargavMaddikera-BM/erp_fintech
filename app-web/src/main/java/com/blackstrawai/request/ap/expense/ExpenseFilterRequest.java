package com.blackstrawai.request.ap.expense;

import com.blackstrawai.common.BaseRequest;

import java.sql.Timestamp;

public class ExpenseFilterRequest extends BaseRequest {

    private Integer statusId;
    private Integer natureOfSpendingId;
    private Boolean isAttachment;
    private String minAmountRange;
    private String maxAmountRange;
    private Timestamp startDateRange;
    private Timestamp endDateRange;


    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getNatureOfSpendingId() {
        return natureOfSpendingId;
    }

    public void setNatureOfSpendingId(Integer natureOfSpendingId) {
        this.natureOfSpendingId = natureOfSpendingId;
    }

    public Boolean getAttachment() {
        return isAttachment;
    }

    public void setAttachment(Boolean attachment) {
        isAttachment = attachment;
    }

    public String getMinAmountRange() {
        return minAmountRange;
    }

    public void setMinAmountRange(String minAmountRange) {
        this.minAmountRange = minAmountRange;
    }

    public String getMaxAmountRange() {
        return maxAmountRange;
    }

    public void setMaxAmountRange(String maxAmountRange) {
        this.maxAmountRange = maxAmountRange;
    }

    public Timestamp getStartDateRange() {
        return startDateRange;
    }

    public void setStartDateRange(Timestamp startDateRange) {
        this.startDateRange = startDateRange;
    }

    public Timestamp getEndDateRange() {
        return endDateRange;
    }

    public void setEndDateRange(Timestamp endDateRange) {
        this.endDateRange = endDateRange;
    }
}
