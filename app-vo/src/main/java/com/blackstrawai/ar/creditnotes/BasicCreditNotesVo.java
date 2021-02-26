package com.blackstrawai.ar.creditnotes;

import com.blackstrawai.common.TokenVo;


public class BasicCreditNotesVo extends TokenVo{
	private int id;  	  
	private String creditNoteNumber;
	private String balanceDue;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getCreditNoteNumber() {
		return creditNoteNumber;
	}
	public void setCreditNoteNumber(String creditNoteNumber) {
		this.creditNoteNumber = creditNoteNumber;
	}
	public String getBalanceDue() {
		return balanceDue;
	}
	public void setBalanceDue(String balanceDue) {
		this.balanceDue = balanceDue;
	}    
	
}