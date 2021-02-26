package com.blackstrawai.export;

import java.sql.Date;
import java.util.List;

public class ContraExportVo {

	private String referenceNo;
	private Date date;
	private Integer id;
	private String status;
	
	private List<ContraEntriesExportVo> contraEntries;

	public List<ContraEntriesExportVo> getContraEntries() {
		return contraEntries;
	}

	public void setContraEntries(List<ContraEntriesExportVo> contraEntries) {
		this.contraEntries = contraEntries;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
