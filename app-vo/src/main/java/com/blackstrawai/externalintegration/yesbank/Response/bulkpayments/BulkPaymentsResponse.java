package com.blackstrawai.externalintegration.yesbank.Response.bulkpayments;

import com.blackstrawai.externalintegration.yesbank.Response.common.CodeVo;
import com.blackstrawai.externalintegration.yesbank.Response.common.LinksVo;
import com.blackstrawai.externalintegration.yesbank.Response.common.MetaData;
import com.fasterxml.jackson.annotation.JsonProperty;


public class BulkPaymentsResponse extends MetaData{

	@JsonProperty("Data")
	private BulkPaymentsDataResponse  data;
	  
	@JsonProperty("Links")
	private LinksVo  links;

	@JsonProperty("Code")
	private CodeVo code;
	  
	public BulkPaymentsDataResponse getData() {
		return data;
	}

	public void setData(BulkPaymentsDataResponse data) {
		this.data = data;
	}

	public LinksVo getLinks() {
		return links;
	}

	public void setLinks(LinksVo links) {
		this.links = links;
	}

	public CodeVo getCode() {
		return code;
	}

	public void setCode(CodeVo code) {
		this.code = code;
	}
	  
	  
}
