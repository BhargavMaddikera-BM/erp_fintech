package com.blackstrawai.upload;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class ListAccountingEntriesUploadVo extends BaseVo {

	private List<AccountingEntriesUploadVo> accountingEntries;

	public List<AccountingEntriesUploadVo> getAccountingEntries() {
		return accountingEntries;
	}

	public void setAccountingEntries(List<AccountingEntriesUploadVo> accountingEntries) {
		this.accountingEntries = accountingEntries;
	}

}
