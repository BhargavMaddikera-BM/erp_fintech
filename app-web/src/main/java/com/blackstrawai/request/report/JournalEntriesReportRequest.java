package com.blackstrawai.request.report;

import com.blackstrawai.common.BaseRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

public class JournalEntriesReportRequest extends BaseRequest {

	private Integer organizationId;
	private String startDate;
	private String endDate;
	private String dateFormat;
	

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public String toString() {
		return "JournalEntriesReportRequest [organizationId=" + organizationId + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", dateFormat=" + dateFormat + "]";
	}
	
	
}
